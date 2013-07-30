package st.rattmuffen.feedme.client;

import java.util.ArrayList;

import st.rattmuffen.feedme.shared.Feed;
import st.rattmuffen.feedme.shared.FeedEntry;
import st.rattmuffen.feedme.shared.FieldVerifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FeedMe_web implements EntryPoint {

	public final RssServiceAsync rssService = GWT.create(RssService.class);

	public Button addButton;
	public TextBox addressField;
	public Label errorLabel;

	public ListBox feedList;

	public ArrayList<Feed> feeds;

	DockLayoutPanel panel;
	VerticalPanel menuPanel;
	
	ScrollPanel sp;
	HTML titleHTML;
	
	public String[] startingFeeds = {"https://github.com/hagward.atom","http://feber.se/rss",};

	public void onModuleLoad() {
		feeds = new ArrayList<Feed>();

		addButton = new Button("Add feed");
		addButton.addStyleName("addButton");

		addressField = new TextBox();

		InputHandler handler = new InputHandler(this);
		addButton.addClickHandler(handler);
		addressField.addKeyUpHandler(handler);

		feedList = new ListBox(false);
		feedList.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if (sp!=null)
					panel.remove(sp);
				if (titleHTML != null)
					panel.remove(titleHTML);
				

				int i = feedList.getSelectedIndex();
				Feed f = feeds.get(i);

				titleHTML = new HTML("<div id=\"feedTitle\"><img src=\"" + f.getImage() + "\" />" + 
						f.getDescription() + "</div>");
	

				EntryCell cell = new EntryCell(); 

				CellList<FeedEntry> cellList = new CellList<FeedEntry>(cell);
				cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
				cellList.setRowCount(f.entries.size(), true);
				cellList.setRowData(0, f.entries);
				cellList.addStyleName("list");
				
				sp = new ScrollPanel(cellList);
				panel.addNorth(titleHTML, 60);
				panel.add(sp);
			}
		});

		menuPanel = new VerticalPanel();

		menuPanel.setSpacing(4);
		menuPanel.add(new HTML("<h1>FeedMe</h1>"));

		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(4);
		hp.add(addressField);
		hp.add(addButton);

		menuPanel.add(hp);
		menuPanel.add(feedList);

		errorLabel = new Label();
		menuPanel.add(new HTML("<div id=\"creds\">by <a href=\"https://twitter.com/rattmuffen\">@rattmuffen</a></div>"));
		menuPanel.add(errorLabel);
		menuPanel.addStyleName("menu");
		
		
		errorLabel.setHeight(Window.getClientHeight() + "px");
		Window.addResizeHandler(new ResizeHandler() {

		 public void onResize(ResizeEvent event) {
		   int height = event.getHeight();
		   errorLabel.setHeight(height + "px");
		 }
		});

		panel = new DockLayoutPanel(Unit.PX);
		panel.addWest(menuPanel, 270);

		feedList.setWidth("270 px");

		RootLayoutPanel rp = RootLayoutPanel.get();
		rp.add(panel);
		
		// Add starting feeds...
		for (int i = 0; i < startingFeeds.length; i++) {
			sendAddressToServer(startingFeeds[i]);
		}
	}

	public void sendAddressToServer(String textToServer) throws IllegalArgumentException {
		addButton.setEnabled(false);

		rssService.getFeed(textToServer,new AsyncCallback<Feed>() {
			public void onFailure(Throwable caught) {
				errorLabel.setText("Remote Procedure Call - Failure");
				addButton.setEnabled(true);
			}

			public void onSuccess(Feed result) {
				feeds.add(result);
				feedList.addItem(result.title);

				feedList.setSelectedIndex(feedList.getItemCount());

				System.out.println("Added " + result.title + " to feeds...");

				addressField.setText("");
				addButton.setEnabled(true);
			}
		});
	}
	
	public void sendAddressToServer() throws IllegalArgumentException {
		errorLabel.setText("");
		String textToServer = addressField.getText();
		if (!FieldVerifier.isValidAddress(textToServer)) {
			errorLabel.setText("Please enter a valid address.");
			return;
		}
		sendAddressToServer(textToServer);
	}
}

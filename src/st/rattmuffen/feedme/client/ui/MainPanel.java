package st.rattmuffen.feedme.client.ui;

import st.rattmuffen.feedme.client.FeedMe_web;
import st.rattmuffen.feedme.shared.Feed;
import st.rattmuffen.feedme.shared.FeedEntry;
import st.rattmuffen.feedme.shared.FieldVerifier;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class MainPanel extends DockLayoutPanel {
	
	VerticalPanel menuPanel;

	ScrollPanel sp;
	HorizontalPanel titlePanel;

	public Button addButton,showAllButton,removeAllButton;
	public Label errorLabel;
	
	public InputHandler handler;

	public FeedTree feedTree;

	FeedMe_web controller;
	
	public Feed currentFeed;
	
	AddFeedPopup addPopup;
	EditFeedPopup editPopup;

	public MainPanel(FeedMe_web c) {
		super(Unit.PX);
		controller = c;
		
		handler = new InputHandler(this);
		addPopup = new AddFeedPopup(this);
		editPopup = new EditFeedPopup(this);
	}
	
	public void createAndShowGUI() {
		addButton = new Button("Add...");
		addButton.addStyleName("addButton");
		addButton.addClickHandler(handler);
		
		showAllButton = new Button("Show all");
		showAllButton.addClickHandler(handler);
		
		removeAllButton = new Button("Remove all");
		removeAllButton.addClickHandler(handler);

		feedTree = new FeedTree(controller,controller.feeds);

		menuPanel = new VerticalPanel();
		menuPanel.setSpacing(4);
		menuPanel.add(new HTML("<h1>FeedMe</h1>"));
		menuPanel.add(new HTML("<div id=\"creds\">by <a href=\"https://twitter.com/rattmuffen\">@rattmuffen</a></div>"));

		HorizontalPanel hp = new HorizontalPanel();
		hp.addStyleName("center");
		hp.setSpacing(4);
		hp.add(addButton);
		hp.add(showAllButton);
		hp.add(removeAllButton);
		

		menuPanel.add(hp);
		menuPanel.add(feedTree);

		errorLabel = new Label();
		errorLabel.addStyleName("serverResponseLabelError");
		
		menuPanel.add(errorLabel);
		menuPanel.addStyleName("menu");

		errorLabel.setHeight(Window.getClientHeight() + "px");
		Window.addResizeHandler(new ResizeHandler() {
			public void onResize(ResizeEvent event) {
				int height = event.getHeight();
				errorLabel.setHeight(height + "px");
			}
		});

		this.addWest(menuPanel, 270);
		
		RootLayoutPanel rp = RootLayoutPanel.get();
		rp.add(this);
	}
	
	public void createAndShowAddPopup() {
		addPopup.createAndShowGUI();
	}
	
	public void createAndShowEditPopup(Feed f) {
		editPopup.createAndShowGUI(f);
	}

	public void setFeed(final Feed f) {
		if (sp!=null)
			this.remove(sp);
		if (titlePanel != null)
			this.remove(titlePanel);
		
		currentFeed = f;

		titlePanel = new HorizontalPanel();
		
		HTML title = new HTML("<div id=\"feedTitle\"><img src=\"" + f.getImage() + "\" />" + 
				f.getDescription() + "</div>");
		titlePanel.add(title);

		EntryCell cell = new EntryCell(); 
		CellList<FeedEntry> cellList = new CellList<FeedEntry>(cell);
		cellList.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		cellList.setRowCount(f.entries.size(), true);
		cellList.setRowData(0, f.entries);
		cellList.addStyleName("list");

		sp = new ScrollPanel(cellList);
		this.addNorth(titlePanel, 60);
		this.add(sp);
	}

	public void showError(String string) {
		errorLabel.setText(string);
		addButton.setEnabled(true);
	}
	
	public void sendAddressToServer() throws IllegalArgumentException {
		errorLabel.setText("");

		String textToServer = addPopup.addressField.getText();
		if (!FieldVerifier.isValidAddress(textToServer)) {
			errorLabel.setText("Please enter a valid address.");
			return;
		}
		
		controller.sendAddressToServer(textToServer);
	}

	public void setEmpty() {
		if (sp!=null)
			this.remove(sp);
		if (titlePanel != null)
			this.remove(titlePanel);
	}

	public void removeAllFeeds() {
		controller.removeAllFeeds();
		setEmpty();
	}

	public void showAllFeeds() {
		controller.showAllFeeds();
	}

	public void updateTree() {
		feedTree.buildTree(controller, controller.feeds);
	}
}

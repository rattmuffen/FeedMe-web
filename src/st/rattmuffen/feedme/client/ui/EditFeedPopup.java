package st.rattmuffen.feedme.client.ui;

import st.rattmuffen.feedme.client.WebStorage;
import st.rattmuffen.feedme.shared.Feed;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EditFeedPopup extends DialogBox implements ChangeHandler {

	MainPanel controller;

	Button editButton,closeButton,removeButton;
	public TextBox titleField;
	public CategoryBox categoryBox;
	public CheckBox isFavoriteBox;
	
	public Feed feed;
	
	public EditFeedPopup(MainPanel c) {	
		super(true);

		controller = c;
	}

	public void createAndShowGUI(Feed f) {
		this.feed = f;
		String storedTitle = WebStorage.getFeedTitleFromStorage(feed.url);
		String feedTitle = feed.title;
		if(storedTitle != null && storedTitle != "") {
			feedTitle = storedTitle;
		}
		
		
		this.addStyleName("popup");
		this.setText("Edit feed " + feedTitle);
		this.setAnimationEnabled(true);
		this.setGlassEnabled(true);

		VerticalPanel contents = new VerticalPanel();

		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(5);
		titleField = new TextBox();
		titleField.addKeyUpHandler(controller.handler);
		titleField.setText(feedTitle);
		
		HTML h = new HTML("Title:");
		hp.add(h);
		hp.add(titleField);
		contents.add(hp);

		hp = new HorizontalPanel();
		hp.setSpacing(5);

		categoryBox = new CategoryBox(false);
		categoryBox.addChangeHandler(this);
		categoryBox.setSelectedIndex(0);
		categoryBox.setSelectedIndex(categoryBox.indexOf(feed.category));
		
		h = new HTML("Category:");
		hp.add(h);
		hp.add(categoryBox);
		contents.add(hp);
		
		
		
		
		hp = new HorizontalPanel();
		hp.setSpacing(5);
		
		isFavoriteBox = new CheckBox();
		isFavoriteBox.setValue(WebStorage.getFeedFavoriteFromStorage(feed.url));
		
		h = new HTML("Favorite:");
		hp.add(h);
		hp.add(isFavoriteBox);
		contents.add(hp);


		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.setSpacing(5);
		hp2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		editButton = new Button("Save");
		editButton.addClickHandler(controller.handler);
		removeButton = new Button("Remove feed");
		removeButton.addClickHandler(controller.handler);
		closeButton = new Button("Close");
		closeButton.addClickHandler(controller.handler);
		
		hp2.add(editButton);
		hp2.add(removeButton);
		hp2.add(closeButton);
		contents.add(hp2);
		
		

		setWidget(contents);

		this.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
			public void setPosition(int offsetWidth, int offsetHeight) {
				int left = (Window.getClientWidth() - offsetWidth) / 2;
				int top = (Window.getClientHeight() - offsetHeight) / 2;
				setPopupPosition(left, top);
			}
		});

		this.show();
		titleField.setFocus(true);
		categoryBox.setSize(titleField.getOffsetWidth() + "px", titleField.getOffsetHeight() + "px");
	}

	@Override
	public void onChange(ChangeEvent event) {

	}

}



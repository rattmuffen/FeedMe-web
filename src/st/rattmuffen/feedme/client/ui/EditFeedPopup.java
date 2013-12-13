package st.rattmuffen.feedme.client.ui;

import st.rattmuffen.feedme.client.FeedMe_web;
import st.rattmuffen.feedme.client.WebStorage;
import st.rattmuffen.feedme.shared.Feed;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EditFeedPopup extends DialogBox implements ChangeHandler, ClickHandler, KeyUpHandler {

	FeedMe_web controller;

	Button editButton,closeButton,removeButton;
	public TextBox titleField;
	public CategoryBox categoryBox;
	public CheckBox isFavoriteBox;
	
	public Feed feed;
	
	public EditFeedPopup(FeedMe_web c) {	
		super(true);

		controller = c;
	}

	public void createAndShowGUI(Feed f) {
		this.feed = f;
		String feedTitle = controller.getFeedTitle(f);
		
		
		this.addStyleName("popup");
		this.setText("Edit feed " + feedTitle);
		this.setAnimationEnabled(true);
		this.setGlassEnabled(true);

		VerticalPanel contents = new VerticalPanel();

		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(5);
		titleField = new TextBox();
		titleField.addKeyUpHandler(this);
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
		editButton = new Button("<i class=\"fa fa-check-circle\"></i> Save");
		editButton.addClickHandler(this);
		removeButton = new Button("<i class=\"fa fa-times-circle\"></i> Remove");
		removeButton.addClickHandler(this);
		closeButton = new Button("<i class=\"fa fa-reply\"></i> Close");
		closeButton.addClickHandler(this);
		
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

	@Override
	public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			String newCat = categoryBox.getItemText(categoryBox.getSelectedIndex());
			if (!newCat.equalsIgnoreCase(feed.category)) {
				WebStorage.saveFeedCategoryToStorage(newCat,feed.url);
				feed.category = newCat;
			}
			
			String newTitle = titleField.getText();
			if (!newTitle.equalsIgnoreCase(feed.title)) {
				WebStorage.saveFeedTitleToStorage(newTitle, feed.url);
			}
			
			boolean isFavorite = isFavoriteBox.getValue();
			WebStorage.saveFeedFavoriteToStorage(feed.url, isFavorite);
			
			this.hide();
			controller.updateTree(feed);
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();
		
		if (sender == closeButton) {
			this.hide();
		} else if (sender == removeButton) {
			controller.removeFeed(feed);
			controller.setFeed(null);
			this.hide();
		} else if (sender == editButton) {
			String newCat = categoryBox.getItemText(categoryBox.getSelectedIndex());
			if (!newCat.equalsIgnoreCase(feed.category)) {
				WebStorage.saveFeedCategoryToStorage(newCat,feed.url);
				feed.category = newCat;
			}
			
			String newTitle = titleField.getText();
			if (!newTitle.equalsIgnoreCase(feed.title)) {
				WebStorage.saveFeedTitleToStorage(newTitle, feed.url);
			}
			
			boolean isFavorite = isFavoriteBox.getValue();
			WebStorage.saveFeedFavoriteToStorage(feed.url, isFavorite);
			
			this.hide();
			controller.updateTree(feed);
		}
	}

}



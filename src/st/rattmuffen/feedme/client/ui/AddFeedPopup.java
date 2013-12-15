package st.rattmuffen.feedme.client.ui;

import st.rattmuffen.feedme.client.FeedMe_web;
import st.rattmuffen.feedme.client.WebStorage;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AddFeedPopup extends DialogBox implements ChangeHandler, ClickHandler, KeyUpHandler {

	FeedMe_web controller;

	IconButton addButton,closeButton,addCategoryButton;
	public TextBox addressField,newCategoryField;
	public CategoryBox categoryBox;

	HorizontalPanel addCatPanel;

	public AddFeedPopup(FeedMe_web c) {
		super(true);

		controller = c;
	}

	public void createAndShowGUI() {
		this.addStyleName("popup");
		this.setText("Add a feed");
		this.setAnimationEnabled(true);
		this.setGlassEnabled(true);

		VerticalPanel contents = new VerticalPanel();

		HorizontalPanel hp = new HorizontalPanel();
		hp.setSpacing(5);
		addressField = new TextBox();
		addressField.addKeyUpHandler(this);

		HTML h = new HTML("Address:");
		hp.add(h);
		hp.add(addressField);
		contents.add(hp);

		hp = new HorizontalPanel();
		hp.setSpacing(5);

		categoryBox = new CategoryBox(false);
		categoryBox.addChangeHandler(this);
		categoryBox.setSelectedIndex(0);
		categoryBox.addItem("Add new category...");

		h = new HTML("Category:");
		hp.add(h);
		hp.add(categoryBox);
		contents.add(hp);


		addCatPanel = new HorizontalPanel();
		addCatPanel.setSpacing(5);


		HTML h2 = new HTML("New category:");
		addCatPanel.add(h2);

		newCategoryField = new TextBox(); 
		addCategoryButton = new IconButton("Add");
		addCategoryButton.addClickHandler(this);

		addCatPanel.add(newCategoryField);
		addCatPanel.add(addCategoryButton);


		contents.add(addCatPanel);


		HorizontalPanel hp2 = new HorizontalPanel();
		hp2.setSpacing(5);
		hp2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		addButton = new IconButton("Add","fa-check-circle");
		addButton.addClickHandler(this);
		closeButton = new IconButton("Close","fa-reply");
		closeButton.addClickHandler(this);

		hp2.add(addButton);
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
		addressField.setFocus(true);
		categoryBox.setSize(addressField.getOffsetWidth() + "px", addressField.getOffsetHeight() + "px");
		addCatPanel.setVisible(false);
	}

	@Override
	public void onChange(ChangeEvent event) {

		if (event.getSource() == categoryBox) {
			int i = categoryBox.getSelectedIndex();
			if (i + 1 == categoryBox.getItemCount()) {
				addCatPanel.setVisible(true);
			} else {
				addCatPanel.setVisible(false);
			}
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		Object sender = event.getSource();

		if (sender == closeButton) {
			this.hide();
		} else if (sender == addButton) {
			String cat = categoryBox.getItemText(categoryBox.getSelectedIndex());

			if (categoryBox.getSelectedIndex() +1 == categoryBox.getItemCount()) {
				cat = "Default";
			}

			String url =  addressField.getText().trim();
			WebStorage.saveFeedCategoryToStorage(cat,url);
			controller.sendAddressToServer(url);
			this.hide();
		} else if (sender == addCategoryButton) {
			String cat = newCategoryField.getText().trim();
			WebStorage.addCategoryToStorage(cat);
			addCatPanel.setVisible(false);

			categoryBox.insertItem(cat,categoryBox.getItemCount()-1);
			categoryBox.setSelectedIndex(categoryBox.getItemCount()-2);
		}
	}
	
	
	@Override
	public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			try {
				String cat = categoryBox.getItemText(categoryBox.getSelectedIndex());
				
				if (categoryBox.getSelectedIndex()+1 == categoryBox.getItemCount()) 
					cat = "Default";
				
				String url =  addressField.getText().trim();
				WebStorage.saveFeedCategoryToStorage(cat, url);
				controller.panel.sendAddressToServer(url);
				this.hide();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}
}

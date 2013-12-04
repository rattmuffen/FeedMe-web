package st.rattmuffen.feedme.client.ui;

import st.rattmuffen.feedme.client.WebStorage;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Widget;

public class InputHandler implements ClickHandler, KeyUpHandler {

	MainPanel controller;

	public InputHandler(MainPanel parent) {
		controller = parent;
	}

	@Override
	public void onKeyUp(KeyUpEvent event) {
		if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
			try {
				String cat = controller.addPopup.categoryBox.getItemText(controller.addPopup.categoryBox.getSelectedIndex());
				
				if (controller.addPopup.categoryBox.getSelectedIndex()+1 == controller.addPopup.categoryBox.getItemCount()) 
					cat = "Default";
				WebStorage.saveFeedCategoryToStorage(cat, controller.addPopup.addressField.getText().trim());
				
				controller.sendAddressToServer();
				controller.addPopup.hide();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();

		if (sender == controller.addButton) {
			controller.createAndShowAddPopup();
		} else if (sender == controller.removeAllButton) {
			controller.removeAllFeeds();
		} else if (sender == controller.showAllButton) {
			controller.showAllFeeds();
		} else if (sender == controller.addPopup.closeButton) {
			controller.addPopup.hide();
		} else if (sender == controller.addPopup.addButton) {
			String cat = controller.addPopup.categoryBox.getItemText(controller.addPopup.categoryBox.getSelectedIndex());
			
			if (controller.addPopup.categoryBox.getSelectedIndex() +1 == controller.addPopup.categoryBox.getItemCount()) {
				cat = "Default";
			}

			WebStorage.saveFeedCategoryToStorage(cat, controller.addPopup.addressField.getText().trim());
			
			controller.sendAddressToServer();
			controller.addPopup.hide();
		} else if (sender == controller.addPopup.addCategoryButton) {
			String cat = controller.addPopup.newCategoryField.getText().trim();
			WebStorage.addCategoryToStorage(cat);
			controller.addPopup.addCatPanel.setVisible(false);
			
			controller.addPopup.categoryBox.insertItem(cat, controller.addPopup.categoryBox.getItemCount()-1);
			controller.addPopup.categoryBox.setSelectedIndex(controller.addPopup.categoryBox.getItemCount()-2);
		} else if (sender == controller.editPopup.closeButton) {
			controller.editPopup.hide();
		} else if (sender == controller.editPopup.removeButton) {
			controller.controller.removeFeed(controller.editPopup.feed); //lol, this needs a rewrite
			controller.controller.setFeed(null);
			controller.editPopup.hide();
		} else if (sender == controller.editPopup.editButton) {
			String newCat = controller.editPopup.categoryBox.getItemText(controller.editPopup.categoryBox.getSelectedIndex());
			if (!newCat.equalsIgnoreCase(controller.editPopup.feed.category)) {
				WebStorage.saveFeedCategoryToStorage(newCat, controller.editPopup.feed.url);
				controller.editPopup.feed.category = newCat;
			}
			
			String newTitle = controller.editPopup.titleField.getText();
			if (!newTitle.endsWith(controller.editPopup.feed.title)) {
				WebStorage.saveFeedTitleToStorage(newTitle, controller.editPopup.feed.url);
			}
			
			controller.editPopup.hide();
			controller.controller.updateTree(controller.editPopup.feed);
		}
	}
}

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
				String cat = controller.popup.categoryBox.getItemText(controller.popup.categoryBox.getSelectedIndex());
				
				if (controller.popup.categoryBox.getSelectedIndex()+1 == controller.popup.categoryBox.getItemCount()) 
					cat = "Default";
				WebStorage.saveFeedCategoryToStorage(cat, controller.popup.addressField.getText().trim());
				
				controller.sendAddressToServer();
				controller.popup.hide();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();

		if (sender == controller.addButton) {
			controller.createAndShowPopup();
		} else if (sender == controller.removeAllButton) {
			controller.removeAllFeeds();
		} else if (sender == controller.showAllButton) {
			controller.showAllFeeds();
		} else if (sender == controller.popup.closeButton) {
			controller.popup.hide();
		} else if (sender == controller.popup.addButton) {
			String cat = controller.popup.categoryBox.getItemText(controller.popup.categoryBox.getSelectedIndex());
			
			if (controller.popup.categoryBox.getSelectedIndex() +1 == controller.popup.categoryBox.getItemCount()) {
				cat = "Default";
			}

			WebStorage.saveFeedCategoryToStorage(cat, controller.popup.addressField.getText().trim());
			
			controller.sendAddressToServer();
			controller.popup.hide();
		} else if (sender == controller.popup.addCategoryButton) {
			String cat = controller.popup.newCategoryField.getText().trim();
			WebStorage.addCategoryToStorage(cat);
			controller.popup.addCatPanel.setVisible(false);
			
			controller.popup.categoryBox.insertItem(cat, controller.popup.categoryBox.getItemCount()-1);
			controller.popup.categoryBox.setSelectedIndex(controller.popup.categoryBox.getItemCount()-2);
		}
	}
}

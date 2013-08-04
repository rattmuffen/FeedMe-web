package st.rattmuffen.feedme.client.ui;

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
				controller.sendAddressToServer();
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
	}

	@Override
	public void onClick(ClickEvent event) {
		Widget sender = (Widget) event.getSource();

		if (sender == controller.addButton) {
			
			try {
				controller.sendAddressToServer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (sender == controller.removeAllButton) {
			controller.removeAllFeeds();
		} else if (sender == controller.showAllButton) {
			controller.showAllFeeds();
		}
	}

}

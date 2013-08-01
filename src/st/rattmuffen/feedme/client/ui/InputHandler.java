package st.rattmuffen.feedme.client.ui;

import st.rattmuffen.feedme.client.FeedMe_web;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.Widget;

public class InputHandler implements ClickHandler, KeyUpHandler {

	FeedMe_web controller;

	public InputHandler(FeedMe_web parent) {
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

		System.out.println(sender.getStyleName());

		if (sender.getStyleName().equals("gwt-Button removeButton")) {
			controller.removeFeed(controller.panel.currentFeed);
			controller.setFeed(null);
		} else {
			try {
				controller.sendAddressToServer();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}

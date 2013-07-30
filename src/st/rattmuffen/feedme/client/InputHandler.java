package st.rattmuffen.feedme.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;

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
		try {
			controller.sendAddressToServer();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}

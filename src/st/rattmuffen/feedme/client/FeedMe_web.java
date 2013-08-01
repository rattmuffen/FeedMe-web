package st.rattmuffen.feedme.client;

import java.util.ArrayList;

import st.rattmuffen.feedme.client.ui.InputHandler;
import st.rattmuffen.feedme.client.ui.MainPanel;
import st.rattmuffen.feedme.shared.Feed;
import st.rattmuffen.feedme.shared.FieldVerifier;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class FeedMe_web implements EntryPoint {

	public final RssServiceAsync rssService = GWT.create(RssService.class);
	public ArrayList<Feed> feeds;
	private Storage stockStore = null;

	public MainPanel panel;

	public InputHandler handler;

	public void onModuleLoad() {
		feeds = new ArrayList<Feed>();

		panel = new MainPanel(this);
		handler = new InputHandler(this);
		panel.createAndShowGUI();

		initiateLocalStorage();
	}

	private void initiateLocalStorage() {
		stockStore = Storage.getLocalStorageIfSupported();
		if (stockStore != null) {
			System.out.print("accessing local storage... ");
			ArrayList<String> urls = getAllFeedsFromLocalStorage();
			System.out.println(urls.size()  +" urls in storage!");

			for (String	url : urls) {
				sendAddressToServer(url);
			}
		} else {
			Window.alert("Your browser does not support HTML5 storage and won't be able to save feeds between sessions!");
		}
	}

	public void setFeed(Feed f) {
		if (f != null) {
			panel.setFeed(f);
			System.out.println("Selecting feed " + f.title);
		} else {
			panel.setEmpty();
		}
	}

	public void sendAddressToServer(String textToServer) throws IllegalArgumentException {
		panel.addButton.setEnabled(false);

		if (alreadyAdded(textToServer)) {
			panel.showError("You have already added this feed!");
		} else {

			rssService.getFeed(textToServer,new AsyncCallback<Feed>() {
				public void onFailure(Throwable caught) {
					panel.showError("Remote Procedure Call Failure");
				}

				public void onSuccess(Feed result) {
					feeds.add(result);
					panel.feedList.addFeed(result);
					saveFeedToLocalStorage(result);

					panel.addressField.setText("");
					panel.addButton.setEnabled(true);
				}
			});
		}
	}

	private boolean alreadyAdded(String textToServer) {
		for (Feed f : feeds) {
			if (f.url.equalsIgnoreCase(textToServer)) 
				return true;
		}
		return false;
	}

	public void sendAddressToServer() throws IllegalArgumentException {
		panel.errorLabel.setText("");
		String textToServer = panel.addressField.getText();
		if (!FieldVerifier.isValidAddress(textToServer)) {
			panel.errorLabel.setText("Please enter a valid address.");
			return;
		}
		sendAddressToServer(textToServer);
	}

	public ArrayList<String> getAllFeedsFromLocalStorage() {
		if (stockStore != null) {
			ArrayList<String> list = new ArrayList<String>();

			for (int i = 0; i < stockStore.getLength(); i++){
				String key = stockStore.key(i);
				list.add(stockStore.getItem(key));
			}
			return list;
		}
		return null;
	}

	public void saveFeedToLocalStorage(Feed f) {
		if (stockStore != null) {
			stockStore.setItem("Feed."+feeds.indexOf(f), f.url);
		}
	}

	public void removeFeed(Feed f) {
		System.out.println("Removing " + f.title);

		if (stockStore != null) {
			stockStore.removeItem("Feed."+feeds.indexOf(f));
		}
		
		feeds.remove(f);
		panel.feedList.remove(f);
	}
}

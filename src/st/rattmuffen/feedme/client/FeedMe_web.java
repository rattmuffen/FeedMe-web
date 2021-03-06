package st.rattmuffen.feedme.client;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import st.rattmuffen.feedme.client.ui.MainPanel;
import st.rattmuffen.feedme.shared.Feed;
import st.rattmuffen.feedme.shared.FeedEntry;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class FeedMe_web implements EntryPoint {

	public final RssServiceAsync rssService = GWT.create(RssService.class);
	public ArrayList<Feed> feeds;
	public Feed currentFeed;

	public MainPanel panel;

	public void onModuleLoad() {
		feeds = new ArrayList<Feed>();

		panel = new MainPanel(this);
		panel.createAndShowGUI();
		panel.toggleLoading();

		boolean supportsStorage = WebStorage.initiateStorage();
		if (supportsStorage) {
			System.out.print("accessing local storage... ");
			ArrayList<String> urls = WebStorage.getAllFeedURLsFromStorage();
			System.out.println(urls.size()  +" urls in storage!");
			if (urls.size() == 0)
				panel.toggleLoading();

			for (int i = 0; i < urls.size(); i++) {
				sendAddressToServer(urls.get(i), i == urls.size() - 1);
			}


		} else {
			Window.alert("Your browser does not support HTML5 storage and won't be able to save feeds between sessions!");
		}
	}

	public void setFeed(Feed f) {
		if (f != null) {
			currentFeed = f;
			panel.setFeed(f);
			f.unread = 0;
			WebStorage.saveDateToStorage(f.url);
		} else {
			panel.setEmpty();
		}
	}

	public String getFeedTitle(Feed f) {
		String storedTitle = WebStorage.getFeedTitleFromStorage(f.url);
		String feedTitle = f.title;
		if(storedTitle != null && storedTitle != "") {
			feedTitle = storedTitle;
		}
		return feedTitle;
	}

	public void sendAddressToServer(String textToServer) {
		sendAddressToServer(textToServer,false);
	}

	public void sendAddressToServer(String textToServer,final boolean lastFeed) throws IllegalArgumentException {
		panel.addButton.setEnabled(false);

		if (alreadyAdded(textToServer)) {
			panel.showError("You have already added this feed!");
			panel.addButton.setEnabled(true);
		} else {

			rssService.getFeed(textToServer,WebStorage.getDateFromStorage(textToServer), new AsyncCallback<Feed>() {
				public void onFailure(Throwable caught) {
					panel.showError("Remote Procedure Call Failure");
				}

				public void onSuccess(Feed result) {
					feeds.add(result);

					String storedCategory = WebStorage.getFeedCategoryFromStorage(result.url);
					result.category = (storedCategory == null) ? "Default" : storedCategory;

					WebStorage.saveFeedToLocalStorage(result);

					panel.updateTree();
					panel.addButton.setEnabled(true);

					if (lastFeed) {
						panel.toggleLoading();
					}
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

	public void removeFeed(Feed f) {
		WebStorage.removeFeedFromStorage(f);
		feeds.remove(f);
		panel.feedTree.buildTree(this, feeds);
	}

	public void updateTree() {
		panel.feedTree.buildTree(this, feeds);
	}

	public void updateTree(Feed updFeed) {
		ListIterator<Feed> it = feeds.listIterator();    
		if(it.hasNext()) {  
			Feed f = it.next();   

			if (f.url.equals(updFeed.url)) {
				feeds.remove(f);
				feeds.add(updFeed);
			}
		}  
		panel.feedTree.buildTree(this, feeds);
	}


	public void showAllFeeds() {
		Feed allFeeds = new Feed();
		allFeeds.description = "All feeds";
		allFeeds.link = "";
		allFeeds.url = "";
		allFeeds.unread = 0;
		allFeeds.title = "All feeds";

		for (Feed f : feeds) {
			for (FeedEntry fe : f.entries) {
				allFeeds.addEntry(fe);
			}
		}

		allFeeds.sort();
		System.out.println("All feeds size: " + allFeeds.entries.size());

		setFeed(allFeeds);
	}

	public void showAllForCategory(String cat) {
		Feed catFeeds = new Feed();
		catFeeds.description = "All feeds for " + cat;
		catFeeds.link = "";
		catFeeds.url = "";
		catFeeds.unread = 0;
		catFeeds.title = "All feeds for " + cat;

		for (Feed f : panel.feedTree.getFeedsWithCategory(cat, feeds)) {
			for (FeedEntry fe : f.entries) {
				catFeeds.addEntry(fe);
			}
		}

		catFeeds.sort();
		setFeed(catFeeds);
	}

	public void showFavoriteFeeds() {
		Feed favFeeds = new Feed();
		favFeeds.description = "Favorite feeds";
		favFeeds.link = "";
		favFeeds.url = "";
		favFeeds.unread = 0;
		favFeeds.title = "Favorite feeds";

		for (Feed f : panel.feedTree.getFavoriteFeeds(feeds)) {
			for (FeedEntry fe : f.entries) {
				favFeeds.addEntry(fe);
			}
		}

		favFeeds.sort();
		setFeed(favFeeds);
	}

	public void showShuffleFeeds() {
		Feed shuffledFeed = new Feed();
		if (!currentFeed.description.endsWith("(shuffled)"))
			shuffledFeed.description = currentFeed.description + " (shuffled)";
		else
			shuffledFeed.description = currentFeed.description;
		shuffledFeed.link = "";
		shuffledFeed.url = "";
		shuffledFeed.unread = 0;
		if (!getFeedTitle(currentFeed).endsWith("(shuffled)"))
			shuffledFeed.title = getFeedTitle(currentFeed) + " (shuffled)";
		else
			shuffledFeed.title = getFeedTitle(currentFeed);

		ArrayList<FeedEntry> allEntries = new ArrayList<FeedEntry>();
		for (FeedEntry fe : currentFeed.entries) {
			allEntries.add(fe);
		}

		while (!allEntries.isEmpty()) {
			int rand = (int) (Math.random() * allEntries.size());
			FeedEntry entry = allEntries.get(rand);
			shuffledFeed.addEntry(entry);
			allEntries.remove(entry);
		}

		setFeed(shuffledFeed);
	}

	public void removeAllFeeds() {
		ArrayList<Feed> feedCopy = new ArrayList<Feed>();
		feedCopy.addAll(feeds);

		for (Feed f : feedCopy) {
			removeFeed(f);
		}

		WebStorage.clearStorage();
		feedCopy.clear();
		panel.feedTree.buildTree(this, feeds);
	}
}

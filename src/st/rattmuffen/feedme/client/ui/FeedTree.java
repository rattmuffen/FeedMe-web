package st.rattmuffen.feedme.client.ui;

import java.util.ArrayList;

import st.rattmuffen.feedme.client.FeedMe_web;
import st.rattmuffen.feedme.shared.Feed;

import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class FeedTree extends Tree implements SelectionHandler<TreeItem> {

	FeedMe_web controller;
	
	public FeedTree(FeedMe_web c, ArrayList<Feed> feeds) {
		super();
		this.controller = c;
		buildTree(c,feeds);
	}
	
	public void buildTree(FeedMe_web c, ArrayList<Feed> feeds) {
		this.removeItems();
		
		addTextItem("All feeds (" + feeds.size() + ")");
		getItem(0).addItem(new FeedList(c,feeds));
		
		this.setAnimationEnabled(true);
		
		ArrayList<String> usedCategories = getUsedCategories(feeds);
		for (int i = 0; i < usedCategories.size(); i++) {
			ArrayList<Feed> catFeeds = getFeedsWithCategory(usedCategories.get(i),feeds);
			addTextItem(usedCategories.get(i) + " (" + catFeeds.size() + ")");
			getItem(i+1).addItem(new FeedList(c,catFeeds));
		}
		
		addSelectionHandler(this);
		
		getItem(0).setState(true);
	}
	
	
	
	private ArrayList<String> getUsedCategories(ArrayList<Feed> feeds) {
		ArrayList<String> cats = new ArrayList<String>();
		
		for (Feed f : feeds) {
			System.out.println(f.category);
			if (!cats.contains(f.category))
				cats.add(f.category);
		}
		
		return cats;
	}
	
	public ArrayList<Feed> getFeedsWithCategory(String category, ArrayList<Feed> feeds) {
		ArrayList<Feed> catFeeds = new ArrayList<Feed>();
		
		for (Feed feed : feeds) {
			if (feed.category.equals(category))
				catFeeds.add(feed);
		}
		return catFeeds;
	}

	@Override
	public void onSelection(SelectionEvent<TreeItem> event) {
		if (event.getSelectedItem().getChildCount() != 0) {
			String label = event.getSelectedItem().asTreeItem().getText();
			String category = label.substring(0,label.lastIndexOf("(") - 1);
			
			if (category.equals("All feeds")) {
				controller.showAllFeeds();
			} else {
				controller.showAllForCategory(category);
			}
		}
	}
}

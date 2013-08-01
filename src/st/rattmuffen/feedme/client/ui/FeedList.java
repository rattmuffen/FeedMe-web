package st.rattmuffen.feedme.client.ui;

import java.util.ArrayList;

import st.rattmuffen.feedme.client.FeedMe_web;
import st.rattmuffen.feedme.shared.Feed;

import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class FeedList extends CellList<Feed> implements SelectionChangeEvent.Handler {

	private ListDataProvider<Feed> provider;
	private SingleSelectionModel<Feed> selectionModel;
	private FeedMe_web controller;

	public FeedList(FeedMe_web c, ArrayList<Feed> feeds) {
		super(new FeedCell(c));

		controller = c;

		selectionModel = new SingleSelectionModel<Feed>();
		selectionModel.addSelectionChangeHandler(this);
		this.setSelectionModel(selectionModel);

		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		this.setRowCount(feeds.size(), true);
		this.setRowData(0, feeds);
		this.addStyleName("feedList");


		provider = new ListDataProvider<Feed>(feeds);
		provider.addDataDisplay(this);
	}

	public void addFeed(Feed result) {
		provider.getList().add(result);
	}

	public void setSelectedIndex(int rowCount) {
		selectionModel.setSelected(provider.getList().get(rowCount), true);
	}

	@Override
	public void onSelectionChange(SelectionChangeEvent event) {
		Feed selected = selectionModel.getSelectedObject();
		controller.setFeed(selected);
	}

	public void remove(Feed f) {
		provider.getList().remove(f);
	}

}

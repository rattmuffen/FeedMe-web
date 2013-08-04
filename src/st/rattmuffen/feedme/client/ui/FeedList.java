package st.rattmuffen.feedme.client.ui;

import java.util.ArrayList;

import st.rattmuffen.feedme.client.FeedMe_web;
import st.rattmuffen.feedme.shared.Feed;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;

public class FeedList extends CellTable<Feed> implements SelectionChangeEvent.Handler {

	private ListDataProvider<Feed> provider;
	private SingleSelectionModel<Feed> selectionModel;
	private FeedMe_web controller;

	public FeedList(FeedMe_web c, ArrayList<Feed> feeds) {
		super(15,(Resources)GWT.create(TableResources.class));

		controller = c;

		TextColumn<Feed> nameColumn = new TextColumn<Feed>() {
			@Override
			public String getValue(Feed f) {
				return f.title + " (" + f.unread + ")";
			}

			@Override
			public void render(Context context, Feed object, SafeHtmlBuilder sb) {
				if (object != null)
					super.render(context, object, sb);
			}
		};
		nameColumn.setCellStyleNames("feedListEntry");

		ButtonCell buttonCell = new ButtonCell();
		Column<Feed, String> buttonColumn = new Column<Feed, String>(buttonCell) {

			@Override
			public String getValue(Feed object) {
				return "X";
			}

			@Override
			public void render(Cell.Context context,Feed object,SafeHtmlBuilder sb) {
				if (object != null)
					super.render(context,object,sb);
			}
			
			@Override
			public void onBrowserEvent(Context context, Element elem,
					Feed object, NativeEvent event) {
				event.preventDefault();

				controller.removeFeed(object);
				controller.setFeed(null);
			}
		};

		this.addColumn(nameColumn , "");
		this.addColumn(buttonColumn, "");

		this.setWidth("100%", true);

		this.setColumnWidth(nameColumn, 80.0, Unit.PCT);
		this.setColumnWidth(buttonColumn, 20.0, Unit.PCT);

		this.addStyleName("feedList");

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

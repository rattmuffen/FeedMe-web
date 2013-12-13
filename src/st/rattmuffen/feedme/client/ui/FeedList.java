package st.rattmuffen.feedme.client.ui;

import java.util.ArrayList;

import st.rattmuffen.feedme.client.FeedMe_web;
import st.rattmuffen.feedme.client.WebStorage;
import st.rattmuffen.feedme.shared.Feed;

import com.google.gwt.cell.client.ButtonCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.ImageCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
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
				String title = f.title;
				String storedTitle = WebStorage.getFeedTitleFromStorage(f.url);


				if (storedTitle != null && !storedTitle.equals(""))
					title = storedTitle;

				return " " + title + " (" + f.unread + ")";
			}

			@Override
			public void render(Context context, Feed object, SafeHtmlBuilder sb) {
				if (object != null) {
					final boolean fav = WebStorage.getFeedFavoriteFromStorage(object.url);

					sb.append(new SafeHtml() {
						private static final long serialVersionUID = 1L;
						@Override
						public String asString() {
							return fav ? Constants.FILLED_STAR : Constants.EMPTY_STAR;
						}
					});
					super.render(context, object, sb);
				}
			}
		};
		nameColumn.setCellStyleNames("feedListEntry");

		ButtonCell buttonCell = new ButtonCell();
		Column<Feed, String> buttonColumn = new Column<Feed, String>(buttonCell) {

			@Override
			public String getValue(Feed object) {
				return "Edit";
			}

			@Override
			public void render(Cell.Context context,Feed object,SafeHtmlBuilder sb) {
				sb.appendHtmlConstant("<button type=\"button\" class=\"gwt-Button\" tabindex=\"-1\"><i class=\"fa fa-pencil-square\"></i>");
				//sb.appendEscaped(" Edit");
				sb.appendHtmlConstant("</button>");
			}

			@Override
			public void onBrowserEvent(Context context, Element elem,
					Feed object, NativeEvent event) {
				event.preventDefault();

				controller.panel.createAndShowEditPopup(object);
			}
		};

		ImageCell iconCell = new ImageCell();
		Column<Feed, String> iconColumn = new Column<Feed, String>(iconCell) {

			@Override
			public String getValue(Feed object) {
				return "http://g.etfv.co/" + SafeHtmlUtils.htmlEscape(object.url);
			}

			@Override
			public void render(Cell.Context context,final Feed object,SafeHtmlBuilder sb) {
				if (object != null) {
					SafeHtml rendered = new SafeHtml() {
						final boolean fav = WebStorage.getFeedFavoriteFromStorage(object.url);
						private static final long serialVersionUID = 1L;

						@Override
						public String asString() {
							return "<img src=\"http://g.etfv.co/" + SafeHtmlUtils.htmlEscape(object.url) + "\"" + 
									"height=\"16\" width=\"16\">";
						}
					};

					sb.append(rendered);
				}
			}
		};

		iconColumn.setCellStyleNames("feedIconListEntry");

		this.addColumn(iconColumn , "");
		this.addColumn(nameColumn , "");
		this.addColumn(buttonColumn, "");

		this.setColumnWidth(iconColumn, 15.0, Unit.PX);
		this.setColumnWidth(nameColumn, 80.0, Unit.PX);
		this.setColumnWidth(buttonColumn, 20.0, Unit.PX);

		this.setWidth("100%", true);

		selectionModel = new SingleSelectionModel<Feed>();
		selectionModel.addSelectionChangeHandler(this);
		this.setSelectionModel(selectionModel);

		this.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		this.setRowCount(feeds.size(), true);
		this.setRowData(0, feeds);

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

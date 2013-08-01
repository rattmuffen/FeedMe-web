package st.rattmuffen.feedme.client.ui;

import st.rattmuffen.feedme.client.FeedMe_web;
import st.rattmuffen.feedme.shared.Feed;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class FeedCell extends AbstractCell<Feed> {

	FeedMe_web controller;

	public FeedCell(FeedMe_web c) {
		super();
		this.controller = c;
	}

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			Feed value, SafeHtmlBuilder sb) {
		if (value == null) return;

		sb.appendHtmlConstant("<span id=\"feedListEntry\">");
		sb.appendEscaped(value.title);	
		sb.appendHtmlConstant("</span>");
	}
}

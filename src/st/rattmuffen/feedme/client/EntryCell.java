package st.rattmuffen.feedme.client;

import st.rattmuffen.feedme.shared.FeedEntry;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

public class EntryCell extends AbstractCell<FeedEntry> {

	@Override
	public void render(com.google.gwt.cell.client.Cell.Context context,
			FeedEntry value, SafeHtmlBuilder sb) {
		if (value == null) return;

		final FeedEntry fe = value;
		SafeHtml rendered = new SafeHtml() {

			private static final long serialVersionUID = 1L;

			@Override
			public String asString() {
				return "<div id=\"rssEntry\">" + 
						"<div id=\"rssTitle\"><a target=\"_blank\" href=\""+ fe.link +"\">" + fe.title + "</a></div>" + 
						"<div id=\"rssDetails\">" + fe.date.toString() + " - " + fe.author +"</div>" +
						"<div id=\"rssDescription\">" + fe.getDescription() +"</div>" +
						"</div>";
			}
		};
		
		sb.append(rendered);
	}

}

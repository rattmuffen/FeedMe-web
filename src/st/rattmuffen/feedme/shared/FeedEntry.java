package st.rattmuffen.feedme.shared;

import java.io.Serializable;
import java.util.Date;

public class FeedEntry implements Serializable {

	
	private static final long serialVersionUID = 1L;
	
	public String title;

	public Date date;

	public String author;

	public String link;

	public String description;

	public FeedEntry() {}

	public String getDescription() {
		return description==null ? "" : description;
	};
}

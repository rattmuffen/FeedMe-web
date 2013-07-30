package st.rattmuffen.feedme.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Feed implements Serializable{

	private static final long serialVersionUID = 1L;
	
	public String title;
	
	public List<FeedEntry> entries;

	public String description;

	public String language;

	public String image;

	public String link;
	
	public Feed() {
		entries = new ArrayList<FeedEntry>();
	}
	
	public void addEntry(FeedEntry fe) {
		entries.add(fe);
	}

	public String getImage() {
		return image==null ? "" : image;
	};
	
	public String getDescription() {
		return description==null ? title : description;
	}

}

package st.rattmuffen.feedme.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Feed implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public String title;
	
	public List<FeedEntry> entries;

	public String description;

	public String language;

	public String image;

	public String link;

	public String url;
	
	public String category;

	public int unread;
	
	public Feed() {
		entries = new ArrayList<FeedEntry>();
	}
	
	public void addEntry(FeedEntry fe) {
		entries.add(fe);
	}
	
	public void sort() {
		Collections.sort(entries, new Comparator<FeedEntry>() {
			@Override
			public int compare(FeedEntry o1, FeedEntry o2) {
				 return o2.date.compareTo(o1.date);
			}
		});
		
		// Remove duplicates if any
		Set<FeedEntry> setItems = new LinkedHashSet<FeedEntry>(entries);
		entries.clear();
		entries.addAll(setItems);
	}

	public String getImage() {
		return image==null ? "" : image;
	};
	
	public String getDescription() {
		return description==null ? title : description;
	}
}

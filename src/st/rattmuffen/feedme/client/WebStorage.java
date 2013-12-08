package st.rattmuffen.feedme.client;

import java.util.ArrayList;
import java.util.Date;

import st.rattmuffen.feedme.shared.Feed;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.storage.client.Storage;

public class WebStorage {
	
	public static Storage stockStore = null;
	
	public static boolean initiateStorage() {
		stockStore = Storage.getLocalStorageIfSupported();
		return (stockStore != null);
	}
	
	public static ArrayList<String> getAllFeedURLsFromStorage() {
		if (stockStore != null) {
			ArrayList<String> list = new ArrayList<String>();

			for (int i = 0; i < stockStore.getLength(); i++){
				String key = stockStore.key(i);
				
				if (key.startsWith("Feed.URL.")) {
					list.add(stockStore.getItem(key));
				}
			}
			return list;
		}
		return null;
	}
	
	public static void saveFeedToLocalStorage(Feed f) {
		if (stockStore != null) {
			stockStore.setItem("Feed.URL." + f.url, f.url);
		}
	}
	
	public static void removeFeedFromStorage(Feed f) {
		if (stockStore != null) {
			stockStore.removeItem("Feed.URL." + f.url);
			stockStore.removeItem("Feed.Date." + f.url);
			stockStore.removeItem("Feed.Category." + f.url);
		}
	}
	
	public static Date getDateFromStorage(String url) {
		long time = System.currentTimeMillis();
		Date d = new Date(time);
		
		if (stockStore != null) {
			String s = stockStore.getItem("Feed.Date." + url);
			
			System.out.println("Got " + s + " from " + url);
			
			if (s==null)
				return d;
			
			return DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_FULL).parse(s);
		} else {
			return d;
		}
	}
	
	public static void saveDateToStorage(String url) {
		if (stockStore != null) {
			long time = System.currentTimeMillis();
			Date d = new Date(time);

			stockStore.setItem("Feed.Date." + url, DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_FULL).format(d));
			System.out.println("Set " + DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_FULL).format(d) + " to " + url);
		}
	}
	
	public static String getFeedCategoryFromStorage(String url) {
		if (stockStore != null) {
			String s = stockStore.getItem("Feed.Category." + url);
			
			System.out.println("Got " + s + " from " + url);

			return s;
		} else {
			return "";
		}
	}
	
	public static void saveFeedCategoryToStorage(String category, String url) {
		if (stockStore != null) {
			stockStore.setItem("Feed.Category." + url, category);
		}
	}
	
	
	public static String getFeedTitleFromStorage(String url) {
		if (stockStore != null) {
			String s = stockStore.getItem("Feed.Title." + url);
			
			System.out.println("Got " + s + " from " + url);

			return s;
		} else {
			return "";
		}
	}
	
	public static void saveFeedTitleToStorage(String title, String url) {
		if (stockStore != null) {
			stockStore.setItem("Feed.Title." + url, title);
		}
	}
	
	public static boolean getFeedFavoriteFromStorage(String url) {
		if (stockStore != null) {
			String s = stockStore.getItem("Feed.Favorite." + url);
			
			return (s != null && s.equals("true"));
		} else {
			return false;
		}
	}
	
	public static void saveFeedFavoriteToStorage(String url, boolean favorite) {
		if (stockStore != null) {
			String s = "true";
			if (!favorite)
				s = "false";
			
			stockStore.setItem("Feed.Favorite." + url, s);
		}
	}
	
	public static void addCategoryToStorage(String category) {
		if (stockStore != null) {
			stockStore.setItem("Category." + category, category);
		}
	}
	
	public static void removeCategoryFromStorage(String category) {
		if (stockStore != null) {
			stockStore.removeItem("Category." + category);
		}
	}
	
	public static ArrayList<String> getAllCategoriesFromStorage() {
		if (stockStore != null) {
			ArrayList<String> list = new ArrayList<String>();

			for (int i = 0; i < stockStore.getLength(); i++){
				String key = stockStore.key(i);
				
				if (key.startsWith("Category.")) {
					list.add(stockStore.getItem(key));
				}
			}
			return list;
		}
		return null;
	}

	public static void clearStorage() {
		if (stockStore != null) {
			for (int i = 0; i < stockStore.getLength(); i++){
				String key = stockStore.key(i);
				
				if (key.startsWith("Feed."))
					stockStore.removeItem(key);
			}
		}
	}
	
}

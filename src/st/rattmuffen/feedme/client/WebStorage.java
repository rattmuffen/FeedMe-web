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
	
	public static ArrayList<String> getAllFeedsFromStorage() {
		if (stockStore != null) {
			ArrayList<String> list = new ArrayList<String>();

			for (int i = 0; i < stockStore.getLength(); i++){
				String key = stockStore.key(i);
				
				if (key.startsWith("Feed.")) {
					list.add(stockStore.getItem(key));
				}
			}
			return list;
		}
		return null;
	}
	
	public static void saveFeedToLocalStorage(Feed f) {
		if (stockStore != null) {
			stockStore.setItem("Feed." + f.url, f.url);
		}
	}
	
	public static void removeFeedFromStorage(Feed f) {
		if (stockStore != null) {
			stockStore.removeItem("Feed." + f.url);
			stockStore.removeItem("Url.date." + f.url);
		}
	}
	
	public static Date getDateFromStorage(String url) {
		long time = System.currentTimeMillis();
		Date d = new Date(time);
		
		if (stockStore != null) {
			String s = stockStore.getItem("Url.date." + url);
			
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

			stockStore.setItem("Url.date." + url, DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_FULL).format(d));
			System.out.println("Set " + DateTimeFormat.getFormat(PredefinedFormat.DATE_TIME_FULL).format(d) + " to " + url);
		}
	}
}

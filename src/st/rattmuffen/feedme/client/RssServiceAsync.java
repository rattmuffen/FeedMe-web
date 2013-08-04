package st.rattmuffen.feedme.client;

import java.util.Date;

import st.rattmuffen.feedme.shared.Feed;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RssServiceAsync {
	void getFeed(String address,Date d, AsyncCallback<Feed> callback)
			throws IllegalArgumentException;
}

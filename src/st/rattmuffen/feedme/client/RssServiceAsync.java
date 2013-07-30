package st.rattmuffen.feedme.client;

import st.rattmuffen.feedme.shared.Feed;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RssServiceAsync {
	void getFeed(String address, AsyncCallback<Feed> callback)
			throws IllegalArgumentException;
}

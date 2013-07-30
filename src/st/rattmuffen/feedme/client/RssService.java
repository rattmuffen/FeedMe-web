package st.rattmuffen.feedme.client;

import st.rattmuffen.feedme.shared.Feed;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("rss")
public interface RssService extends RemoteService {
	Feed getFeed(String address) throws IllegalArgumentException;
}

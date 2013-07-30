package st.rattmuffen.feedme.server;

import java.io.IOException;
import java.net.URL;

import org.xml.sax.InputSource;

import st.rattmuffen.feedme.client.RssService;
import st.rattmuffen.feedme.shared.Feed;
import st.rattmuffen.feedme.shared.FeedEntry;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.sun.syndication.feed.synd.SyndEntryImpl;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;

@SuppressWarnings("serial")
public class RssServiceImpl extends RemoteServiceServlet implements RssService {

	public Feed getFeed(String address) throws IllegalArgumentException {
		Feed f = new Feed();
		
        try {
    		URL feedSource = new URL(address);
            XmlReader xmlReader = new XmlReader(feedSource);
            InputSource is = new InputSource(xmlReader);
 
            SyndFeedInput input = new SyndFeedInput();
			SyndFeed syndFeed = input.build(is);
			
			f.title = syndFeed.getTitle();
			f.description = syndFeed.getDescription();
			f.language = syndFeed.getLanguage();
			f.link = syndFeed.getLink();
			if (syndFeed.getImage() != null)
				f.image = syndFeed.getImage().getUrl();
			
			for (Object object : syndFeed.getEntries()) {
				SyndEntryImpl syndEntry = (SyndEntryImpl) object;
				
				FeedEntry fe = new FeedEntry();
				fe.title = syndEntry.getTitle();
				fe.date = syndEntry.getPublishedDate();
				fe.author = syndEntry.getAuthor();
				fe.link = syndEntry.getLink();
				if (syndFeed.getDescription() != null)
					fe.description = syndEntry.getDescription().getValue();
				
				f.addEntry(fe);
			}
			
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (FeedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return f;
	}
}

package uk.net.catchpoler.news_alert;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

public class RssReader {
    public String buildAlerts() throws IOException, FeedException {
        URL feedSource = new URL("https://www.scmp.com/rss/2/feed");
        SyndFeedInput input = new SyndFeedInput();
        SyndFeed feed = input.build(new XmlReader(feedSource));
        System.out.println(feed);
        String html = "<p>";

        for (final Iterator iter = feed.getEntries().iterator();
             iter.hasNext(); ) {
            final SyndEntry entry = (SyndEntry) iter.next();
            String title = entry.getTitle();
            if (title.toLowerCase().contains("carbon")
                    && title.toLowerCase().contains("car")) {
                String uri = entry.getUri();
                html += ("<a href=" + uri + ">" + title + "</a>\n");
            }
        }
        html += "</p>";
        System.out.println(html);

        return html;
    }
}

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
    public String buildAlerts(String url, String[] searchTerms) throws IOException, FeedException {
        URL feedSource = new URL(url);
        SyndFeedInput input = new SyndFeedInput();
        try {
            SyndFeed feed = input.build(new XmlReader(feedSource));
            String html = "";
            int hitCount = 0;
            html += ("<strong>" + feed.getTitle() + "<strong>");
            for (final Iterator iter = feed.getEntries().iterator(); iter.hasNext(); ) {
                final SyndEntry entry = (SyndEntry) iter.next();
                String title = entry.getTitle();
                boolean hit = true;
                for (int i = 0; i < searchTerms.length && hit; i++) {
                    if (searchTerms[i].length() > 0) {
                        if (!title.toLowerCase().contains(searchTerms[i])) {
                            hit = false;
                        }
                    }
                }
                if (hit) {
                    String uri = entry.getUri();
                    html += ("<p><a href=" + uri + ">" + title + "</a></p>\n");
                    hitCount++;
                }
            }
            if (hitCount == 0) {
                html += "   - No hits!<br>";
            } else {
                System.out.println("New links added: " );
            }
            return html;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}

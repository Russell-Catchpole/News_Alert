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
    public String buildAlerts(String url, String[] searchTerms, DataProcessor dp, String to) throws IOException, FeedException {
        URL feedSource = new URL(url);
        SyndFeedInput input = new SyndFeedInput();
        try {
            SyndFeed feed = input.build(new XmlReader(feedSource));
            String html = "";
            int hitCount = 0;

            // Read through all the entries in the feed
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
                // If a hit was found, check if it's not already been recorded,
                // then add it to the html & record it
                if (hit) {

                    String uri = entry.getUri();
                    if (!dp.isAlertFound(to, uri)) {
                        // If this is the 1st entry, write a feed heading
                        if (hitCount == 0) {
                            html += ("<strong>" + feed.getTitle() + "<strong>");
                        }
                        html += ("<p><a href=" + uri + ">" + title + "</a></p>\n");
                        if (!dp.writeAlert(to, uri)) {
                            return "";  // Failed :-(
                        }
                        hitCount++;
                    }
                }
            }
            if (hitCount > 0) {
                System.out.println("New links added: " );
            }
            return html;
        } catch (Exception e) {
//            e.printStackTrace();
            return "";
        }
    }
}

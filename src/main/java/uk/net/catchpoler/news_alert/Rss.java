package uk.net.catchpoler.news_alert;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;

import com.rometools.rome.io.FeedException;

public class Rss {
    public static void main(String[] args) throws IOException, FeedException, SQLException, ClassNotFoundException, InterruptedException {
        String[] feeds = new String[]{
                "http://rss.cnn.com/rss/cnn_topstories.rss",
                "https://www.scmp.com/rss/2/feed",
                "https://rss.nytimes.com/services/xml/rss/nyt/Travel.xml",
                "https://www.huffpost.com/section/front-page/feed?x=1",
                "http://www.foxnews.com/about/rss/",
                "https://cdn.feedcontrol.net/8/1114-wioSIX3uu8MEj.xml",
                "https://www.yahoo.com/news/rss",
//                "view-source:https://www.reutersagency.com/feed/", odd format
                "https://feeds.feedburner.com/breakingtravelnews",
                "https://www.feedspot.com/infiniterss.php?_src=followbtn&followfeedid=5245042&q=site:",
                "https://www.news.gov.hk/en/common/html/topstories.rss.xml"
        };
        String anAt = "@";
        String dom = "gmail";
        String user = "russelljcatchpole" + anAt + dom + ".com"; // deflect spam from github!

        // Connect to news-alert database
        DataProcessor dp = new DataProcessor();

        while (true) {
            if (!dp.connect()) {
                return;
            }

            RssReader rssReader = new RssReader();
            String searchTerms[] = {"hong kong", "flights"};
            String html = "";

            for (int x = 0; x < feeds.length; x++) {
                html += rssReader.buildAlerts(feeds[x], searchTerms, dp, user);
            }
            if (html.length() > 0) {
                Mailer mailer = new Mailer();
                String messageBody = "<heading 1>Search terms:" + Arrays.toString(searchTerms) + "<heading 1><br><br>"
                        + html;
                mailer.sendEmail(user, messageBody);
            } else {
                System.out.println("No new hits found");
            }
            dp.close();
            System.out.println("Cycle finished. Sleeping zzzzzzz");
            Thread.sleep(180000);
        }
    }
}
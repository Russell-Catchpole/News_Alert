package uk.net.catchpoler.news_alert;

import java.io.IOException;

import com.rometools.rome.io.FeedException;

public class Rss {

	public static void main(String[] args) throws IOException, FeedException {
		RssReader rssReader = new RssReader();
		String html = rssReader.buildAlerts();

		Mailer mailer = new Mailer();
		mailer.sendEmail(html);
	}
}
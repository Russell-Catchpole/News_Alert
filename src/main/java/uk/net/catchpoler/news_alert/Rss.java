package uk.net.catchpoler.news_alert;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

public class Rss {

	public static void main(String[] args) throws IOException, FeedException {

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

		// Now create and send an email
		String to = "rcatchpo@yahoo.co.uk";
		String from = "russelljcatchpole@googlemail.com";
		final String username = "russelljcatchpole@googlemail.com";
		final String password = "";
		String host = "smtp.gmail.com";

		// Get system properties
		Properties properties = System.getProperties();

		// Setup mail server
		properties.setProperty("mail.smtp.host", host);
		properties.setProperty("mail.smtp.port", "465");
		properties.setProperty("mail.smtp.starttls.enable", "true");
		properties.setProperty("mail.smtp.auth", "true");
		properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		properties.setProperty("mail.smtp.socketFactory.fallback", "false");
		// Get the default Session object.
		Session session = Session.getInstance(properties,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});

		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);

			// Set From: header field of the header.
			message.setFrom(new InternetAddress(from));

			// Set To: header field of the header.
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// Set Subject: header field
			message.setSubject("News Alert!");

			message.setContent(html, "text/html");

			// Now set the actual message
//			message.setText(html);

			// Send message
			Transport.send(message);
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
		}
	}

	}




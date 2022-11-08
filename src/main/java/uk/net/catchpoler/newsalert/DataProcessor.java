package uk.net.catchpoler.newsalert;

import com.amazonaws.regions.Regions;
import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.ItemCollection;
import com.amazonaws.services.dynamodbv2.document.ScanOutcome;
import com.amazonaws.services.dynamodbv2.document.Table;

public class DataProcessor {
    private Connection con = null;
    private DynamoDB dynamoDB = null;
    private Statement st = null;
    private PreparedStatement psAlert = null;
    private ResultSet rsAlert = null;

    public boolean connect() {
        try {
            Dotenv dotenv = Dotenv.load();
            String newsAlertPw = dotenv.get("NEWS_ALERT_PW");
            con = DriverManager
                    .getConnection("jdbc:mysql://localhost/news-alert?"
                            + "user=root&password=" + newsAlertPw);
            if (con == null) {
                System.out.println("Connection to news-alert database failed!");
                return false;
            }
            System.out.println("Successfully connected to news-alert database!");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean getDynamoDBClient() {
        try {
            AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().withRegion(Regions.EU_WEST_2).build();
            this.dynamoDB = new DynamoDB(client);
            System.out.println("AmazonDynamoDB Client created!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isAlertFound(String user_id, String uri) throws SQLException {
        psAlert = con.prepareStatement("select 1 from alert where user_id = ? and uri = ?");
        psAlert.setString(1, user_id);
        psAlert.setString(2, uri);
        rsAlert = psAlert.executeQuery();
        return rsAlert.next();
    }

    public ResultSet rtvUsers() throws SQLException {
        ResultSet rsUser = null;
        try {
            PreparedStatement psUser = con.prepareStatement("select * from user");
            rsUser = psUser.executeQuery();
            return rsUser;
        } catch (SQLException s) {
            System.out.println(s.getMessage());
            return null;
        }
    }

    //    public ResultSet rtvFeeds() throws SQLException {
//        ResultSet rsFeed = null;
//        try {
//            PreparedStatement psFeed = con.prepareStatement("select uri from feed");
//            rsFeed = psFeed.executeQuery();
//            return rsFeed;
//        } catch (SQLException s) {
//            System.out.println(s.getMessage());
//            return null;
//        }
//    }
    public ResultSet rtvFeeds() throws SQLException {
        ResultSet rsFeed = null;
        try {
            PreparedStatement psFeed = con.prepareStatement("select uri from feed");
            rsFeed = psFeed.executeQuery();
            return rsFeed;
        } catch (SQLException s) {
            System.out.println(s.getMessage());
            return null;
        }
    }

    public boolean writeAlert(String user_id, String uri) throws SQLException {
        try {
            psAlert = con.prepareStatement("INSERT INTO alert (user_id, uri) VALUES (?, ?)");
            psAlert.setString(1, user_id);
            psAlert.setString(2, uri);
            if (!psAlert.execute()) {
                System.out.println("Record added: " + user_id + ", " + uri);
                return true;
            } else {
                System.out.println("Failed to record alert" + user_id + ", " + uri);
                return false;
            }

        } catch (SQLException s) {
            System.out.println(s.getMessage());
            return false;
        }
    }

    public void close() {
        try {
            if (rsAlert != null) {
                rsAlert.close();
            }

            if (st != null) {
                st.close();
            }

            if (con != null) {
                con.close();
            }
        } catch (Exception e) {

        }
    }
}



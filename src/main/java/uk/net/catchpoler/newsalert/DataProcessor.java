package uk.net.catchpoler.newsalert;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

public class DataProcessor {
    private Connection con = null;
    private Statement st = null;
    private PreparedStatement psAlert = null;
    private ResultSet rsAlert = null;

    public boolean connect() {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .filename("/.env_azure")
                    .load();
            String newsAlertPw = dotenv.get("NEWS_ALERT_PW");
            con = DriverManager
                    .getConnection("jdbc:mysql://mysql-training-db.mysql.database.azure.com:3306/news-alert?"
                            + "user=naadmin&password=" + newsAlertPw);

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

//            writeResultSet(resultSet);

// PreparedStatements can use variables and are more efficient
//            preparedStatement = connect
//                    .prepareStatement("insert into  news-alert.alert values (default, ?, ?, ?, ? , ?, ?)");
// "myuser, webpage, datum, summary, COMMENTS from feedback.comments");
// Parameters start with 1
//            ps.setString(1, "Test");
//            ps.setString(2, "TestEmail");
//            ps.setString(3, "TestWebpage");
//            ps.setDate(4, new java.sql.Date(2009, 12, 11));
//            ps.setString(5, "TestSummary");
//            ps.setString(6, "TestComment");
//            ps.executeUpdate();
//
//            ps = con
//                    .prepareStatement("SELECT myuser, webpage, datum, summary, COMMENTS from feedback.comments");
//            rs = ps.executeQuery();
//            writeResultSet(resultSet);

// Remove again the insert comment
//            ps = con
//                    .prepareStatement("delete from feedback.comments where myuser= ? ; ");
//            ps.setString(1, "Test");
//            ps.executeUpdate();
//
//            rs = st
//                    .executeQuery("select * from feedback.comments");
////            writeMetaData(resultSet);

//        } catch (Exception e) {
//            throw e;
//        } finally {
//            close();
//        }



package uk.net.catchpoler.newsalert;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.*;

public class DataProcessor {
    private Connection con = null;
    private Statement st = null;
    private PreparedStatement ps = null;
    private ResultSet rs = null;

    public boolean connect() throws SQLException {
        try {
            Dotenv dotenv = Dotenv.load();
            String newsAlertPw = dotenv.get("NEWS_ALERT_PW");
            ;
            con = DriverManager
                    .getConnection("jdbc:mysql://localhost/news-alert?"
                            + "user=root&password=" + newsAlertPw);
            if (con == null) {
                System.out.println("Connection to news-alert database failed!");
                return false;
            }
            System.out.println("Successfully connected to news-alert database!");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isAlertFound(String user_id, String uri) throws SQLException {
        ps = con.prepareStatement("select 1 from alert where user_id = ? and uri = ?");
        ps.setString(1, user_id);
        ps.setString(2, uri);
        rs = ps.executeQuery();
       return rs.next();
    }

    public boolean writeAlert(String user_id, String uri) throws SQLException {
        try {
            ps = con.prepareStatement("INSERT INTO alert (user_id, uri) VALUES (?, ?);");
            ps.setString(1, user_id);
            ps.setString(2, uri);
            if (!ps.execute()) {
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
            if (rs != null) {
                rs.close();
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



import java.sql.*;

public class Controller extends DBConn {
    public void printResultat(String threadID, String courseID) {
        try {
            Statement stmt = conn.createStatement();
            String query = "select * from PostInThread where ThreadID='" + threadID + "' and CourseID='" + courseID
                    + "'";
            // System.out.println(query);

            ResultSet rs = stmt.executeQuery(query);
            int nr = 1;
            System.out.println("Resultatliste for thread: " + threadID);
            while (rs.next()) {
                System.out.println(" " + nr++ + " " + rs.getInt("PostID") + " " + rs.getInt("ThreadID") + " "
                        + rs.getInt("CourseID"));
            }

        } catch (Exception e) {
            System.out.println("db error during select of loper = " + e);
        }

    }
}

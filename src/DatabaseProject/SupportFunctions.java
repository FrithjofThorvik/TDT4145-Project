package DatabaseProject;

import java.sql.*;

//This class contain functions that are used in DBController
//This was done to make DBController more clear and concise
public class SupportFunctions extends DBConn {
    // Border is used to make things look nice when printed to console
    private String border = "===================================================================================\n";

    public SupportFunctions() {
        this.connect();
    }

    // Prints a query made to the connected database
    public String handleQuery(String query) {
        try {
            Statement stmt = conn.createStatement();

            ResultSet rs = stmt.executeQuery(query);

            // Return a formatted version of the query request
            return this.handleResultSet(rs);

        } catch (Exception e) {
            return "Error (handleQuery()): " + e;
        }
    }

    // Prints a formatted version of a MySQL ResultSet
    public String handleResultSet(ResultSet rs) {
        try {
            String columns = "";
            String result = "";
            ResultSetMetaData rsmd = rs.getMetaData(); // Prepare meta data from result set
            Boolean firstRow = true;

            // Loop through resultSet
            while (rs.next()) {
                // Loop through the current resultSet (row)
                for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                    if (firstRow) { // Print column names if first run in loop
                        columns += rsmd.getColumnName(i) + "\t\t";
                    }

                    // Print column values in rows
                    result += rs.getString(i) + "\t\t";

                }
                firstRow = false;

                // Break line for new row
                result += "\n";
            }

            return this.border + columns + "\n" + result + border;

        } catch (Exception e) {
            System.out.println("Error (handleResultSet()): " + e);
            return "";
        }
    }

    // Get threadId from postID
    public Integer getThreadId(String postId) {
        Integer threadId = 0;
        String query = "SELECT ThreadID FROM PostInThread WHERE PostID = " + postId + ";";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Check if row exists, and return boolean value
            if (rs.next()) {
                threadId = Integer.parseInt(rs.getString(1));
            }

            // Return validation
            if (threadId > 0) {
                return threadId;
            }
            return 0;

        } catch (Exception e) {
            System.out.println("Error (getThreadId()): " + e);
            return -1;
        }
    }

    // Validate postId
    public Integer validatePostId(String postId) {
        Boolean result = false;
        String query = "SELECT EXISTS(SELECT * FROM Post WHERE PostID='" + postId + "') AS PostExists";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Check if row exists, and return boolean value
            if (rs.next()) {
                result = rs.getBoolean(1);
            }

            // Return validation
            if (result) {
                return 1;
            }
            return 0;
        } catch (Exception e) {
            System.out.println("Error (handleUserLogin()): " + e);
            return -1;
        }
    }

    // Get post count -> this is used to automatically assign a new postID to new
    // posts
    public Integer getNextRow(String id, String table) {
        Integer nextPostId = 0;
        String query = "SELECT " + id + " FROM " + table + " ORDER BY " + id + " DESC;";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Check if row exists, and return boolean value
            if (rs.next()) {
                nextPostId = Integer.parseInt(rs.getString(1));
            }

            // Return validation
            if (nextPostId > 0) {
                return nextPostId + 1;
            }
            return 0;

        } catch (Exception e) {
            System.out.println("Error (getNextRow()): " + e);
            return -1;
        }
    }

    // Get folderID -> needed since we insert a post in a folder using the folderID
    public Integer getFolderID(String folderName) {
        Integer folderID = 0;
        String query = "SELECT folderID FROM Folder WHERE Name='" + folderName + "'";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                folderID = rs.getInt("folderID");
            }
            // Return folderID
            if (folderID > 0) {
                return folderID;
            }
            return 0;
        } catch (Exception e) {
            System.out.println("Error (getFolderID()): " + e);
            return -1;
        }
    }

    // Get tagID -> needed since we insert a post with a tag using the tagID
    public Integer getTagID(String tagName) {
        Integer tagID = 0;
        String query = "SELECT tagID FROM Tag WHERE Name='" + tagName + "'";

        try {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                tagID = rs.getInt("tagID");
            }
            // Return tagID
            if (tagID > 0) {
                return tagID;
            }
            return 0;
        } catch (Exception e) {
            System.out.println("Error (getTagID()): " + e);
            return -1;
        }
    }
}

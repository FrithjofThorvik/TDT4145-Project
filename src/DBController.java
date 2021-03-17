package src;

import java.sql.*;

public class DBController extends DBConn {
    private String border = "===================================================================================\n";

    // Constructor for establishing connection to database
    public DBController() {
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
    private String handleResultSet(ResultSet rs) {
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

    // Handles user login
    public Integer handleUserLogin(String email, String password) {
        try {
            Boolean result = false;
            String query = "SELECT EXISTS(SELECT * FROM user WHERE Email='" + email + "' AND Password='" + password
                    + "') AS Auth";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Check if row exists, and return boolean value
            if (rs.next()) {
                result = rs.getBoolean(1);
            }

            if (result) {
                return 1;
            }
            return 0;

        } catch (Exception e) {
            System.out.println("Error (handleUserLogin()): " + e);
            return -1;
        }
    }

    // Handle seraching for Posts
    public Integer handlePostSearch(String text) {
        try {
            String result = "";
            String query = "SELECT PostID FROM Post WHERE Text LIKE '%" + text + "%';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Check if row exists, and return boolean value
            result = this.handleResultSet(rs);
            System.out.println(result);

            // Return success if no errors
            return 1;

        } catch (Exception e) {
            System.out.println("Error (handleUserLogin()): " + e);
            return -1;
        }
    }

    // Handle replying to Posts
    public Integer handlePostReply(String postId, String postText) {
        try {
            // Set preset Ids
            Integer courseId = 4145;
            Integer userId = 2;

            // Fetch respective Ids
            Integer nextPostId = this.getNextRow("PostID", "Post");
            Integer validPostId = this.validatePostId(postId);
            Integer threadId = this.getThreadId(postId);

            // Generate respective queries for necessary tables
            String insertPost = "INSERT INTO Post VALUES(" + nextPostId + ", " + courseId + ", " + userId + ", '"
                    + postText + "');";
            String insertPostInThread = "INSERT INTO PostInThread VALUES(" + nextPostId + ", " + threadId + ", "
                    + courseId + ");";
            String queryPostInThread = "SELECT ThreadID, PostID, Text FROM (Post JOIN PostInThread using (PostID)) WHERE ThreadID = "
                    + threadId + ";";

            // Validate postId and nextPostId
            if (validPostId == 1 && nextPostId > 0) {
                try {
                    Statement stmt = conn.createStatement();

                    // Insert queries
                    stmt.executeUpdate(insertPost);
                    stmt.executeUpdate(insertPostInThread);

                    // Print results of posts in thread
                    System.out.println(this.handleQuery(queryPostInThread));

                    // Return successfull run value
                    return 1;

                } catch (Exception e) {
                    System.out.println("Error (handlePostReply()): " + e);
                    return -1;
                }
            } else {
                System.out.println("Invalid postID...");
                return 1;
            }

        } catch (Exception e) {
            System.out.println("Error (handleUserLogin()): " + e);
            return -1;
        }
    }

    // Get post count
    private Integer getNextRow(String id, String table) {
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

    // Get threadId from postID
    private Integer getThreadId(String postId) {
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
    private Integer validatePostId(String postId) {
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
}

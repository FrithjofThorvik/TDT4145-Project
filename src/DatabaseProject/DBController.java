package DatabaseProject;

import java.sql.*;

//This class is responsible for connecting to the database (by inheriting from DBConn), 
//it is also responsible for quering the database with the relevant user input accuired from the Question controller. 
public class DBController extends DBConn {
    SupportFunctions SprtFunc = new SupportFunctions(); // Call functions from this class to make the DBController less
                                                        // convoluted
    // Constructor for establishing connection to database

    public DBController() {
        this.connect();
    }

    // Handles user login (USECASE 1)
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

    // Handle making a post (USECASE 2)
    public Integer handleMakePost(String postText, String folder, String tag) {
        try {
            // Set preset Ids
            Integer courseId = 4145;
            Integer userId = 2;

            // Fetch respective Ids
            Integer nextPostId = SprtFunc.getNextRow("PostID", "Post");
            Integer folderID = SprtFunc.getFolderID(folder);
            Integer tagID = SprtFunc.getTagID(tag);

            // Set the threadID to be the same as the postID
            Integer threadId = nextPostId;

            // Create post query strings
            String insertPost = "INSERT INTO Post VALUES(" + nextPostId + ", " + courseId + ", " + userId + ", '"
                    + postText + "');";
            String insertThread = "INSERT INTO Thread VALUES(" + threadId + ", " + courseId + ");";
            String insertPostInThread = "INSERT INTO PostInThread VALUES(" + nextPostId + ", " + threadId + ", "
                    + courseId + ");";

            // Query string to make the post belong to the inputted folder
            String insertThreadFolder = "INSERT INTO ThreadFolder VALUES(" + folderID + ", " + threadId + ", "
                    + courseId + ");";

            // Query string to make the post have the inputted tag
            String insertPostTag = "INSERT INTO PostTag VALUES(" + nextPostId + ", " + courseId + ", " + tagID + ");";

            // Query string to fetch the newly created post -> too see that it was created
            // succesfully
            String queryPostInThread = "SELECT ThreadID, Post.TEXT from (Thread join ThreadFolder using (ThreadID, CourseID)) join Post on (Post.PostID=Thread.ThreadID) where ThreadID in(select ThreadID from ThreadFolder join Folder using (FolderID, CourseID) where Name='"
                    + folder
                    + "') AND ThreadID in (select PostID as ThreadID from PostTag natural join Tag where Name='" + tag
                    + "');";

            // Validate nextPostId
            if (nextPostId > 0) {
                try {
                    Statement stmt = conn.createStatement();

                    // Insert queries
                    stmt.executeUpdate(insertPost);
                    stmt.executeUpdate(insertThread);
                    stmt.executeUpdate(insertPostInThread);

                    // Check if the user inputted value for folder and tag exists
                    if (folderID > 0 && tagID > 0) {
                        // Execute the insert queries to make the post within the user inputted folder
                        // and tag
                        stmt.executeUpdate(insertThreadFolder);
                        stmt.executeUpdate(insertPostTag);

                        // Print results of posts in thread -> To check that everything was executed
                        // correctly
                        System.out
                                .println("\nThese are the ThreadIDs of all post matching the choosen folder and tag:");
                        System.out.println(SprtFunc.handleQuery(queryPostInThread));
                    } else {
                        System.out.println("Folder or Tag does not exist");
                    }

                    // Return successfull run value
                    return 1;

                } catch (Exception e) {
                    System.out.println("Error (handleMakePost()): " + e);
                    return -1;
                }
            } else {
                System.out.println("Invalid postID...");
                return 1;
            }

        } catch (Exception e) {
            System.out.println("Error (handleMakePost()): " + e);
            return -1;
        }
    }

    // Handle replying to Posts (USECASE 3)
    public Integer handlePostReply(String postId, String postText) {
        try {
            // Set preset Ids
            Integer courseId = 4145;
            Integer userId = 2;

            // Fetch respective Ids
            Integer nextPostId = SprtFunc.getNextRow("PostID", "Post");
            Integer validPostId = SprtFunc.validatePostId(postId);
            Integer threadId = SprtFunc.getThreadId(postId);

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

                    // Execute the insert queries
                    stmt.executeUpdate(insertPost);
                    stmt.executeUpdate(insertPostInThread);

                    // Print results of posts in thread (so you can see that the reply was created
                    // in the database)
                    System.out.println(SprtFunc.handleQuery(queryPostInThread));

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

    // Handle seraching for Posts (USECASE 4)
    public Integer handlePostSearch(String text) {
        try {
            String result = "";
            String query = "SELECT PostID FROM Post WHERE Text LIKE '%" + text + "%';";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            // Check if row exists, and return boolean value
            result = SprtFunc.handleResultSet(rs);
            System.out.println(result);

            // Return success if no errors
            return 1;

        } catch (Exception e) {
            System.out.println("Error (handleUserLogin()): " + e);
            return -1;
        }
    }

    // Handle getting statistics (USECASE 5)
    public Integer handleGetStatistics() {
        try {

            // Query string to fetch the newly created post
            String queryStatistics = "SELECT Email, sum(NumberRead) as NumberRead , sum(NumberCreated) as NumberCreated from( SELECT Email, NumberRead , NumberCreated from( (Select Email, count(PostViewer.PostID) as NumberRead, NULL as NumberCreated from User left outer join PostViewer using (UserID) group by  User.UserID) UNION (Select Email, NULL, count(Post.PostID) as NumberCreated from User left outer join Post using (UserID) group by User.UserID) )as T1 )as T2 group by Email order by NumberRead DESC;";
            try {
                // Print results of statistics
                System.out.println(SprtFunc.handleQuery(queryStatistics));

                // Return successfull run value
                return 1;

            } catch (Exception e) {
                System.out.println("Error (handleGetStatistics(): " + e);
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Error (handleGetStatistics(): " + e);
            return -1;
        }
    }
}

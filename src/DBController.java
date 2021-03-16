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
}

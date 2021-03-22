package DatabaseProject;

import java.sql.*;
import java.util.Properties;

//This an abstract class that handles connection with the database
public abstract class DBConn {
    protected Connection conn;

    final private String host = "//127.0.0.1:3306"; // Set hostname for your MySQL Workbench connection
    final private String username = "root"; // Set username for your MySQL Workbench connection
    final private String password = "root"; // Set password for your MySQL Workbench connection
    final private String schema = "db1"; // Change if your MySQL schema has a different name

    public DBConn() {
    }

    public void connect() {
        try {
            // This will load the MySQL driver, each DB has its own driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Setting properties for the database connection
            Properties p = new Properties();
            p.put("user", username);
            p.put("password", password);

            // Connecting to the database, with properties (p)
            conn = DriverManager.getConnection("jdbc:mysql:" + host + "/" + schema
                    + "?allowPublicKeyRetrieval=true&autoReconnect=true&useSSL=false", p);

        } catch (Exception e) {
            throw new RuntimeException("Unable to connect", e);
        }
    }
}

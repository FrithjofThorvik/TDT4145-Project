import java.sql.*;
import java.util.Properties;

public abstract class DBConn {
    protected Connection conn;

    public DBConn() {
    }

    public void connect() {
        try {
            // Class.forName("com.mysql.jdbc.Driver");
            Class.forName("com.mysql.jdbc.Driver");

            Properties p = new Properties();
            p.put("user", "root");
            p.put("password", "root");
            conn = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/db2?allowPublicKeyRetrieval=true&autoReconnect=true&useSSL=false", p);
        } catch (Exception e) {
            throw new RuntimeException("Unable to connect", e);
        }
    }
}

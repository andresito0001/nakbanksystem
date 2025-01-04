package main.java.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionPool {
    private ConnectionPool() {}

    public static final Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                String url = System.getenv("NAKBANK_DB_URL");
                String user = System.getenv("NAKBANK_DB_USERNAME");
                String password = System.getenv("NAKBANK_DB_PASSWORD");
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return connection;
    }

    private static Connection connection = null;
}

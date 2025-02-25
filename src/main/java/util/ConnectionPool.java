package main.java.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ConnectionPool {
    private static final Integer MAX_POOL_SIZE = 20;
    private static final List<Connection> availableConnections  = new ArrayList<>();
    private static final List<Connection> usedConnections = new ArrayList<>();

        
    static {
        try {
            initializePool();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void initializePool() throws SQLException {
        try {
            String url = System.getenv("NAKBANK_DB_URL");
            String user = System.getenv("NAKBANK_DB_USERNAME");
            String password = System.getenv("NAKBANK_DB_PASSWORD");

            for (int i = 0; i < MAX_POOL_SIZE; i++) {
                Connection conn = DriverManager.getConnection(url, user, password);
                availableConnections.add(conn);
            }

        } catch (SQLException e) {
            throw new SQLException("Error al inicializar el pool");
        }
    }

    public static synchronized Connection getConnection() throws SQLException {
        if (availableConnections.isEmpty()) {
            throw new SQLException("No hay conexiones disponibles...");
        }

        Connection conn = availableConnections.remove(0);
        usedConnections.add(conn);

        return conn;
    }

    public static synchronized void releaseConnection(Connection conn) {
        if (conn != null && usedConnections.remove(conn)) {
            availableConnections.add(conn);
        }
    }

    private static boolean isValid(final Connection conn) {
        try {
            return conn != null && !conn.isClosed() && conn.isValid(2); // Timeout de 2 segundos
        } catch (SQLException e) {
            return false;
        }
    }
}
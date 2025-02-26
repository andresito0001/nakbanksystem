package main.java.util;

import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionPool {
    private static HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    static {
        config.setJdbcUrl(System.getenv("NAKBANK_DB_URL"));
        config.setUsername(System.getenv("NAKBANK_DB_USERNAME"));
        config.setPassword(System.getenv("NAKBANK_DB_PASSWORD"));
        config.addDataSourceProperty( "cachePrepStmts" , "true" );
        config.addDataSourceProperty( "prepStmtCacheSize" , "250" );
        config.addDataSourceProperty( "prepStmtCacheSqlLimit" , "2048" );
        config.setLeakDetectionThreshold(5000);
        config.setIdleTimeout(60000);
        
        ds = new HikariDataSource(config);
    }
    
    private ConnectionPool() {}
    
    public static Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    
}
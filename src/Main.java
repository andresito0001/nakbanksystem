import java.sql.*;

public class Main {
    public static void main (String args[]) {
        try {
            String url = "URL HERE";
            String user = "USER HERE";
            String pass = "PASS HERE";
            
            Connection conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Done!");
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getCause());
        }
    }
}

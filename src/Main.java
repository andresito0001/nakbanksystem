import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import main.test.java.transactionsAppCLI;

// number of id's 1210000000000000000000000

public class Main {
    public static void main (String args[]) {
        try { 
            /* 
            Scanner sc = new Scanner(System.in);
            String url, user, pass;
            System.out.print("Welcome to NakBank System!\nEnter url: ");
            url = sc.nextLine();
            System.out.print("Enter username: ");
            user = sc.nextLine();
            System.out.print("Enter password: ");
            pass = sc.nextLine();
           */
            
            final Connection conn = DriverManager.getConnection("", "", "");

            transactionsAppCLI app = new transactionsAppCLI(conn);
            app.executeApp();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
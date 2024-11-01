import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main (String args[]) {
        try {
            Scanner sc = new Scanner(System.in);
            String url, user, pass;
            System.out.print("Welcome to NakBank System!\nEnter url: ");
            url = sc.nextLine();
            System.out.print("Enter username: ");
            user = sc.nextLine();
            System.out.print("Enter password: ");
            pass = sc.nextLine();
           
            Connection conn = DriverManager.getConnection(url, user, pass);
            System.out.println("Done!");
            
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getCause());
        }
    }
}

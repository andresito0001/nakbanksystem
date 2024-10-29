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
            
            Statement st = conn.createStatement();

            ResultSet rs = st.executeQuery("select * from cliente");
            
            while (rs.next()) {
                System.out.print("Cedula: ");
                System.out.println(rs.getString("cedula") + " Nombre: " + rs.getString("nombre") + " Apellido: " + rs.getString("apellido") + " Alias: " + rs.getString("alias"));
            }
            
            rs.close();
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getCause());
        }
    }
}

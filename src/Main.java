import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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
            
            final Connection conn = DriverManager.getConnection();
            conn.close();
            
            System.out.println("Done!");


            
    //   //  tabla simutrans
    //        List<String> colums = List.of (
    //         "referencia",
    //             "cedula_cliente",
    //             "admin",
    //             "fecha",
    //             "hora",
    //             "tipo",
    //             "cantidad_recibida",
    //             "moneda_recibida",
    //             "metodo_recibido",
    //             "cantidad_enviada",
    //             "moneda_enviada",
    //             "metodo_enviado",
    //             "status",
    //             "tasa",
    //             "ganancia",
    //             "ref_bancaria"
    //        );

    //        List<String> types = List.of(
    //             "serial primary key",
    //             "text references clienteS(cedula)",
    //             "text references administradores(nombre_usuario)",
    //             "date default current_date",
    //             "TIME WITH TIME ZONE DEFAULT timezone('America/Caracas', CURRENT_TIME)",
    //             "text",
    //             "double precision",
    //             "text",
    //             "text references bancos(codigo)",
    //             "double precision",
    //             "text",
    //             "text references bancos(codigo)",
    //             "text",
    //             "double precision",
    //             "double precision",
    //             "text not null"
    //        );

        // tabla simuinv
            // List<String> colums = List.of (
            //     "referencia",
            //     "fecha",
            //      "hora",
            //     "tipo_movimiento",
            //     "cantidad",
            //     "moneda",
            //     "metodo",
            //     "tipo"
            // );

            // List<String> types = List.of (
            //     "serial primary key",
            //     "date",
            //     "time with time zone",
            //     "text",
            //     "double precision",
            //     "text",
            //     "text",
            //     "text"
            // );
            
            // // BANCOS
            // List<String> colums = List.of (
            //     "id_cuenta",
            //     "codigo",
            //     "nombre_banco",
            //     "numero_cuenta",
            //     "moneda",
            //     "saldo_actual"
            // );

            // List<String> types = List.of (
            //     "serial primary key",
            //     "text unique not null",
            //     "text",
            //     "text",
            //     "text",
            //     "double precision"
            // );

            // TABLA ADMINISTRADORES 
            // List<String> colums = List.of(
            //     "id_administrador",
            //     "nombre_usuario",
            //     "password",
            //     "estado"
            // );

            // List<String> types = List.of(
            //     "serial primary key",
            //     "text unique not null",
            //     "text not null",
            //     "text"
            // );



            // TABLA CLIENTES
            // List<String> colums = List.of(
            //     "id_cliente",
            //     "cedula",
            //     "nombre",
            //     "apellido",
            //     "alias"
            // );
            
            // List<String> types = List.of(
            //     "serial",
            //     "text primary key",
            //     "text",
            //     "text",
            //     "text"
            // );


            

           // crea una nueva tabla
            // Transacciones.createTable(conn, "simutrans", colums, types);

            // elimina una tabla
        //    String query = "drop table simutrans;";

        //     try (final PreparedStatement st = conn.prepareStatement(query)) {
        //         st.executeUpdate();
        //         st.close();
        //     }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}

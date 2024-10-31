package queries;
import java.sql.*;
import java.util.Scanner;


public class Reportes {
    
    public void reporteFlujoLunes () {
        try {
            Scanner sc = new Scanner(System.in);
            Connection conn = DriverManager.getConnection("jdbc:postgresql://autorack.proxy.rlwy.net:18835/railway", "postgres", "GIkVDzIIaAUzmJLTFrTMujbkkuyMkhKW");
            System.out.println("Conexion establecida");

            System.out.println ("Ingrese la fecha de consulta: ");
            String fecha = " '"+sc.nextLine()+"'";
            System.out.println("Cuantos lunes atrás de esa fecha quieres ver? ");
            int cantidad = sc.nextInt();

            String subConsulta1 = "select sum(cantidad_recibida) from transaccion where tipo = 'compra' and moneda_recibida = 'usdt' "+
            "and extract(dow from fecha) = 1 and fecha >= "+ fecha + "::DATE - interval '" + (cantidad) + " weeks'";
            String subConsulta2 = "select sum(cantidad_enviada) from transaccion where tipo = 'venta' and moneda_enviada = 'usdt' "+
            "and extract(dow from fecha) = 1 and fecha >= "+ fecha + "::DATE - interval '" + (cantidad) + " weeks'";
            String subConsulta3 = "select sum(cantidad_recibida) from transaccion where tipo = 'compra' and moneda_recibida = 'usd' or moneda_recibida = 'usdt' "+
            "and extract(dow from fecha) = 1 and fecha >= "+ fecha + "::DATE - interval '" + (cantidad) + " weeks'";
            //System.out.println(consulta1);

            //String consulta2= consulta1 + " or fecha = (" + fecha + "::DATE - interval '" + (cantidad * 7) + " days') ";
            String consultaFinal = "select (" + subConsulta1 + ") as monto_posicion_usdt, (" + subConsulta2 + ") as total_ventas_usdt, (" + subConsulta3 + ") as total_compras_usd";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(consultaFinal);
            //System.out.println(consulta2);

            while(rs.next()) {
                System.out.println(rs.getMetaData().getColumnLabel(1)+ ": " + rs.getString(1) + " | " + 
                rs.getMetaData().getColumnLabel(2)+ ": " + rs.getString(2) + " | "+ 
                rs.getMetaData().getColumnLabel(3)+ ": " + rs.getString(3));
            }

            rs.close();
            st.close();

            conn.close();
            System.out.println("Conexion cerrada");


        } catch (SQLException e)
        {
            System.out.println("Eror de conexion");
            System.out.println(e.getCause());
        }
    }
    
}

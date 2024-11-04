package queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.ResultSet;

public class Clientes {
    public static void fillClients(final Connection conn) {
        try {
            final Statement st = conn.createStatement();
            final String query = "select * from cliente";
            final ResultSet rs = st.executeQuery(query);
            
            while (rs.next()) {
                String ci = rs.getString("cedula");
                String alias = rs.getString("alias");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");

                System.err.println (
                    "Cedula: " + ci + 
                    ", alias: " + alias 
                    + ", nombre: " + nombre 
                    + ", apellido: " + apellido
                );
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("[ERROR]: " + e.getSQLState());
        }
    }

    public static boolean existClient(final Connection conn, final String cedula) {
        try {
            final String query = "SELECT COUNT(*) FROM cliente WHERE cedula = ?";
            final PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, cedula);

            ResultSet rs = st.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            
            rs.close();
            st.close();

            return count > 0;
        } catch (SQLException e) {
            System.out.println("[ERROR]: " + e.getSQLState());
            return false;
        }
    }

    public static void fillByAlias(final Connection conn, final String alias) {
        try {
            final String query = "select * from cliente where alias = ?";
            final PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, alias);
            final ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String ci = rs.getString("cedula");
                String aliasQuery =  rs.getString("alias");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");

                System.out.println (
                    "alias: " + aliasQuery + 
                    ", Nombre: " + nombre + 
                    ", Apellido: " + apellido + 
                    ", Cedula: " + ci
                );
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("[ERROR]: " + e.getSQLState());
        }
    }

    public void reporteTopCliente(Connection conn) {
        try {
            Scanner sc = new Scanner(System.in);

            System.out.println("Ingrese el mes para ver el top clientes (1 es enero, 12 es diciembre)");
            String mes = sc.nextLine();

            String calcularPromedio = "select avg (cantidad_recibida/cantidad_enviada) as promedio from transaccion where moneda_recibida = 'bs' and tipo = 'venta' " + 
            " and extract (month from fecha) = " + mes + " ;";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(calcularPromedio);
            double promedioTasa = 0;

            while (rs.next()) {
                promedioTasa = rs.getDouble("promedio");                
            }

            String consultaTopCliente = "select concat (nombre, ' ', apellido) as cliente, count(*) as cantidad_trx, " +
            "(sum(cantidad_recibida) * avg(cantidad_enviada/cantidad_recibida))/" + promedioTasa + " as profit " +
            " from transaccion inner join cliente on transaccion.cedula_cliente = cliente.cedula where moneda_enviada = 'bs' and tipo = 'compra' " + 
            " and extract (month from fecha) = " + mes + " group by cliente.cedula order by profit desc";

            System.out.println("Promedio de Tasa Bs en las transacciones: " + promedioTasa + "\nTOP CLIENTES: ");

            rs = st.executeQuery(consultaTopCliente);
            System.out.println(" | Cliente" + " | Cantidad Transacciones | Profit |");
            while (rs.next()) {
                System.out.println (" | " + rs.getString("cliente")+ " | "+rs.getString("cantidad_trx") + " | " + rs.getDouble("profit") + " | ");
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            System.out.println("Error de conexion. " + e.getCause());
        }
    }
}




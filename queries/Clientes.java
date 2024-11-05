package queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class Clientes {
    /***
     * Muestra todos los datos de todos los clientes registrados
     * @param conn
     */
    public static void fillClients(final Connection conn) throws SQLException {

        String query = "select * from cliente";

        try (PreparedStatement st = conn.prepareStatement(query)) {

            final ResultSet rs = st.executeQuery();
            
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
    
    /**
     * Retorna verdadero si un cliente existe dado un numero de cedula
     * Si la cedula existe, el cliente existe, y returna true. En caso contrario, retorna false
     * @param conn
     * @param cedula
     * @return boolean
     */
    public static boolean existClient(final Connection conn, final String cedula) throws SQLException {
        
        final String query = "SELECT COUNT(*) FROM cliente WHERE cedula = ?";

        try (PreparedStatement st = conn.prepareStatement(query)) {
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

    /**
     * Muestra el registro de clientes dado un alias
     * @param conn
     * @param alias
     */
    public static void fillByAlias(final Connection conn, final String alias) throws SQLException {
        final String query = "select * from cliente where alias = ?";

        try (PreparedStatement st = conn.prepareStatement(query)) {
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

    /**
     * Muestra la lista de Top Clientes dado un periodo de tiempo, es decir, 
     * desde una fecha de inicio a una fecha de fin.
     * Esta ordenado de forma decreciente tomando en cuenta el profit del cliente en ese tiempo.
     * @param conn
     * @param startDate
     * @param endDate
     */
    public static void fillTopClient(final Connection conn, final String startDate, final String endDate) throws SQLException {
        String query = "select concat (nombre, ' ', apellido) as cliente, count(*) as cantidad_trx, " + 
        "(sum(cantidad_recibida) * avg(cantidad_enviada/cantidad_recibida))/(" +
        "select avg (cantidad_recibida/cantidad_enviada) as promedio from transaccion where moneda_recibida = 'bs' and tipo = 'venta' " +
        "and fecha between '" + startDate + "' and '" + endDate + "') as profit " + 
        "from transaccion inner join cliente on transaccion.cedula_cliente = cliente.cedula where moneda_enviada = 'bs' and tipo = 'compra' " +
        "and fecha between '" + startDate + "' and '" + endDate + "' group by cliente.cedula order by profit desc";
        
        try (PreparedStatement st = conn.prepareStatement(query)) {
            ResultSet rs = st.executeQuery();

            while (rs.next()) {
                String cliente = rs.getString("cliente");
                String cantidad_transacciones = rs.getString("cantidad_trx");
                String profit = rs.getString("profit");
                System.out.println (" | " + cliente +  " | "+ cantidad_transacciones + " | " + profit + " | ");                  
            }

            rs.close();
            st.close();
        }
    }

    /**
     * Inserta un nuevo cliente en la tabla cliente
     * @param conn
     * @param alias
     * @param nombre
     * @param apellido
     * @param cedula
     */
    public static void insertClient(final Connection conn, final String alias, final String nombre, final String apellido, final String cedula) throws SQLException {
        final String query = "insert into cliente(alias, nombre, apellido, cedula) values (?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(query)){
            st.setString(1, alias);
            st.setString(2, nombre);
            st.setString(3, apellido);
            st.setString(4, cedula);
            // int numUpdate = st.executeUpdate();

            st.close();
        }
    }

}

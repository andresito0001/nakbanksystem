package main.java.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.postgresql.core.SqlCommand;

import javafx.collections.ObservableList;
import main.java.entities.Clients;
import main.java.util.ConnectionPool;

public class ClientsDAO {

    public ClientsDAO(final Connection conn) {
        this.conn = conn;
    }

    /***
     * Muestra todos los datos de todos los clientes registrados
     * @param conn
     * @return void
     */
    public StringBuilder fillClients() throws SQLException {
        String query = "select * from cliente";
        StringBuilder metaData = new StringBuilder();

        try (PreparedStatement st = conn.prepareStatement(query)) {
            final ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                metaData.append("Cedula: ").append(rs.getString("cedula"))
                .append(", Alias: ").append(rs.getString("alias"))
                .append(", Nombre: ").append(rs.getString("nombre"))
                .append(", Apellido: ").append(rs.getString("apellido"))
                .append("\n");
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("[ERROR]: " + e.getSQLState());
        }

        return metaData;
    }
    
    /**
     * Retorna verdadero si un cliente existe dado un numero de cedula
     * Si la cedula existe, el cliente existe, y returna true. En caso contrario, retorna false
     * @param conn
     * @param cedula
     * @return boolean
     */
    public boolean existClient(final String cedula) throws SQLException {
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
    public void fillByAlias(final String alias) throws SQLException {
        final String query = "select * from cliente where alias = ?";

        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setString(1, alias);
            final ResultSet rs = st.executeQuery();
            StringBuilder metaData = new StringBuilder();

            while (rs.next()) {
                metaData.append("Alias: ").append(rs.getString("alias"))
                .append(", Nombre: ").append(rs.getString("nombre"))
                .append(", Apellido: ").append(rs.getString("apellido"))
                .append(", Cedula: ").append(rs.getString("cedula"));
            }

            System.out.println (metaData);

            rs.close();
            st.close();
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
    public void fillTopClient(final Date beginDate, final Date endDate) throws SQLException {
        String query = "select concat (nombre, ' ', apellido) as cliente, count(*) as cantidad_trx, " + 
        "(sum(cantidad_recibida) * avg(cantidad_enviada/cantidad_recibida))/(" +
        "select avg (cantidad_recibida/cantidad_enviada) as promedio from transaccion where moneda_recibida = 'bs' and tipo = 'venta' " +
        "and fecha between ? and ? ) as profit " + 
        "from transaccion inner join cliente on transaccion.cedula_cliente = cliente.cedula where moneda_enviada = 'bs' and tipo = 'compra' " +
        "and fecha between ? and ? group by cliente.cedula order by profit desc";
                    
        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setDate(1, beginDate);
            st.setDate(2, endDate);
            st.setDate(3, beginDate);
            st.setDate(4, endDate);
            final ResultSet rs = st.executeQuery();
            StringBuilder metaData = new StringBuilder();

            while (rs.next()) {
                metaData.append("Cliente: ").append(rs.getString("cliente"))
                .append(", Transacciones Realizadas: ").append(rs.getString("cantidad_trx"))
                .append(", Profit: ").append(rs.getString("profit"));
            }

            System.out.println(metaData);

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
    public void insertClient(final Clients client) throws SQLException {
        final String query = "insert into clientes(alias, nombre, apellido, cedula) values (?, ?, ?, ?)";
        try (PreparedStatement st = conn.prepareStatement(query)){
            st.setString(1, client.getAlias());
            st.setString(2, client.getName());
            st.setString(3, client.getLastName());
            st.setString(4, client.getCi());

            st.executeUpdate();
            st.close();
        }
    }

    public List<Clients> getClientsAsList() throws SQLException {
        final String query = "select nombre, apellido, cedula, alias from clientes";
        List<Clients> clients = new ArrayList<>();

        try (PreparedStatement st = conn.prepareStatement(query)) {
            final ResultSet rs = st.executeQuery();

            while (rs.next()) {
                Clients client = new Clients(rs.getString("cedula"), rs.getString("nombre"), rs.getString("apellido"), rs.getString("alias"));
                clients.add(client);
            }

            rs.close();
            st.close();
        }

        return clients;
    }
    public void getClientsFilter(String filtro, String value, ObservableList<Clients> listaClientes) throws SQLException {
        final String query = "select cedula, nombre, apellido, alias from clientes where " + filtro + " ~ " +  "'" + "\\" + "m" + value + "' ";

       // List<Clients> clients = new ArrayList<>();
        try (PreparedStatement st = ConnectionPool.getConnection().prepareStatement(query)) {
            final ResultSet rs = st.executeQuery();
            while(rs.next()) {
                Clients cliente = new Clients(rs.getString("cedula"), rs.getString("nombre"), rs.getString("apellido"), rs.getString("alias"));
                listaClientes.add(cliente);
            }
            rs.close();
            st.close();
        }
       // return clients;
    }

    public final Clients getCLientBy(final String condition) throws SQLException {
        final String query = "select cedula, nombre, apellido, alias from clientes "
        .concat(condition)
        .concat(" limit 1");
        
        final Connection cpnn = ConnectionPool.getConnection();

        try (final PreparedStatement st = cpnn.prepareStatement(query);
        final ResultSet rs = st.executeQuery(); ) {
            if (rs.next()) {
                Clients client = new Clients(rs.getString("cedula"),
                rs.getString("nombre"), rs.getString("apellido"),
                rs.getString("alias"));

                return client;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al intentar obtener datos del cliente");
        } finally { 
            if (cpnn != null)
                ConnectionPool.releaseConnection(cpnn);
        }
    }
/*
    public static void generarLista (ObservableList<ClientsDAO> listaClients, String filtro, String value) throws SQLException {
        final String query = "select cedula, nombre, apellido, alias where " + filtro + " = '" + value + "'";

        try (PreparedStatement st = conn.prepareStatement(query)) {
            final ResultSet rs = st.executeQuery();

            while(rs.next()) {
                listaClients.add()
                //Clients client = new Clients(rs.getString("cedula"), rs.getString("nombre"), rs.getString("apellido"), rs.getString("alias"));
                //listaClients.add(client);
            }
        }
    }
    */


    private final Connection conn;
}
package main.java.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.Main;
import main.java.entities.Transactions;
import main.java.util.ConnectionPool;
import main.java.util.TimeZone;

public class TransactionsDAO {
    public void fillByClient(final String cedula, Date beginDate, Date endDate) throws SQLException {
        final String query = "select * from transacciones where cedula_cliente = ? and fecha between ? and ?";
        
        try (final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement st = conn.prepareStatement(query);
            final ResultSet rs = st.executeQuery()) {
            
            st.setString(1, cedula);
            st.setDate(2, beginDate);
            st.setDate(3, endDate);
            
            StringBuilder metaData = new StringBuilder();

            while (rs.next()) {
                metaData.append("Fecha: ").append(String.valueOf(rs.getDate("fecha")))
                .append(", tipo: ").append(rs.getString("tipo"))
                .append(", cantidad_recibida: ").append(rs.getString("cantidad_recibida"))
                .append(", moneda_recibida: ").append(rs.getString("moneda_recibida"))
                .append(", metodo_recibido: ").append(rs.getString("metodo_recibido"))
                .append(", cantidad_enviada: ").append(rs.getString("cantidad_enviada"))
                .append(", moneda_enviada: " ).append(rs.getString("moneda_enviada"))
                .append(", metodo_enviado: ").append(rs.getString("metodo_enviado"))
                .append(", status: ").append(rs.getString("status")).append("\n");
            }
        }
    }

    public void fillByDate(final Date beginDate, final Date endDate) throws SQLException {
        final String query = "select c.nombre, c.apellido, c.cedula, t.fecha, t.tipo, t.cantidad_recibida, t.moneda_recibida, " +
        "t.metodo_recibido, t.cantidad_enviada, t.moneda_enviada, t.metodo_enviado, t.status " +
        "from transacciones t " +
        "inner join clientes c on t.cedula_cliente = c.cedula " +
        "where t.fecha between ? and ?";

        try (final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement st = conn.prepareStatement(query);
            final ResultSet rs = st.executeQuery()) {
            st.setDate(1, beginDate);
            st.setDate(2, endDate);
            
            StringBuilder metaData = new StringBuilder();
            
            while (rs.next()) {
                metaData.append("Nombre: ").append(rs.getString("nombre"))
                .append(", Apellido: ").append(rs.getString("apellido"))
                .append(", Cedula: ").append(rs.getString("cedula"))
                .append(", Fecha: ").append(String.valueOf(rs.getDate("fecha"))).append("\n");
            }

            System.out.println(metaData);
            rs.close();
            st.close();
        }
    }

    public final Integer getNumOftTransByTypeAndDate(final String type, final Date beginDate, final Date endDate) throws SQLException {
        final String query = "select tipo, count(*) as total_transacciones " +
                        "from transacciones where tipo = ? and fecha between ? and ? group by tipo;";

        try (final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement st = conn.prepareStatement(query);
            final ResultSet rs = st.executeQuery()) {
            st.setString(1, type);
            st.setDate(2, beginDate);
            st.setDate(3, endDate);

            rs.next();
            
            final Integer count = rs.getInt("total_transacciones");
            
            return count > 0 ? count : 0;
        }
    }
    
    public final Float getAllAmountReceivedBy(final String transType, final String currencyReceived,
                                                final String receivedMethod) throws SQLException {
        final String query = "select sum(cantidad_recibida) as total "
                              + "from transacciones " 
                              + "where tipo = ? and moneda_recibida = ? and metodo_recibido = ? ";
        
        try (final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement st = conn.prepareStatement(query);
            final ResultSet rs = st.executeQuery()) {
            st.setString(1, transType);
            st.setString(2, currencyReceived);
            st.setString(3, receivedMethod);

            rs.next();

            return rs.getFloat("total");
        }
    }

    public List<String> getAccountsReceivable() {
        List<String> accounts = new ArrayList<>();
        return accounts;

        // ... 
    }
    
    public void newTransaction(Transactions transaction) throws SQLException {
        final String query = "insert into transacciones (id, cicle_id, cedula_cliente, admin, fecha, tipo, "
        + "cantidad_recibida, moneda_recibida, metodo_recibido, cantidad_enviada, "
        + "moneda_enviada, metodo_enviado, status, tasa, ganancia, ref_bancaria) "
        + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (final Connection connection = ConnectionPool.getConnection();
        final PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, transaction.getId());
            st.setString(2, transaction.getParent_id());
            st.setString(3, transaction.getClient().getCi());
            st.setString(4, Main.getUsername());
            st.setDate(5, Date.valueOf(TimeZone.getDateZoneCaracas()));
            st.setString(6, transaction.getType());
            st.setDouble(7, transaction.getQuantityReceived());
            st.setString(8, transaction.getCurrencyReceived());
            st.setString(9, transaction.getReceivedMethod());
            st.setDouble(10, transaction.getSentQuantity());
            st.setString(11, transaction.getSentCurrency());
            st.setString(12, transaction.getSentMethod());
            st.setString(13, transaction.getStatus());
            st.setDouble(14, transaction.getRate());
            st.setDouble(15, transaction.getRevenue());
            st.setString(16, transaction.getBankRef());

            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[ERROR] Mensaje: " + e.getMessage());
            System.err.println("[ERROR] Estado SQL: " + e.getSQLState());
            System.err.println("[ERROR] Código de error: " + e.getErrorCode());
            e.printStackTrace();
        }
    }
}
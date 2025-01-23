package main.java.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionsDAO {
    public TransactionsDAO(final Connection conn) {
        this.conn = conn;
    }

    public void fillByClient(final String cedula, Date beginDate, Date endDate) throws SQLException {
        final String query = "select * from transaccion where cedula_cliente = ? and fecha between ? and ?";
        try (PreparedStatement st = this.conn.prepareStatement(query);) {
            st.setString(1, cedula);
            st.setDate(2, beginDate);
            st.setDate(3, endDate);
            
            final ResultSet rs = st.executeQuery();
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
            System.out.println(metaData);
            rs.close();
            st.close();
        }
    }

    public void fillByDate(final Date beginDate, final Date endDate) throws SQLException {
        String query = "select c.nombre, c.apellido, c.cedula, t.fecha, t.tipo, t.cantidad_recibida, t.moneda_recibida, " +
        "t.metodo_recibido, t.cantidad_enviada, t.moneda_enviada, t.metodo_enviado, t.status " +
        "from trans t " +
        "inner join clientes c on t.cedula_cliente = c.cedula " +
        "where t.fecha between ? and ?";

        try (final PreparedStatement st = this.conn.prepareStatement(query)) {
            st.setDate(1, beginDate);
            st.setDate(2, endDate);
            final ResultSet rs = st.executeQuery();
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

    public Integer getNumOftTransByTypeAndDate(final String type, final Date beginDate, final Date endDate) throws SQLException {
        final String query = "select tipo, count(*) as total_transacciones " +
                        "from transaccion where tipo = ? and fecha between ? and ? group by tipo;";

        try (final PreparedStatement st = conn.prepareStatement(query)) {
            st.setString(1, type);
            st.setDate(2, beginDate);
            st.setDate(3, endDate);

            final ResultSet rs = st.executeQuery();
            rs.next();

            Integer count = rs.getInt("total_transacciones");
            
            rs.close();
            st.close();
            return count > 0 ? count : 0;
        }
    }
    
    public Float getAllAmountReceivedBy(final String transType, final String currencyReceived,
                                                final String receivedMethod) throws SQLException {
        final String query = "select sum(cantidad_recibida) as total "
                              + "from transaccion " 
                              + "where tipo = ? and moneda_recibida = ? and metodo_recibido = ? ";
        
        try (final PreparedStatement st = this.conn.prepareStatement(query)) {
            st.setString(1, transType);
            st.setString(2, currencyReceived);
            st.setString(3, receivedMethod);

            final ResultSet rs = st.executeQuery();
            rs.next();

            Float total = rs.getFloat("total");
            rs.close();
            st.close();

            return total;
        }
    }

    public List<String> getAccountsReceivable() {
        List<String> accounts = new ArrayList<>();
        return accounts;

        
    }
    
    private final Connection conn;
}
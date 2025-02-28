package main.java.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.Main;
import main.java.entities.Cycle;
import main.java.util.ConnectionPool;

public class CicleDAO {
    public StringBuilder fillByDate(final Date beginDate, final Date endDate) throws SQLException {
        final String query = "select c.cedula, a.nombre_usuario, t.id, t.fecha, t.cantidad_recibida, t.moneda_recibida, " +
            "t.metodo_recibido, t.cantidad_enviada, t.moneda_enviada, t.metodo_enviado, t.status, t.tasa " +
            "from ciclos t " +
            "inner join clientes c on t.cedula_cliente = c.cedula " +
            "inner join administradores a on t.admin = a.nombre_usuario " +
            "where t.fecha >= ? AND t.fecha <= ?";
        
        try (final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement st = conn.prepareStatement(query);
            final ResultSet rs = st.executeQuery()) {
            st.setDate(1, beginDate);
            st.setDate(2, endDate);
            
            StringBuilder metaData = new StringBuilder();
            
            while (rs.next()) {
                metaData.append("ID: ").append(rs.getString("id"))
                .append(", Cedula: ").append(rs.getString("cedula"))
                .append(", admin: ").append(rs.getString("nombre_usuario"))
                .append(", Recibido: ").append(rs.getString("cantidad_recibida") + " " + rs.getString("moneda_recibida") + " en " + rs.getString("metodo_recibido"))
                .append(", Enviado: ").append(rs.getString("cantidad_enviada") + " " + rs.getString("moneda_enviada") + " desde " + rs.getString("metodo_enviado"))
                .append(", status: ").append(rs.getString("status"))
                .append(", tasa: ").append(rs.getDouble("tasa"))
                .append(", Fecha: ").append(String.valueOf(rs.getDate("fecha"))).append("\n------------------------------------------------------------------------------------------\n");
            }

            return metaData;
        }
    }

    public final List<String> getIdsByDate(final Date beginDate, final Date endDate) throws SQLException {
        List<String> ids =  new ArrayList<>();
        final String query = "select id from ciclos where fecha between ? and ?";

        try (final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement st = conn.prepareStatement(query);
            final ResultSet rs = st.executeQuery()) {
            st.setDate(1, beginDate);
            st.setDate(2, endDate);
            
            while(rs.next()) { 
                ids.add(rs.getString("id"));
            }
        }
        return ids;
    }

    public void insertCycle (Cycle ciclo) throws SQLException {
        final String query = "insert into ciclos " + 
        "(id, cedula_cliente, admin, fecha, hora, cantidad_recibida, moneda_recibida, metodo_recibido, cantidad_enviada, moneda_enviada, metodo_enviado, status, tasa, ref_bancaria, status_recepcion) " + 
        " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement st = conn.prepareStatement(query)) {
                st.setString(1, ciclo.getId());
                st.setString(2, ciclo.getClient().getCedula());
                st.setString(3, Main.getUsername());
                st.setString(4, ciclo.getDate());
                st.setString(5, ciclo.getTime());
                st.setDouble(6, ciclo.getQuantityReceived());
                st.setString(7, ciclo.getCurrencyReceived());
                st.setString(8, ciclo.getReceivedMethod());
                st.setDouble(9, ciclo.getSentQuantity());
                st.setString(10, ciclo.getSentCurrency());
                st.setString(11, ciclo.getSentMethod());
                st.setString(12, ciclo.getStatus());
                st.setDouble(13, ciclo.getRate());
                st.setString(14, ciclo.getBankRef());
                st.setString(15, ciclo.getStatusRecepcion());

                st.executeQuery();
        } catch (Exception e) {
            throw new SQLException("Error al crear el ciclo");
        }
    }
}
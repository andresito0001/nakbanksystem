package main.java.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.Main;
import main.java.entities.Clients;
import main.java.entities.Cycle;
import main.java.util.ConnectionPool;
import main.java.util.TimeZone;

public class CicleDAO {
    public void fillByDate(List<Cycle> listaCycles, final Date beginDate, final Date endDate, String condition) throws SQLException {
        String query = "select * from ciclos where fecha between ? and ? ";
        if (condition != null) {
            query = query + condition;
        }
        
        try (final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement st = conn.prepareStatement(query)) {
            st.setDate(1, beginDate);
            st.setDate(2, endDate);

            ResultSet rs = st.executeQuery();
                        
            while (rs.next()) {
                String clientID = rs.getString("cedula_cliente");
                Clients client = new ClientsDAO().getCLientBy("where cedula = '" + clientID + "'");
                Cycle cycle = new Cycle(
                    rs.getString("id"),
                    client,
                    rs.getString("admin"), 
                    rs.getDate("fecha").toString(), 
                    rs.getTimestamp("hora").toString(), 
                    rs.getDouble("cantidad_recibida"), 
                    rs.getString("moneda_recibida"), 
                    rs.getString("metodo_recibido"), 
                    rs.getDouble("cantidad_enviada"), 
                    rs.getString("moneda_enviada"), 
                    rs.getString("metodo_enviado"), 
                    rs.getString("status"), 
                    rs.getDouble("tasa"), 
                    rs.getString("ref_bancaria"), 
                    rs.getString("status_recepcion"));

                listaCycles.add(cycle);
            }

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
        final String query = "insert into ciclos (id, cedula_cliente, admin, fecha, cantidad_recibida, moneda_recibida, metodo_recibido, cantidad_enviada, moneda_enviada, metodo_enviado, status, tasa, ref_bancaria, status_recepcion) " + 
        " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (final Connection conn = ConnectionPool.getConnection()) {
            final PreparedStatement st = conn.prepareStatement(query);
                st.setString(1, ciclo.getId());
                st.setString(2, ciclo.getClient().getCedula());
                st.setString(3, Main.getUsername());
                st.setDate(4, Date.valueOf(TimeZone.getDateZoneCaracas()));
                st.setDouble(5, ciclo.getQuantityReceived());
                st.setString(6, ciclo.getCurrencyReceived());
                st.setString(7, ciclo.getReceivedMethod());
                st.setDouble(8, ciclo.getSentQuantity());
                st.setString(9, ciclo.getSentCurrency());
                st.setString(10, ciclo.getSentMethod());
                st.setString(11, ciclo.getStatus());
                st.setDouble(12, ciclo.getRate());
                st.setString(13, ciclo.getBankRef());
                st.setString(14, ciclo.getStatusRecepcion());

                st.executeQuery();
                st.close();
        } catch (SQLException e) {
            System.err.println("[ERROR] Mensaje: " + e.getMessage());
            System.err.println("[ERROR] Estado SQL: " + e.getSQLState());
            System.err.println("[ERROR] Código de error: " + e.getErrorCode());
            e.printStackTrace();
        }
    }

    public Cycle getCycle (String id_ciclo) throws SQLException{
        final String query =
             "select id, clientes.nombre as nombre, clientes.apellido as apellido, clientes.cedula as cedula, clientes.alias as alias," +
            " admin, fecha, hora, cantidad_recibida, moneda_recibida, metodo_recibido, " + 
            "cantidad_enviada, moneda_enviada, metodo_enviado, status, tasa, ref_bancaria, status_recepcion from ciclos " +
            "join clientes on ciclos.cedula_cliente = clientes.cedula where id = '" + id_ciclo + "'" + " limit 1";
        try (final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement st = conn.prepareStatement(query)) {
            final ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    
                    return new Cycle(
                        rs.getString("id"), 
                        new Clients(rs.getString("cedula"), rs.getString("nombre"), rs.getString("apellido"), rs.getString("alias")),
                        rs.getString("admin"), 
                        rs.getDate("fecha").toString(), 
                        rs.getTimestamp("hora").toString(),
                        rs.getDouble("cantidad_recibida"), 
                        rs.getString("moneda_recibida"), 
                        rs.getString("metodo_recibido"), 
                        rs.getDouble("cantidad_enviada"), 
                        rs.getString("moneda_enviada"), 
                        rs.getString("metodo_enviado"), 
                        rs.getString("status"), 
                        rs.getDouble("tasa"), 
                        rs.getString("ref_bancaria"), 
                        rs.getString("status_recepcion"));
                }
                else 
                    return null;
        } 
    }

    public Double getAvailableByCycle (String cycleId) throws SQLException {
        final String query = "select sum(cantidad) as spend from inventario inner join " + 
                        "transacciones on id_trans = transacciones.id " + 
                        "where cicle_id = '" + cycleId + "' and " + 
                        "moneda = 'VES' and tipo_movimiento = 'EGRESO'";
        try (final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement st = conn.prepareStatement(query);
            final ResultSet rs = st.executeQuery()) {
                if (rs.next()) { 
                    return rs.getDouble("spend");
                } else {
                    return 0.0;
                }

        }
    }

}
package main.java.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.List;

import main.java.entities.Inventory;
import main.java.util.ConnectionPool;

public class InventoryDAO {
    public void newRegister(final String referencia, final Date fecha, final java.sql.Timestamp hora, final String tipo_mov,
                                        final Double cantidad, final String moneda, final String metodo,
                                        final String tipo, final String id_trans) throws SQLException {
        final String query = "insert into inventario(referencia, fecha, hora, tipo_movimiento, cantidad, moneda, metodo, tipo, id_trans)" +
                              "values (?,?, ?, ?, ?, ?, ?, ?, ?);";

        try (final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement st = conn.prepareStatement(query)) {
            st.setString(1, referencia);
            st.setDate(2, fecha);
            st.setTimestamp(3, hora);
            st.setString(4, tipo_mov);
            st.setDouble(5, cantidad);
            st.setString(6, moneda);
            st.setString(7, metodo);
            st.setString(8, tipo);
            st.setString(9, id_trans);

            st.executeUpdate();
        }
    }

    public void newRegister(Inventory inventory) throws SQLException {
        final String query = "insert into inventario(referencia, fecha, hora, tipo_movimiento, cantidad, moneda, metodo, tipo, id_trans)" +
                              "values (?, ?, ?, ?, ?, ?, ?, ?, ?);";

        try (final Connection connection = ConnectionPool.getConnection();
             final PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, inventory.getReference());
            st.setDate(2, Date.valueOf(inventory.getDate()));
            st.setTime(3, Time.valueOf(inventory.getTime()));
            st.setString(4, inventory.getMovementType());
            st.setDouble(5, inventory.getQuantity());
            st.setString(6, inventory.getMoneyType());
            st.setString(7, inventory.getMethod());
            st.setString(8, inventory.getOperationType());
            st.setString(9, inventory.getTransId());

            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("[ERROR] Mensaje: " + e.getMessage());
            System.err.println("[ERROR] Estado SQL: " + e.getSQLState());
            System.err.println("[ERROR] Código de error: " + e.getErrorCode());
            e.printStackTrace();
        }
    }

    public void fillByDate (List<Inventory> listaInventory, final Date beginDate, final Date endDate, String condition) throws SQLException {
        String query = "select * from inventario where fecha between ? and ?";
    
        if (condition != null) {
            query = query + condition;
        }

        try (final Connection connection = ConnectionPool.getConnection(); 
            final PreparedStatement st = connection.prepareStatement(query)) {
                st.setDate(1, beginDate);
                st.setDate(2, endDate);
                
                ResultSet rs = st.executeQuery();

                while (rs.next()) {
                    Inventory inventory = new Inventory(
                        rs.getString("referencia"), 
                        rs.getDate("fecha").toString(), 
                        rs.getString("tipo_movimiento"),
                        rs.getDouble("cantidad"), 
                        rs.getString("moneda"), 
                        rs.getString("metodo"), 
                        rs.getString("tipo"), 
                        rs.getTimestamp("hora").toString(),
                        rs.getString("id_trans")
                    ); 
                    listaInventory.add(inventory);
                }
        }
    }
}

package main.java.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InventoryDAO {
    public InventoryDAO(final Connection conn) {
        this.conn = conn;
    }
    
    public void newRegister(final Date fecha, final java.sql.Timestamp hora, final String tipo_mov,
                                        final Double cantidad, final String moneda, final String metodo,
                                        final String tipo) throws SQLException {
        final String query = "insert into simuinv(fecha, hora, tipo_movimiento, cantidad, moneda, metodo, tipo)" +
                              "values (?, ?, ?, ?, ?, ?, ?);";

        try (final PreparedStatement st = conn.prepareStatement(query)) {
            st.setDate(1, fecha);
            st.setTimestamp(2, hora);
            st.setString(3, tipo_mov);
            st.setDouble(4, cantidad);
            st.setString(5, moneda);
            st.setString(6, metodo);
            st.setString(7, tipo);

            st.executeUpdate();
            st.close();
        }
    }

    private final Connection conn;
}

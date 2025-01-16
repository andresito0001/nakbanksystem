package main.java.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InventoryDAO {
    public InventoryDAO(final Connection conn) {
        this.conn = conn;
    }
    
    public void newRegister(final String referencia, final Date fecha, final java.sql.Timestamp hora, final String tipo_mov,
                                        final Double cantidad, final String moneda, final String metodo,
                                        final String tipo, final String id_trans) throws SQLException {
        final String query = "insert into inventario(referencia, fecha, hora, tipo_movimiento, cantidad, moneda, metodo, tipo, id_trans)" +
                              "values (?,?, ?, ?, ?, ?, ?, ?, ?);";

        try (final PreparedStatement st = conn.prepareStatement(query)) {
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
            st.close();
        }
    }

    private final Connection conn;
}

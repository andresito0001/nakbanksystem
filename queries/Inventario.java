package queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Date;

public class Inventario {
    /**
     * Muestra el historial de todo lo que ha ingresado y egresado a
     * través de las distintas transacciones y gastos
     * @param conn
     * @throws SQLException
     */
    public static void fillStock(final Connection conn) throws SQLException {

        final String query = "select * from inventario";
        try (PreparedStatement st = conn.prepareStatement(query)) {
            
            final ResultSet rs = st.executeQuery();
            StringBuilder metaData = new StringBuilder();

            while (rs.next()) {
                metaData.append("ID: ").append(rs.getString("inventario_id"))
                .append(", fecha: ").append(String.valueOf(rs.getString("fecha")))
                .append(", tipo: ").append(rs.getString("tipo_movimiento"))
                .append(", cantidad: ").append(rs.getString("cantidad"))
                .append(", moneda: ").append(rs.getString("moneda"))
                .append(", metodo: ").append(rs.getString("metodo"))
                .append(", operacion: ").append(rs.getString("tipo_operacion"));
            }

            rs.close();
            st.close();
        }
    }
    /**
     * Permite filtrar todos los ingresos o egresos de dinero al inventario
     * @param conn
     * @param type
     * @throws SQLException
     */
    public static void fillByType (final Connection conn, String type) throws SQLException {
        final String query = "select * from inventario where tipo = ?";
        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setString(1, type);
            final ResultSet rs = st.executeQuery();
            StringBuilder metaData = new StringBuilder();

            while (rs.next()) {
                metaData.append("ID: ").append(rs.getString("inventario_id"))
                .append(", fecha: ").append(String.valueOf(rs.getString("fecha")))
                .append(", Tipo: ").append(rs.getString("tipo_movimiento"))
                .append(", Cantidad: ").append(rs.getString("cantidad"))
                .append(", Moneda: ").append(rs.getString("moneda"))
                .append(", Metodo: ").append(rs.getString("metodo"))
                .append(", Operacion: ").append(rs.getString("tipo_operacion"));
            }

            rs.close();
            st.close();
        } 
    }    

    public static void fillByDate (final Connection conn, Date beginDate, Date endDate) throws SQLException {
        final String query = "select * from inventario where fecha between ? and ?";

        try (PreparedStatement st = conn.prepareStatement(query)) {
            st.setDate(1, beginDate);
            st.setDate(2, endDate);
            StringBuilder metaData = new StringBuilder();
            final ResultSet rs = st.executeQuery();

            while (rs.next()) {
                metaData.append("ID: ").append(rs.getString("inventario_id"))
                .append(", Fecha: ").append(String.valueOf(rs.getString("fecha")))
                .append(", Tipo: ").append(rs.getString("tipo_movimiento"))
                .append(", Cantidad: ").append(rs.getString("cantidad"))
                .append(", Moneda: ").append(rs.getString("moneda"))
                .append(", Metodo").append(rs.getString("metodo"))
                .append(", Operacion: ").append(rs.getString("tipo_operacion"));
            }
            rs.close();
            st.close();
        }
    }
}

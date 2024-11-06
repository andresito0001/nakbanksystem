package queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

public class Transacciones {
    /***
     * Muestra el historial de transacciones de un cliente dado su numero de cedula.
     * 
     * Tome en cuenta que este es el historico total de transacciones de este cliente
     * a lo largo del tiempo.
     * @param conn
     * @param cedula
     * @return void
     */
    public static void fillByClient(Connection conn, final String cedula) throws SQLException {
        final String query = "select * from transaccion where cedula_cliente = ?";
        try (PreparedStatement st = conn.prepareStatement(query);) {
            st.setString(1, cedula);
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

    public static void fillByDate(final Connection conn, final Date beginDate, final Date endDate) throws SQLException {
        String query = "select c.nombre, c.apellido, c.cedula, t.fecha, t.tipo, t.cantidad_recibida, t.moneda_recibida, " +
        "t.metodo_recibido, t.cantidad_enviada, t.moneda_enviada, t.metodo_enviado, t.status " +
        "from transaccion t " +
        "inner join cliente c on t.cedula_cliente = c.cedula " +
        "where t.fecha between ? and ?";

        try (final PreparedStatement st = conn.prepareStatement(query)) {
            st.setDate(1, beginDate);
            st.setDate(2, endDate);
            final ResultSet rs = st.executeQuery();
            StringBuilder metaData = new StringBuilder();
            
            while (rs.next()) {
                metaData.append("Nombre: ").append(rs.getString("nombre"))
                .append(", Apellido: ").append(rs.getString("apellido"))
                .append(", Cedula: ").append(rs.getString("cedula"))
                .append(", Fecha: ").append(String.valueOf(rs.getDate("fecha"))).append("\n");
                
                // el resto de los datos...
            }
            
            System.out.println(metaData);
            rs.close();
            st.close();
        }
    }

    }
}

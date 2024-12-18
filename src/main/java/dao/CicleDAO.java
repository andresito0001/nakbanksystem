package main.java.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CicleDAO {
    public CicleDAO (final Connection conn) {
        this.conn = conn;
    }

    public StringBuilder fillByDate(final Date beginDate, final Date endDate) throws SQLException {
        final String query = "select c.cedula, a.nombre_usuario, t.id, t.fecha, t.cantidad_recibida, t.moneda_recibida, " +
            "t.metodo_recibido, t.cantidad_enviada, t.moneda_enviada, t.metodo_enviado, t.status, t.tasa " +
            "from cicles t " +
            "inner join clientes c on t.cedula_cliente = c.cedula " +
            "inner join administradores a on t.admin = a.nombre_usuario " +
            "where t.fecha >= ? AND t.fecha <= ?";
        
        try (final PreparedStatement st = this.conn.prepareStatement(query)) {
            st.setDate(1, beginDate);
            st.setDate(2, endDate);
            final ResultSet rs = st.executeQuery();
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

            rs.close();
            st.close();
            return metaData;
        }
    }

    public final List<String> getIdsByDate(final Date beginDate, final Date endDate) throws SQLException {
        List<String> ids =  new ArrayList<>();
        final String query = "select id from cicles where fecha between ? and ?";

        try (final PreparedStatement st = this.conn.prepareStatement(query)) {
            st.setDate(1, beginDate);
            st.setDate(2, endDate);
            final ResultSet rs = st.executeQuery();

            while(rs.next()) { 
                ids.add(rs.getString("id"));
            }
            
            rs.close();
            st.close();
        }
        return ids;
    }
    
    private final Connection conn;
}
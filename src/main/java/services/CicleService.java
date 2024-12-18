package main.java.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CicleService {
    public CicleService(final Connection conn) {
        this.conn = conn;
    }

    // OBTIENE EL PROFIT DE UN CICLO ESPECIFICO
    public Double getProfit(final String id) throws SQLException {
        final String query = "select sum(ganancia) from trans where cicle_id = ?";

        try (final PreparedStatement st = this.conn.prepareStatement(query)) {
            st.setString(1, id);
            final ResultSet rs = st.executeQuery();

            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

    // OBTIENE EL SUMATORIO DE TODOS LOS PROFITS DE LOS CICLOS
    public Double getProfitByIds(final List<String> ciclesIds) throws SQLException {
        Double profit = 0.0;
        
        for (String entry : ciclesIds)
            profit += getProfit(entry);

        return profit;
    }

    // TASA PROMEDIO DE COMPRA DE USD A VES DE UN CICLO (SE PUEDE GENERALIZAR)
    public Double averagePurchaseRate(final String cicleId)  throws SQLException {
        final String query = "select avg(tasa) from trans where tipo = 'COMPRA' and moneda_recibida = 'USD' and moneda_enviada = 'VES' and cicle_id = " + "'" + cicleId + "';";

        try (final PreparedStatement st = this.conn.prepareStatement(query)) {
            final ResultSet rs = st.executeQuery();

            return rs.next() ? rs.getDouble(1) : 0;
        }
    }

    // TASA PROMEDIO DE COMPRA DE USD A VES DE VARIOS CICLOS (SE PUEDE GENERALIZAR)
    public Double averagePurchaseRatebYIds(final List<String> ids) throws SQLException {
        Double average = 0.0;

        for(String entry : ids) {
            average += averagePurchaseRate(entry);
        }
        
        return average / ids.size();
    }

    public Integer getNumofTransByCicleId(final String id)  throws SQLException {
        final String query = "select count(*) from trans where cicle_id = ?";

        try (final PreparedStatement st = this.conn.prepareStatement(query)) {
            st.setString(1, id);
            final ResultSet rs = st.executeQuery();

            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public Integer getTotalOfTransByCiclesIds(final List<String> ciclesIds) throws SQLException {
        Integer num = 0;

        for (String entry : ciclesIds) {
            num += getNumofTransByCicleId(entry);
        }

        return num;
    }

    private final Connection conn;
}

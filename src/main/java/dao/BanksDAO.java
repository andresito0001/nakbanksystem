package main.java.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BanksDAO {
    public BanksDAO(final Connection conn) {
        this.conn = conn;
    }

    public List<String> getInfoOf(final String colum, final String key, final String value) throws SQLException {
        final String query = "select " + colum + " from bancos where " + key + " = " + "'" + value + "'" + ";";

        List<String> data = new ArrayList<>();
        
        try (PreparedStatement st = conn.prepareStatement(query)) {
            final ResultSet rs = st.executeQuery();
            
            while (rs.next()) {
                data.add(rs.getString(colum));
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            System.err.println("[ERROR]: " + e.getSQLState());
        }
        
        return data;
    }

    public Double getTotalBalanceOf(final String bankCode) {
        Double totalBalance = 0.0;

        try (PreparedStatement stmt = conn.prepareStatement("select sum(saldo_actual) from bancos where codigo = ?")) {
            stmt.setString(1, bankCode);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalBalance = rs.getDouble(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalBalance;
    }

    private final Connection conn;
}
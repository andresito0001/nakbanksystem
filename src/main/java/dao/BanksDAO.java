package main.java.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import main.java.entities.Banks;
import main.java.util.ConnectionPool;

public class BanksDAO {
    public BanksDAO(final Connection conn) {
        this.conn = conn;
    }

    public List<String> getInfoOf(final String colum, final String key, final String value) throws SQLException {
        String query = null;

        if (key == null || value == null) {
            query = "select " + colum + " from bancos;";
        } else {
            query = "select " + colum + " from bancos where " + key + " = " + "'" + value + "'" + ";";
        }
        
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
                rs.close();
                stmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return totalBalance;
    }

    public void setBank (List<Banks> listaBancos, String key, String value) {
        String query = null;

        if (key == null || value == null) {
            query = "select * from bancos;";
        } else {
            query = "select * from bancos where " + key + " = " + "'" + value + "'" + ";";
        }
        try(PreparedStatement st = conn.prepareStatement(query)) {
            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                listaBancos.add(
                    new Banks(
                        rs.getString("codigo"), 
                        rs.getString("nombre_banco"), 
                        rs.getString("moneda"), 
                        rs.getDouble("saldo_actual")
                        )
                );
            }
            rs.close();
            st.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final Connection conn;
}
package main.java.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import main.java.entities.BankInfo;
import main.java.util.ConnectionPool;

import main.java.entities.Banks;
import main.java.util.ConnectionPool;

public class BanksDAO {
    public List<BankInfo> getAllBankInfo() throws SQLException {
        List<BankInfo> bankInfoList = new ArrayList<>();
        String query = "select codigo, nombre_banco, saldo_actual, moneda from bancos";
        final Connection conn = ConnectionPool.getConnection();

        try (PreparedStatement statement = conn.prepareStatement(query);) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                bankInfoList.add(new BankInfo(
                    resultSet.getString("codigo"),
                    resultSet.getString("nombre_banco"),
                    resultSet.getString("saldo_actual"),
                    resultSet.getString("moneda")
                ));
            }

            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener datos de bancos", e);
        } finally {
            if (conn != null) {
                ConnectionPool.releaseConnection(conn);
            }
        }

        return bankInfoList;
    }

    public List<String> getInfoOf(final String colum, final String key, final String value) throws SQLException {
        String query = null;
        final Connection conn = ConnectionPool.getConnection();

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
        } finally {
            if (conn == null) {
                ConnectionPool.releaseConnection(conn);
            }
        }
        
        return data;
    }

    public Double getTotalBalanceOf(final String bankCode) throws SQLException {
        Double totalBalance = 0.0;
        final Connection conn = ConnectionPool.getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement("select sum(saldo_actual) from bancos where codigo = ?")) {
            stmt.setString(1, bankCode);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    totalBalance = rs.getDouble(1);
                }
                rs.close();
                stmt.close();
            }

            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener datos de bancos", e);
        } finally {
            if (conn == null) {
                ConnectionPool.releaseConnection(conn);
            }
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

    public BanksDAO() throws SQLException {
        this.conn = ConnectionPool.getConnection();
    }
}
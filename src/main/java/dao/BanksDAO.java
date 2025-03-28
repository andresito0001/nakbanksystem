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

public class BanksDAO {
    public List<BankInfo> getAllBankInfo() throws SQLException {
        List<BankInfo> bankInfoList = new ArrayList<>();
        String query = "select codigo, nombre_banco, numero_cuenta, moneda, saldo_actual, correo from bancos";

        try (final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement statement = conn.prepareStatement(query);
            final ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                bankInfoList.add(new BankInfo (
                    resultSet.getString("codigo"),
                    resultSet.getString("nombre_banco"),
                    resultSet.getString("saldo_actual").concat(" " + resultSet.getString("moneda")),
                    resultSet.getString("moneda"),
                    resultSet.getString("correo"),
                    resultSet.getString("numero_cuenta")
                ));
            }

            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener datos de bancos", e);
        }

        return bankInfoList;
    }

    public List<String> getInfoOf(final String colum, final String key, final String value) throws SQLException {
        String query = new String();

        if (key == null || value == null) {
            query = "select " + colum + " from bancos;";
        } else {
            query = "select " + colum + " from bancos where " + key + " = " + "'" + value + "'" + ";";
        }
        
        List<String> data = new ArrayList<>();
        
        try (final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement st = conn.prepareStatement(query);
            final ResultSet rs = st.executeQuery()) {
            
            while (rs.next()) {
                data.add(rs.getString(colum));
            }

            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener datos de bancos", e);
        }
        
        return data;
    }

    public Double getTotalBalanceOf(final String bankCode) throws SQLException {
        Double totalBalance = 0.0;

        try (final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement stmt = conn.prepareStatement("select sum(saldo_actual) from bancos where codigo = ?")) {
        
            stmt.setString(1, bankCode);
            final ResultSet rs = stmt.executeQuery();

            if (rs.next())
                totalBalance = rs.getDouble(1);
            
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException("Error al obtener datos de bancos", e);
        }

        return totalBalance;
    }

    public void insertBank(Banks bank) throws SQLException {
        final String query = "insert into bancos (codigo, nombre_banco, moneda, correo, saldo_actual) values (?, ?, ?, ?, ?)";

        try (final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement st = conn.prepareStatement(query)) {
                
                st.setString(1, bank.getCodigo());
                st.setString(2, bank.getNombre());
                st.setString(3, bank.getMoneda());
                st.setString(4, bank.getCorreo());
                st.setDouble(5, bank.getSaldo());

                st.executeUpdate();
        } 
    }



    public void setBank(List<Banks> listaBancos, String key, String value) {
        String query = new String();

        if (key == null || value == null) {
            query = "select * from bancos;";
        } else {
            query = "select * from bancos where " + key + " = " + "'" + value + "'" + ";";
        }

        try(final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement st = conn.prepareStatement(query);
            ResultSet rs = st.executeQuery()) {
            
            while(rs.next()) {
                listaBancos.add (
                    new Banks (
                        rs.getString("codigo"), 
                        rs.getString("nombre_banco"), 
                        rs.getString("moneda"), 
                        rs.getDouble("saldo_actual"),
                        rs.getString("correo")
                    )
                );
            }

            rs.close();
            st.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package main.java.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import main.java.entities.CxC;
import main.java.util.ConnectionPool;

public class CxCDAO {
    public static void generarLista (List<CxC> listaCxC, String tipoCuenta) throws SQLException {
        String query = "select id_trans, cliente, monto_transaccion, moneda_trans, tipo, abonado, pendiente from " + tipoCuenta  + " where pendiente > 0";

        try (final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement st = conn.prepareStatement(query);
            final ResultSet rs = st.executeQuery()) {
            
                while (rs.next()) {
                listaCxC.add(
                    new CxC(rs.getString("id_trans"), rs.getString("cliente"), rs.getDouble("monto_transaccion"),rs.getString("moneda_trans") ,rs.getDouble("abonado"), rs.getDouble("pendiente"), rs.getString("tipo"), tipoCuenta)
                );
                rs.close();
                st.close();
            }
        } catch (SQLException exception) {
            throw new SQLException("Error al generar la lista de " + tipoCuenta);
        }
    }
    public void actualizarCxC(CxC cuenta) throws SQLException {
        final String query = "select pendiente from " + cuenta.getTipoCuenta() + " where id_trans = '" + cuenta.getIdTransaction() + "'";
        try (final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement st = conn.prepareStatement(query);
            final ResultSet rs = st.executeQuery())
        {
            while (rs.next()) {
                cuenta.setPendienteTransaccion(rs.getDouble("pendiente"));
            }
            rs.close();
            st.close();
            
        } catch (SQLException e) {
            throw new SQLException("Error al actualizar el pendiente de la cuenta");
        }
    }
}

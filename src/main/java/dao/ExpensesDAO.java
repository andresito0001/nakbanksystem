package main.java.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import main.java.entities.Gastos;
import main.java.util.ConnectionPool;

public class ExpensesDAO {
    public void registerExpense (Gastos newExpense) throws SQLException {
        final String query = "insert into gastos " + 
        "(id_gasto, id_ciclo, admin, fecha, departamento, elemento_contable, proveedor, descripcion, monto, metodo_enviado, usd_equivalente) " + 
        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (final Connection connection = ConnectionPool.getConnection();
            final PreparedStatement st = connection.prepareStatement(query)) {
            
            st.setString(1, newExpense.getId_Gasto());
            st.setString(2, newExpense.getCicle_Id());
            st.setString(3, newExpense.getAdmin());
            st.setDate(4, newExpense.getFecha());
            st.setString(5, newExpense.getDepartamento());
            st.setString(6, newExpense.getElementoContable().getCostElementName());
            st.setString(7, newExpense.getProveedor());
            st.setString(8, newExpense.getDescripcion());
            st.setDouble(9, newExpense.getMonto());
            st.setString(10, newExpense.getMetodo());
            st.setDouble(11, newExpense.getUsd_Equivalente());
            
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

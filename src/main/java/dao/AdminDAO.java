package main.java.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.java.entities.Admin;
import main.java.util.ConnectionPool;

public class AdminDAO {
    public void newUser(final Admin admin) throws SQLException {
        final String query = "insert into administradores(nombre_usuario, password, estado, rol) values (?, ?, ?);";

        try (final Connection conn = ConnectionPool.getConnection();
            final PreparedStatement st = conn.prepareStatement(query)) {
            st.setString(1, admin.getUserName());
            st.setString(2, admin.getPassword());
            st.setString(3, admin.getStatus());
            st.setString(4, admin.getStatus());

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
           throw new SQLException("Error al crear el usuario");
        }
    }

    public boolean authenticateUser(final String username) throws SQLException {
        final String query = "select password from administradores where nombre_usuario = ?";

        try (final Connection connection = ConnectionPool.getConnection();
        final PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, username);

            try (ResultSet rs = st.executeQuery()) {
                Boolean result = rs.next();
                rs.close();
                st.close();
                return result;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } 
    }

    public String authenticateRol(final String userName) {
        final String query = "select rol from administradores where nombre_usuario = ?";
        String rol = "operador";

        try (Connection conn = ConnectionPool.getConnection();
            final PreparedStatement st = conn.prepareStatement(query);
            final ResultSet rs = st.executeQuery()) {
            st.setString(1, userName);
 
            if (rs.next())
                rol = rs.getString("rol");
        
            return rol;
        } catch(SQLException e) {
            return "void";
        }
    }
}

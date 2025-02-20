package main.java.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import main.java.entities.Admin;
import main.java.util.ConnectionPool;

public class AdminDAO {
    public AdminDAO(final Connection conn) {
        this.conn = conn;
    }

    public void newUser(final Admin admin) throws SQLException {
        final String query = "insert into administradores(nombre_usuario, password, estado, rol) values (?, ?, ?);";

        try (final PreparedStatement st = conn.prepareStatement(query)) {
            st.setString(1, admin.getUserName());
            st.setString(2, admin.getPassword());
            st.setString(3, admin.getStatus());
            st.setString(4, admin.getStatus());

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            System.out.println("[ERROR] Este administrador ya esta registrado");
        }
    }

    public boolean authenticateUser(final String userName, final String password) throws SQLException {
        final String query = "select 1 from administradores where nombre_usuario = ? and password = ? limit 1";

        try (Connection connection = ConnectionPool.getConnection();
        final PreparedStatement st = connection.prepareStatement(query)) {
            st.setString(1, userName);
            st.setString(2, password);

            try (ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } 
    }

    public String authenticateRol(final String userName) throws SQLException {
        final String query = "select rol from administradores where nombre_usuario = ?";
        String rol = "operador";
        try (final PreparedStatement st = this.conn.prepareStatement(query)) {
            st.setString(1, userName);
 
            try (final ResultSet rs = st.executeQuery()) {
                if(rs.next())
                    rol = rs.getString("rol");
            } catch ( SQLException e) {
                e.printStackTrace();
            }
            return rol;
        }
    }

    private final Connection conn;
}

package queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Administradores {
    public static void newUser(final Connection conn, final String username, final String password) throws SQLException {
        final String query = "insert into administradores(nombre_usuario, password, estado) values (?, ?, ?);";

        try (final PreparedStatement st = conn.prepareStatement(query)) {
            st.setString(1, username);
            st.setString(2, password);
            st.setString(3, "ACTIVO");

            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            System.out.println("[ERROR] Este administrador ya esta registrado");
        }
    }

    public static boolean authenticateUser(final Connection conn, final String username, final String password) throws SQLException {
        final String query = "SELECT * FROM administradores WHERE nombre_usuario = ? AND password = ?";

        try (final PreparedStatement st = conn.prepareStatement(query)) {
            st.setString(1, username);
            st.setString(2, password);

            try (final ResultSet rs = st.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

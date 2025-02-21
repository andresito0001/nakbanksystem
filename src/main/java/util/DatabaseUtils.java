package main.java.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtils {
    public DatabaseUtils(final Connection conn ) {
        this.conn = conn;
    }

    public void updateRegister(final String table, final String colum, final Object value,
                                final String condition) throws SQLException {
        String sql = "update " + table + " set " + colum + " = ? where " + condition;
        
        try (final PreparedStatement st = this.conn.prepareStatement(sql)) {
            switch (value.getClass().getSimpleName()) {
                case "String": st.setString(1, (String) value);
                    break;
                case "Double": st.setDouble(1, (Double) value);
                    break;
                case "Float": st.setFloat(1, (Float) value);
                    break;
                case "Integer": st.setInt(1, (Integer) value);
                    break;
                default:
                    throw new IllegalArgumentException("Tipo de dato no soportado: " + value.getClass().getSimpleName());
            }
            st.executeUpdate();
        };
    }

    public void createTable(final String name, final List<String> colums,
                                    final List<String> types) throws SQLException {
        StringBuilder sql = new StringBuilder("create table if not exists " + name + " (");

        for (int i = 0; i < colums.size(); i++) {
            sql.append(colums.get(i)).append(" ").append(types.get(i));
                if (i < colums.size() - 1) {
                sql.append(", ");
            }
        }
        sql.append(");");

        try (final PreparedStatement st = conn.prepareStatement(sql.toString())) {
            st.executeUpdate();
            st.close();
        }
    }

    public boolean isEmptyTable(final String tableName) throws SQLException {
        final String query = "select exists (select 1 from " + tableName + ")";
           
        try (final PreparedStatement st = this.conn.prepareStatement(query)) {
                
            ResultSet rs = st.executeQuery();
            st.close();
    
            return !rs.getBoolean(1);
        }
    }

    public String getInfoByLastReferenceOf(final String tableName, final String column, 
                                    final String key, final String value) throws SQLException {
        final String query = key == null || value == null 
            ? "select " + column + " from " + tableName + " order by id desc limit 1"
            : "select " + column + " from " + tableName + " where " + key + " = ? order by id desc limit 1";

        try (final PreparedStatement st = conn.prepareStatement(query)) {
            if (key != null && value != null) {
                st.setString(1, value);
            }
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                String data = rs.getString(column);
                rs.close();
                return data;
            } else {
                return "VOID";
            }
        }
    }

    public Double sumColumn(final String column, final String tableName, final String condition) throws SQLException {
        final String query = "select sum(" + column + ") from " + tableName + " where " + condition;
        
        try (final PreparedStatement st = conn.prepareStatement(query)) {
            ResultSet rs = st.executeQuery();
            
            if (rs.next()) {
                return rs.getDouble(1);
            } else {
                return 0.0;
            }
        }
    } 

    public Object getValueOf(final String column, final String tableName, final String condition) throws SQLException {
        final String query = "select " + column + " from " + tableName + " where " + condition;
        try (final PreparedStatement st = conn.prepareStatement(query)) {
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                return rs.getObject(column);
            } else {
                return 0.0;
            }
        }
    }

    public final List<String> getTableSchemaAsList() throws SQLException {
        final String sql = "select table_name from information_schema.tables where table_schema = 'public';";

        final List<String> results = new ArrayList<>();
        
        try (final Connection connection = ConnectionPool.getConnection();
            final PreparedStatement st = connection.prepareStatement(sql); 
            final ResultSet rs = st.executeQuery()) {

                while (rs.next()) {
                    results.add(rs.getString("table_name"));
                }

                rs.close();
                st.close();
                
                if (connection != null) 
                    ConnectionPool.releaseConnection(connection);
            } catch (SQLException e) {
                throw new SQLException("Error al obtener el esquema de la base de datos", e);
            }

        return results;
    }

    private final Connection conn;
}

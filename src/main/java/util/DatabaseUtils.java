package main.java.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
        StringBuilder sql = new StringBuilder("create table " + name + " (");

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
        final String query;
        if (key == null || value == null) {
            query = "select " + column + " from " + tableName + " " +
            "order by id desc " + 
            "limit 1";
        } else {
            query = "select " + column + " from " + tableName + " " +
                                "where " + key + " = " + "'" + value + "' " + 
                                "order by id desc " + 
                                "limit 1";
        }

        try (final PreparedStatement st = conn.prepareStatement(query)) {
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

    public Object getValueOf(final String tableName, final String key, final String condition) throws SQLException {
        String query = "select " + key + " from " + tableName + " where " + condition;
        try (final PreparedStatement st = conn.prepareStatement(query)) {
            ResultSet rs = st.executeQuery();
            rs.next();
            return rs.getObject(key);
        }
    }

    private final Connection conn;
}

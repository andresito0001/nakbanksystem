package queries;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TableInfo {
    public static String getInfoByLastReferenceOf(final Connection conn, final String tableName, final String column, 
                                    final String key, final String value) throws SQLException {
        final String query = "select " + column + " from " + tableName + " " +
                             "where " + key + " = " + "'" + value + "' " + 
                             "order by referencia desc " + 
                             "limit 1";
        
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

}

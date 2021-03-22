package attendanceLotus.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CRUDOperation {

    private static final Connection conn = ConnectionUtil.connectdb();

    public static ResultSet statement(String query) throws Exception {
        Statement st = conn.createStatement();
        return st.executeQuery(query);
    }

    public static int executeQuery(String query) {
        Statement st;

        try {
            st = conn.createStatement();
            int test = st.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

            if (test != 0) {
                ResultSet getGenKeys = st.getGeneratedKeys();

                if (getGenKeys.next())
                    return getGenKeys.getInt(1);
                else
                    return 0;
            } else
                return 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
}

package attendanceLotus.util;

import java.sql.*;

public class ConnectionUtil {

    public static Connection connectdb() {

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/attendanceLotus","root","");
            return conn;
        }
        catch(Exception e) {
            //alert when no connection
            System.out.println(e);
            return null;
        }
    }

}

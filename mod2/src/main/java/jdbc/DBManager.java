package jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class DBManager {
    private static DBManager instance;

    public static synchronized DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();

        }
        return instance;
    }

    public static Connection getConnection() throws SQLException {
        String connection_url = "jdbc:mysql://localhost:3306/test?user=root&password=rootpassword";
        Connection con = DriverManager.getConnection(connection_url);
        con.setAutoCommit(false);
        return con;
    }

    private DBManager() {

    }

    public void commitAndClose(Connection con) {
        try {
            con.commit();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void rollbackAndClose(Connection con) {
        try {
            con.rollback();
            con.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}

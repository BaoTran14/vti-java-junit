package vn.vti;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private Logger log = LogManager.getLogger(DatabaseConnection.class);

    public Object initConnectionToDb(DatabaseType databaseType, String databaseName, String username, String password) throws SQLException {
        Object result;
        Log.info(DatabaseConnection.class, "Connecting to database", databaseName, "in ", databaseType, "...");
        switch (databaseType) {
            case MYSQL:
                try {
                    result = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/" + databaseName,
                            username,
                            password);
                } catch (SQLException e) {
                    throw new SQLException(e);
                }
                break;
            default:
                throw new RuntimeException("Not support connection to " + databaseType);
        }
        Log.info(DatabaseConnection.class, "Connected to ", databaseName, "in ", databaseType, "successfully!");
        return result;
    }
}

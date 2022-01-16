package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {
    String connectionString;
    Connection connection;
    public DBConnector() throws SQLException {
        connectionString = "jdbc:sqlserver://localhost:1433;user=sa;password=pass;" + "databaseName=project_testdb2;";

        
        DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
        connection = DriverManager.getConnection(connectionString);
    }

    public Statement createStatement() throws SQLException {
        return connection.createStatement();
    }

}

package utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBConnector {
    String connectionString;
    Connection connection;
    Statement statement;
    public DBConnector() throws SQLException, IOException {
        Config.load();
        connectionString = Config.connectionString;

        
        DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
        connection = DriverManager.getConnection(connectionString);
    }

    public Statement createStatement() throws SQLException {
        if(statement == null) {
            statement = connection.createStatement();
        }
        return this.statement;
    }
    public void close() throws SQLException {
        if(statement != null) statement.close();
        if(connection != null) connection.close();
    }
}
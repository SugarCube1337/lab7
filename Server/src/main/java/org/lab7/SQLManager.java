package org.lab7;

import java.sql.*;

/**
 * Manages database interactions using JDBC for the Route Management
 */
public class SQLManager {
    private final String url = "jdbc:postgresql://localhost/studs";
    private final String user = "postgres";
    private final String password = "12345";

    public SQLManager() {
    }

    /**
     * Gets a database connection using the configured URL, user, and password.
     *
     * @return A database connection.
     * @throws SQLException If there is an error while establishing the connection.
     */
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Executes a prepared SQL statement and logs it as an info message.
     *
     * @param sql The prepared SQL statement to execute.
     * @return True if the execution was successful, false otherwise.
     */

    public boolean send(PreparedStatement sql) {
        Main.logger.info(sql);
        try {
            sql.executeUpdate();
            sql.getConnection().close();
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    /**
     * Executes a raw SQL statement and logs it as an info message.
     *
     * @param sql The raw SQL statement to execute.
     * @return True if the execution was successful, false otherwise.
     */
    public boolean sendRaw(String sql) {
        Main.logger.info(sql);
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            connection.close();
        } catch (SQLException ex) {
            return false;
        }
        return true;
    }

    /**
     * Executes a prepared SQL query and logs it as an info message.
     *
     * @param sql The prepared SQL query to execute.
     * @return The ResultSet containing the query results, or null if there was an error.
     */
    public ResultSet get(PreparedStatement sql) {
        Main.logger.info(sql);
        ResultSet result = null;
        try {
            result = sql.executeQuery();
            sql.getConnection().close();
        } catch (SQLException ex) {
        }
        return result;
    }

    /**
     * Executes a raw SQL query and logs it as an info message.
     *
     * @param sql The raw SQL query to execute.
     * @return The ResultSet containing the query results, or null if there was an error.
     */
    public ResultSet getRaw(String sql) {
        Main.logger.info(sql);
        ResultSet result = null;
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            result = statement.executeQuery(sql);
            connection.close();
        } catch (SQLException ex) {
        }
        return result;
    }
}

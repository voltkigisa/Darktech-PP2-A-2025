package com.library.pos.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Database configuration class using Singleton pattern.
 * Manages MySQL database connections for the application.
 */
public class DatabaseConfig {

    // Database connection parameters
    private static final String HOST = "pongo.kencang.com";
    private static final String PORT = "3306";
    private static final String DATABASE = "academyc_tugas-pp2";
    private static final String USERNAME = "academyc_ruang-rasa-user";
    private static final String PASSWORD = "Ruangrasaunpas123";

    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE +
            "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static DatabaseConfig instance;
    private Connection connection;

    private DatabaseConfig() {
        // Private constructor for singleton
    }

    /**
     * Get the singleton instance of DatabaseConfig.
     * 
     * @return DatabaseConfig instance
     */
    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    /**
     * Get a connection to the database.
     * Creates a new connection if none exists or if the existing one is closed.
     * 
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            } catch (ClassNotFoundException e) {
                throw new SQLException("MySQL JDBC Driver not found", e);
            }
        }
        return connection;
    }

    /**
     * Close the database connection.
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }
}

package org.example.togetjob.connection;

import org.example.togetjob.config.ConfigLoaderBase;
import org.example.togetjob.exceptions.DatabaseException;
import org.example.togetjob.printer.Printer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    private static DatabaseConfig instance = null;
    private Connection connection;
    private ConfigLoaderBase loaderDaoConfig;
    private final Properties dbProperties = new Properties();

    // Flag to track if the connection message has already been printed
    private boolean connectionMessagePrinted = false;

    // Private constructor to prevent instantiation
    private DatabaseConfig() {
        loadDatabaseConfig();
    }

    // Method to load DB properties from db.properties file
    private void loadDatabaseConfig() throws DatabaseException {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                throw new DatabaseException("db.properties file not found.");
            }
            dbProperties.load(input);
        } catch (IOException e) {
            throw new DatabaseException("Error loading db.properties: " + e.getMessage());
        }
    }

    // Singleton pattern to ensure only one instance of DatabaseConfig
    public static synchronized DatabaseConfig getInstance() {
        if (instance == null) {
            instance = new DatabaseConfig();
        }
        return instance;
    }

    // Setter for the ConfigDaoLoader
    public synchronized void setConfigLoader(ConfigLoaderBase loader) {
        if (loader == null) {
            throw new IllegalArgumentException("ConfigDaoLoader cannot be null.");
        }
        this.loaderDaoConfig = loader;
    }

    // Method to get the database connection
    public synchronized Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            if (loaderDaoConfig == null) {
                throw new IllegalStateException("ConfigDaoLoader is not set.");
            }

            // Retrieve database credentials from properties file
            String dbUrl = dbProperties.getProperty("CONNECTION_URL");
            String dbUsername = dbProperties.getProperty("DB_USER");
            String dbPassword = dbProperties.getProperty("DB_PASSWORD");

            if (dbUrl == null || dbUsername == null || dbPassword == null) {
                throw new SQLException("Missing database properties!");
            }

            try {
                // Load the JDBC driver and establish the connection
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);

                // Print message only once, when the connection is established
                if (!connection.isClosed() && !connectionMessagePrinted) {
                    Printer.print("Connection to the database established.");
                    connectionMessagePrinted = true; // Set the flag so the message is not printed again
                }
            } catch (ClassNotFoundException e) {
                throw new SQLException("JDBC MySQL driver not found.", e);
            } catch (SQLException e) {
                Printer.print("Error during connection to DB: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    // Method to close the database connection
    public synchronized void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                Printer.print("Connection closed.");
            } catch (SQLException e) {
                Printer.print("Error closing connection: " + e.getMessage());
            } finally {
                connection = null;
                connectionMessagePrinted = false; // Reset the flag when the connection is closed
            }
        }
    }
}

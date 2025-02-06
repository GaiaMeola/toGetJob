package org.example.togetjob.connection;

import org.example.togetjob.config.ConfigDaoLoader;
import org.example.togetjob.printer.Printer;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {

    private static DatabaseConfig instance = null;
    private static Connection connection;
    private static ConfigDaoLoader loaderDaoConfig;
    private final Properties dbProperties = new Properties();

    private DatabaseConfig() {
        loadDatabaseConfig();
    }

    private void loadDatabaseConfig() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            dbProperties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Errore durante il caricamento di db.properties: " + e.getMessage());
        }
    }


    public static synchronized DatabaseConfig getInstance() {

        if (instance == null) {
            instance = new DatabaseConfig();
        }

        return instance;

    }

    public void setConfigLoader(ConfigDaoLoader loader) {
        if (loader == null) {
            throw new IllegalArgumentException("ConfigDaoLoader can't be null.");
        }
        loaderDaoConfig = loader;
    }

    public synchronized Connection getConnection() throws SQLException {
        if (connection == null) {
            if (loaderDaoConfig == null) {
                throw new IllegalStateException(" ConfigDaoLoader not set.");
            }

            String dbUrl = dbProperties.getProperty("CONNECTION_URL");
            String dbUsername = dbProperties.getProperty("DB_USER");
            String dbPassword = dbProperties.getProperty("DB_PASSWORD");

            if (dbUrl == null || dbUsername == null) {
                throw new SQLException("Error in db properties !");
            }

            try {
                // driver JDBC
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                Printer.print("Connection to db done !");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver JDBC MySQL not found", e);
            } catch (SQLException e) {
                Printer.print("Error during connection to db: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                Printer.print("Connection closed.");
            } catch (SQLException e) {
                Printer.print("Error during connection: " + e.getMessage());
            } finally {
                connection = null;
            }
        }
    }
}

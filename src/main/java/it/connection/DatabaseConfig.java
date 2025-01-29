package it.connection;

import it.config.ConfigDaoLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static DatabaseConfig instance = null;
    private Connection connection;
    private static ConfigDaoLoader loaderDaoConfig;

    private DatabaseConfig() {
    }

    public static synchronized DatabaseConfig getInstance() {

        if (instance == null) {
            instance = new DatabaseConfig();
        }

        return instance;

    }

    public void setConfigLoader(ConfigDaoLoader loader) {
        if (loader == null) {
            throw new IllegalArgumentException("Il loader di configurazione non può essere null.");
        }
        loaderDaoConfig = loader;
    }

    public synchronized Connection getConnection() throws SQLException {
        if (connection == null) {
            if (loaderDaoConfig == null) {
                throw new IllegalStateException("Il ConfigDaoLoader non è stato impostato.");
            }

            String dbUrl = loaderDaoConfig.getDbUrl();
            String dbUsername = loaderDaoConfig.getDbUsername();
            String dbPassword = loaderDaoConfig.getDbPassword();

            try {
                // Carica il driver JDBC se necessario
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(dbUrl, dbUsername, dbPassword);
                System.out.println("Connessione al database riuscita!");
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver JDBC MySQL non trovato", e);
            } catch (SQLException e) {
                System.err.println("Errore durante la connessione al database: " + e.getMessage());
                throw e; // Propaga l'errore all'esterno
            }
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connessione chiusa con successo.");
            } catch (SQLException e) {
                System.err.println("Errore durante la chiusura della connessione: " + e.getMessage());
            } finally {
                connection = null; // Resetta la connessione dopo la chiusura
            }
        }
    }
}

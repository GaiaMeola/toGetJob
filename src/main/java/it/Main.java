package it;

import it.config.ConfigDaoLoader;
import it.config.ConfigUILoader;
import it.connection.DatabaseConfig;
import it.exceptions.ConfigException;
import it.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws RuntimeException, IOException {
        ConfigDaoLoader loaderDaoConfig;
        ConfigUILoader loaderUIConfig;

        try {
            loaderDaoConfig = new ConfigDaoLoader("dao.config.properties");
        } catch (ConfigException e) {
            System.out.println("Errore nella configurazione DAO: " + e.getMessage());
            return;
        }
        String daoType = loaderDaoConfig.getProperty("dao.type");
        System.out.println("Tipo di DAO configurato: " + daoType);

        AbstractFactoryDaoSingleton.setConfigLoader(loaderDaoConfig);
        AbstractFactoryDaoSingleton factory = AbstractFactoryDaoSingleton.getFactoryDao();

        try {
            loaderUIConfig = new ConfigUILoader("ui.config.properties");
        } catch (ConfigException e) {
            System.err.println("Errore nella configurazione UI: " + e.getMessage());
            return;
        }

        String uiType = loaderUIConfig.getProperty("ui.type");
        System.out.println("Tipo di interfaccia utente configurata: " + uiType);


        if ("jdbc".equalsIgnoreCase(daoType)) {
            DatabaseConfig databaseConfig = DatabaseConfig.getInstance();
            databaseConfig.setConfigLoader(loaderDaoConfig);

            try {
                Connection connection = databaseConfig.getConnection();
                if (connection != null) {
                    System.out.println("Connessione al database riuscita.");

                    // Esegui le operazioni sul database qui...

                    // Alla fine, chiudi la connessione
                    databaseConfig.closeConnection();  // Chiudi la connessione
                    System.out.println("Connessione al database chiusa.");
                } else {
                    System.out.println("Impossibile ottenere la connessione al database.");
                }
            } catch (SQLException e) {
                System.out.println("Errore durante la connessione al database: " + e.getMessage());
                return;
            }
        } else if ("in memory".equalsIgnoreCase(daoType)) {
            // Configurazione in-memory: Non serve connettersi al database
            System.out.println("DAO In-Memory configurato.");
        } else if ("json".equalsIgnoreCase(daoType)) {
            // Configurazione FileSystem (esempio con json): Non serve connettersi al database
            System.out.println("DAO FileSystem configurato.");
        } else {
            System.out.println("Tipo di DAO non riconosciuto.");
        }

    }
}
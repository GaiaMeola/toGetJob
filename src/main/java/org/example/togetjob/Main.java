package org.example.togetjob;

import org.example.togetjob.config.ConfigDaoLoader;
import org.example.togetjob.config.ConfigUILoader;
import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.exceptions.ConfigException;
import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.view.cli.concretestate.MainMenuState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws RuntimeException {

        ConfigDaoLoader loaderDaoConfig;
        ConfigUILoader loaderUIConfig;

        try {
            loaderDaoConfig = new ConfigDaoLoader("dao.config.properties");
        } catch (ConfigException e) {
            logger.severe("Errore nella configurazione DAO: " + e.getMessage());
            return;
        }
        String daoType = loaderDaoConfig.getProperty("dao.type");
        logger.info("Tipo di DAO configurato: " + daoType);

        AbstractFactoryDaoSingleton.setConfigLoader(loaderDaoConfig);

        try {
            loaderUIConfig = new ConfigUILoader("ui.config.properties");
        } catch (ConfigException e) {
            logger.severe("Errore nella configurazione UI: " + e.getMessage());
            return;
        }

        String uiType = loaderUIConfig.getProperty("ui.type");
        logger.info("Tipo di interfaccia utente configurata: " + uiType);

        if ("jdbc".equalsIgnoreCase(daoType)) {
            DatabaseConfig databaseConfig = DatabaseConfig.getInstance();
            databaseConfig.setConfigLoader(loaderDaoConfig);

            try {
                Connection connection = databaseConfig.getConnection();
                if (connection != null) {
                    logger.info("Connessione al database riuscita.");
                    databaseConfig.closeConnection(); //Connection closed
                    logger.info("Connessione al database chiusa.");
                } else {
                    logger.warning("Impossibile ottenere la connessione al database.");
                }
            } catch (SQLException e) {
                logger.severe("Errore durante la connessione al database: " + e.getMessage());
                return;
            }
        } else if ("in memory".equalsIgnoreCase(daoType)) {
            //In memory
            logger.info("DAO In-Memory configurato.");
        } else if ("json".equalsIgnoreCase(daoType)) {
            //file system
            logger.info("DAO FileSystem configurato.");
        } else {
            logger.warning("Tipo di DAO non riconosciuto.");
        }

        if ("cli".equalsIgnoreCase(uiType)) {
            CliContext context = new CliContext(new MainMenuState());
            context.startCLI();
        } else if ("gui".equalsIgnoreCase(uiType)) {
            launchGui();
        } else {
            logger.warning("Tipo di interfaccia non riconosciuto.");
        }

    }

    private static void launchGui(){
        //GUI
        logger.info("Launching GUI...");
    }

}

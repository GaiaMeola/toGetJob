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

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) throws RuntimeException {
        ConfigDaoLoader loaderDaoConfig;
        ConfigUILoader loaderUIConfig;

        try {
            loaderDaoConfig = new ConfigDaoLoader("dao.config.properties");
        } catch (ConfigException e) {
            logger.severe("Error DAO Configuration: " + e.getMessage());
            return;
        }
        String daoType = loaderDaoConfig.getProperty("dao.type");
        logger.info("Type of DAO: " + daoType);

        AbstractFactoryDaoSingleton.setConfigLoader(loaderDaoConfig);

        try {
            loaderUIConfig = new ConfigUILoader("ui.config.properties");
        } catch (ConfigException e) {
            logger.severe("Error UI Configuration: " + e.getMessage());
            return;
        }

        String uiType = loaderUIConfig.getProperty("ui.type");
        logger.info("Type of UI: " + uiType);

        if ("jdbc".equalsIgnoreCase(daoType)) {
            DatabaseConfig databaseConfig = DatabaseConfig.getInstance();
            databaseConfig.setConfigLoader(loaderDaoConfig);

            try {
                Connection connection = databaseConfig.getConnection();
                if (connection != null) {
                    logger.info("Connection established.");
                    databaseConfig.closeConnection(); //Connection closed
                    logger.info("Connection closed.");
                } else {
                    logger.severe("Error Connection.");
                }
            } catch (SQLException e) {
                logger.severe("Error Connection: " + e.getMessage());
                return;
            }
        } else if ("in memory".equalsIgnoreCase(daoType)) {
            //In memory
            logger.info("DAO In-Memory");
        } else if ("json".equalsIgnoreCase(daoType)) {
            //file system
            logger.info("DAO FileSystem");
        } else {
            logger.severe("DAO DataBase.");
        }

        if ("cli".equalsIgnoreCase(uiType)) {
            CliContext context = new CliContext(new MainMenuState());
            context.startCLI();
        } else if ("gui".equalsIgnoreCase(uiType)) {
            launchGui();
        } else {
            logger.severe("UI not found");
        }
    }

    private static void launchGui(){
        //GUI
        logger.info("Launching GUI...");
    }
}

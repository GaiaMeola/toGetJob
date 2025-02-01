package org.example.togetjob;

import org.example.togetjob.config.ConfigDaoLoader;
import org.example.togetjob.config.ConfigUILoader;
import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.exceptions.ConfigException;
import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.view.cli.concretestate.MainMenuState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws RuntimeException {
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
                    databaseConfig.closeConnection(); //Connection closed
                    System.out.println("Connessione al database chiusa.");
                } else {
                    System.out.println("Impossibile ottenere la connessione al database.");
                }
            } catch (SQLException e) {
                System.out.println("Errore durante la connessione al database: " + e.getMessage());
                return;
            }
        } else if ("in memory".equalsIgnoreCase(daoType)) {
            //In memory
            System.out.println("DAO In-Memory configurato.");
        } else if ("json".equalsIgnoreCase(daoType)) {
           //file system
            System.out.println("DAO FileSystem configurato.");
        } else {
            System.out.println("Tipo di DAO non riconosciuto.");
        }
        if ("cli".equalsIgnoreCase(uiType)){
            CliContext context = new CliContext(new MainMenuState());
            context.startCLI();
        } else if("gui".equalsIgnoreCase(uiType)){
            launchGui();
        } else{
            System.out.println("Tipo di interfaccia non riconosciuto.");
        }

    }

    private static void launchGui(){
       //GUI
        System.out.println("Launching GUI...");
    }

}
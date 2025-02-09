package org.example.togetjob;

import org.example.togetjob.config.ConfigDaoLoader;
import org.example.togetjob.config.ConfigUILoader;
import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.exceptions.ConfigException;
import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.cli.concretestate.MainMenuState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.sql.Connection;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    public static void main(String[] args) throws RuntimeException {

        ConfigDaoLoader loaderDaoConfig;
        ConfigUILoader loaderUIConfig;

        //CONFIGURATION DAO
        try {
            loaderDaoConfig = new ConfigDaoLoader("dao.config.properties");
        } catch (ConfigException e) {
            Printer.print("Error DAO Configuration: " + e.getMessage());
            return;
        }
        String daoType = loaderDaoConfig.getProperty("dao.type");
        Printer.print("Type of DAO: " + daoType);

        AbstractFactoryDaoSingleton.setConfigLoader(loaderDaoConfig);

        //CONFIGURATION UI
        try {
            loaderUIConfig = new ConfigUILoader("ui.config.properties");
        } catch (ConfigException e) {
           Printer.print("Error UI Configuration: " + e.getMessage());
            return;
        }

        String uiType = loaderUIConfig.getProperty("ui.type");
        Printer.print("Type of UI: " + uiType);


        if ("jdbc".equalsIgnoreCase(daoType)) {
            DatabaseConfig databaseConfig = DatabaseConfig.getInstance();
            databaseConfig.setConfigLoader(loaderDaoConfig);

            try {
                Connection connection = databaseConfig.getConnection();
                if (connection != null) {
                    Printer.print("Connection ...");
                } else {
                    Printer.print("Error Connection.");
                }
            } catch (SQLException e) {
                Printer.print("Error Connection: " + e.getMessage());
                return;
            }
        } else if ("in memory".equalsIgnoreCase(daoType)) {
            //In memory
            Printer.print("DAO In-Memory");
        } else if ("file system".equalsIgnoreCase(daoType)) {
           //file system
            Printer.print("DAO FileSystem");
        } else {
            Printer.print("DAO not found");
        }


        if ("cli".equalsIgnoreCase(uiType)){
            CliContext context = new CliContext(new MainMenuState());
            context.startCLI();
        } else if("gui".equalsIgnoreCase(uiType)){
            launchGui();
        } else{
            Printer.print("UI not found");
        }

    }

    private static void launchGui(){
       //GUI
        Printer.print("Launching GUI...");
    }

}
package it;

import it.config.ConfigDaoLoader;
import it.config.ConfigUILoader;
import it.exceptions.ConfigException;
import it.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws RuntimeException, IOException {
        ConfigDaoLoader loaderDaoConfig;
        ConfigUILoader loaderUIConfig;

        String DB_URL = "jdbc:mysql://localhost:3306/ISPW";
        String DB_USER = "root";
        String DB_PASSWORD = "";

        try (
                Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connessione riuscita!");
        } catch (
                SQLException e) {
            System.out.println("Errore di connessione: " + e.getMessage());
        }




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
    }

}
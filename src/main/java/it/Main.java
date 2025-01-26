package it;

import it.Config.ConfigDaoLoader;
import it.Config.ConfigUILoader;
import it.Exceptions.ConfigException;

import java.io.IOException;

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

        factoryDao.setConfigLoader(loaderDaoConfig);

        try {
            loaderUIConfig = new ConfigUILoader("ui.config.properties");
        } catch (ConfigException e) {
            System.out.println("Errore nella configurazione UI: " + e.getMessage());
            return;
        }

        String uiType = loaderUIConfig.getProperty("ui.type");
        System.out.println("Tipo di interfaccia utente configurata: " + uiType);
    }

}
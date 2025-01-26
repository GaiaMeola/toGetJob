package it;

import it.config.ConfigDaoLoader;
import it.config.ConfigUILoader;
import it.exceptions.ConfigException;
import it.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;

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
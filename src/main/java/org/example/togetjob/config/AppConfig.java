package org.example.togetjob.config;

import org.example.togetjob.exceptions.ConfigException;
import org.example.togetjob.dao.abstractfactorydao.AbstractFactoryDaoSingleton;

public class AppConfig {
    private static ConfigDaoLoader daoConfig;
    private static ConfigUILoader uiConfig;

    private AppConfig(){
        /**/
    }

    public static void loadConfigs() throws ConfigException {
        daoConfig = new ConfigDaoLoader("dao.config.properties");
        uiConfig = new ConfigUILoader("ui.config.properties");
        AbstractFactoryDaoSingleton.setConfigLoader(daoConfig);
    }

    public static String getDaoType() {
        return daoConfig.getProperty("dao.type");
    }

    public static String getUiType() {
        return uiConfig.getProperty("ui.type");
    }

    public static ConfigDaoLoader getDaoConfig() {
        return daoConfig;
    }
}

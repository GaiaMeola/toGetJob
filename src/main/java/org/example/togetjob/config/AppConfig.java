package org.example.togetjob.config;

import org.example.togetjob.exceptions.ConfigException;
import org.example.togetjob.dao.abstractfactorydao.AbstractFactoryDaoSingleton;

public class AppConfig {

    private static ConfigLoaderBase daoConfig;
    private static ConfigLoaderBase uiConfig;

    private AppConfig(){
        /**/
    }

    public static void loadConfigs() throws ConfigException {
        daoConfig = new ConfigLoaderBase("dao.config.properties");
        uiConfig = new ConfigLoaderBase("ui.config.properties");
        AbstractFactoryDaoSingleton.setConfigLoader(daoConfig);
    }

    public static String getDaoType() {
        return daoConfig.getProperty("dao.type");
    }

    public static String getUiType() {
        return uiConfig.getProperty("ui.type");
    }

    public static ConfigLoaderBase getDaoConfig() {
        return daoConfig;
    }
}

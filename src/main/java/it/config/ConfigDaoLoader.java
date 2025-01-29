package it.config;

import it.exceptions.ConfigException;

public class ConfigDaoLoader extends ConfigLoaderBase {

    public ConfigDaoLoader(String configFilePath) throws ConfigException {
        super(configFilePath);
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }

    //Metodi per il tipo di DAO "inmemory"

    //Metodi per il tipo di DAO "filesystem"

    //Metodi per il tipo di DAO "jdbc"

    public String getDbUrl(){
        return getProperty("CONNECTION_URL");
    }

    public String getDbUsername(){
        return getProperty("DB_USER");
    }

    public String getDbPassword(){
        return getProperty("DB_PASSWORD");
    }



}

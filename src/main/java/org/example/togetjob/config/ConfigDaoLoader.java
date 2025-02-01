package org.example.togetjob.config;

import org.example.togetjob.exceptions.ConfigException;

public class ConfigDaoLoader extends ConfigLoaderBase {

    public ConfigDaoLoader(String configFilePath) throws ConfigException {
        super(configFilePath);
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }

}

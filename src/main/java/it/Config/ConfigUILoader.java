package it.Config;

import it.Exceptions.ConfigException;

public class ConfigUILoader extends ConfigLoaderBase {

    public ConfigUILoader(String configFilePath) throws ConfigException {
        super(configFilePath);
    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }

}

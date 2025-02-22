package org.example.togetjob.config;

import org.example.togetjob.exceptions.ConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigLoaderBase {

    protected final Properties properties;

    protected ConfigLoaderBase(String configFilePath) throws ConfigException {

        properties = new Properties();

        try(InputStream input = getClass().getClassLoader().getResourceAsStream(configFilePath)){

            if(input == null){
                throw new ConfigException("File not found: " + configFilePath);
            }

            properties.load(input);

        } catch(IOException e){

            throw new ConfigException("Error during the configuration: " + configFilePath, e);

        }

    }

    public String getProperty(String key){
        return properties.getProperty(key);
    }

}

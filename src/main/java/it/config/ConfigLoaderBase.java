package it.config;

import it.exceptions.ConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class ConfigLoaderBase {

    protected final Properties properties;

    protected ConfigLoaderBase(String configFilePath) throws ConfigException {

        properties = new Properties();

        try(InputStream input = getClass().getClassLoader().getResourceAsStream(configFilePath)){

            if(input == null){
                throw new ConfigException("File di configurazione non trovato: " + configFilePath);
            }

            properties.load(input);

        } catch(IOException e){

            throw new ConfigException("Errore durante il caricamento del file di configurazione: " + configFilePath, e);

        }

    }

    public abstract String getProperty(String key);

}

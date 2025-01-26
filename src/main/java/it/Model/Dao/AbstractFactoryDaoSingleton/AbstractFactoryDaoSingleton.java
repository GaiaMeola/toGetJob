package it.Model.Dao.AbstractFactoryDaoSingleton;

import it.Config.ConfigDaoLoader;

public abstract class AbstractFactoryDaoSingleton {

    private static AbstractFactoryDaoSingleton factoryDao = null;

    private static ConfigDaoLoader configLoader;


    protected AbstractFactoryDaoSingleton() {
    }

    public static synchronized AbstractFactoryDaoSingleton getFactoryDao(){

       if(factoryDao == null){

           if(configLoader ==  null){
               throw new IllegalStateException("ConfigDaoLoader non è stato impostato.");
           }

           String dao_type = configLoader.getProperty("dao.type");

           switch (dao_type){

               case "inmemory":

                   factoryDao = new;
                   break;


           }



        }

    }

    public static void setConfigLoader(ConfigDaoLoader loader){
        configLoader = loader;
    }

}
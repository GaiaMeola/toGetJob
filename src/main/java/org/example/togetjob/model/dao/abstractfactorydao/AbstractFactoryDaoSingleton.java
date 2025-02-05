package org.example.togetjob.model.dao.abstractfactorydao;

import org.example.togetjob.config.ConfigDaoLoader;
import org.example.togetjob.model.dao.abstractobjects.*;
import org.example.togetjob.model.dao.concretefactorydao.DataBaseFactoryDao;
import org.example.togetjob.model.dao.concretefactorydao.FileSystemFactoryDao;
import org.example.togetjob.model.dao.concretefactorydao.InMemoryFactoryDao;

public abstract class
AbstractFactoryDaoSingleton {

    private static AbstractFactoryDaoSingleton factoryDao = null;
    private static ConfigDaoLoader configLoader;

    public static void setConfigLoader(ConfigDaoLoader loader){
        if(loader == null){
            throw new IllegalArgumentException("Error.");
        }
        configLoader = loader;
    }

    protected AbstractFactoryDaoSingleton() {
    }

    public static synchronized AbstractFactoryDaoSingleton getFactoryDao(){

       if(factoryDao == null){
           if(configLoader ==  null){
               throw new IllegalStateException("Error.");
           }

           String daoType = configLoader.getProperty("dao.type");
           if(daoType == null || daoType.isEmpty()){
               throw new IllegalArgumentException("Type of DAO not found.");
           }

           switch (daoType.toLowerCase()){

               case "in memory":
                   factoryDao = new InMemoryFactoryDao();
                   break;
               case "jdbc":
                   factoryDao = new DataBaseFactoryDao();
                   break;
               case "json":
                   factoryDao = new FileSystemFactoryDao();
                   break;
               default:
                   throw new IllegalArgumentException("Type of DAO not found" + daoType);
           }

        }

        return factoryDao;

    }

    public abstract UserDao createUserDao();
    public abstract JobAnnouncementDao createJobAnnouncementDao();
    public abstract StudentDao createStudentDao();
    public abstract RecruiterDao createRecruiterDao();
    public abstract JobApplicationDao createJobApplicationDao();
}
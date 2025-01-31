package it.model.dao.abstractfactorydao;

import it.config.ConfigDaoLoader;
import it.model.dao.abstractobjects.JobAnnouncementDao;
import it.model.dao.abstractobjects.RecruiterDao;
import it.model.dao.abstractobjects.StudentDao;
import it.model.dao.abstractobjects.UserDao;
import it.model.dao.concretefactorydao.DataBaseFactoryDao;
import it.model.dao.concretefactorydao.FileSystemFactoryDao;
import it.model.dao.concretefactorydao.InMemoryFactoryDao;

public abstract class AbstractFactoryDaoSingleton {

    private static AbstractFactoryDaoSingleton factoryDao = null;
    private static ConfigDaoLoader configLoader;

    public static void setConfigLoader(ConfigDaoLoader loader){
        if(loader == null){
            throw new IllegalArgumentException("Il ConfigDaoLoader non può essere null. ");
        }
        configLoader = loader;
    }

    protected AbstractFactoryDaoSingleton() {
    }

    public static synchronized AbstractFactoryDaoSingleton getFactoryDao(){

       if(factoryDao == null){
           if(configLoader ==  null){
               throw new IllegalStateException("ConfigDaoLoader non è stato impostato.");
           }

           String daoType = configLoader.getProperty("dao.type");
           if(daoType == null || daoType.isEmpty()){
               throw new IllegalArgumentException("Il tipo di DAO non è specificato nella configurazione.");
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
                   throw new IllegalArgumentException("Tipo di Dao non valido: " + daoType);
           }

        }

        return factoryDao;

    }

    public abstract UserDao createUserDao();
    public abstract JobAnnouncementDao createJobAnnouncementDao();
    public abstract StudentDao createStudentDao();
    public abstract RecruiterDao createRecruiterDao();
}
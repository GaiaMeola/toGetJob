package it.model.dao.concretefactorydao;

import it.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import it.model.dao.abstractobjects.PersonDao;
import it.model.dao.abstractobjects.UserDao;
import it.model.dao.concreteobjects.FileSystemPersonDao;
import it.model.dao.concreteobjects.FileSystemUserDao;

public class FileSystemFactoryDao extends AbstractFactoryDaoSingleton {

    @Override
    public UserDao createUserDao() {
        return new FileSystemUserDao();
    }

    @Override
    public PersonDao createPersonDao() {
        return new FileSystemPersonDao();
    }
}

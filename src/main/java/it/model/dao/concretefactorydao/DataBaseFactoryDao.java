package it.model.dao.concretefactorydao;

import it.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import it.model.dao.abstractobjects.PersonDao;
import it.model.dao.abstractobjects.UserDao;
import it.model.dao.concreteobjects.DataBasePersonDao;
import it.model.dao.concreteobjects.DataBaseUserDao;

public class DataBaseFactoryDao extends AbstractFactoryDaoSingleton {
    @Override
    public UserDao createUserDao() {
        return new DataBaseUserDao();
    }

    @Override
    public PersonDao createPersonDao() {
        return new DataBasePersonDao();
    }
}

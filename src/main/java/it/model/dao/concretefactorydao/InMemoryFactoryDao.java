package it.model.dao.concretefactorydao;

import it.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import it.model.dao.abstractobjects.PersonDao;
import it.model.dao.abstractobjects.UserDao;
import it.model.dao.concreteobjects.InMemoryPersonDao;
import it.model.dao.concreteobjects.InMemoryUserDao;

public class InMemoryFactoryDao extends AbstractFactoryDaoSingleton {
    @Override
    public UserDao createUserDao() {
        return new InMemoryUserDao();
    }

    @Override
    public PersonDao createPersonDao() {
        return new InMemoryPersonDao();
    }
}

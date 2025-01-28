package it.model.dao.concretefactorydao;

import it.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import it.model.dao.abstractobjects.JobAnnouncementDao;
import it.model.dao.abstractobjects.StudentDao;
import it.model.dao.abstractobjects.UserDao;
import it.model.dao.concreteobjects.InMemoryJobAnnouncementDao;
import it.model.dao.concreteobjects.InMemoryUserDao;

public class InMemoryFactoryDao extends AbstractFactoryDaoSingleton {
    @Override
    public UserDao createUserDao() {
        return new InMemoryUserDao();
    }

    @Override
    public JobAnnouncementDao createJobAnnouncementDao() {
        return new InMemoryJobAnnouncementDao();
    }

    @Override
    public StudentDao createStudentDao() {
        return null;
    }
}

package it.model.dao.concretefactorydao;

import it.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import it.model.dao.abstractobjects.JobAnnouncementDao;
import it.model.dao.abstractobjects.RecruiterDao;
import it.model.dao.abstractobjects.StudentDao;
import it.model.dao.abstractobjects.UserDao;
import it.model.dao.concreteobjects.InMemoryJobAnnouncementDao;
import it.model.dao.concreteobjects.InMemoryRecruiterDao;
import it.model.dao.concreteobjects.InMemoryStudentDao;
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
        return new InMemoryStudentDao();
    }

    @Override
    public RecruiterDao createRecruiterDao() {
        return new InMemoryRecruiterDao();
    }
}

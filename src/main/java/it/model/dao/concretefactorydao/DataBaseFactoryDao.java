package it.model.dao.concretefactorydao;

import it.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import it.model.dao.abstractobjects.JobAnnouncementDao;
import it.model.dao.abstractobjects.StudentDao;
import it.model.dao.abstractobjects.UserDao;
import it.model.dao.concreteobjects.DataBaseJobAnnouncementDao;
import it.model.dao.concreteobjects.DataBaseUserDao;

public class DataBaseFactoryDao extends AbstractFactoryDaoSingleton {
    @Override
    public UserDao createUserDao() {
        return new DataBaseUserDao();
    }

    @Override
    public JobAnnouncementDao createJobAnnouncementDao() {
        return new DataBaseJobAnnouncementDao();
    }

    @Override
    public StudentDao createStudentDao() {
        return null;
    }

}

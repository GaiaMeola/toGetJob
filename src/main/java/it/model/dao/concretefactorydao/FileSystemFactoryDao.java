package it.model.dao.concretefactorydao;

import it.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import it.model.dao.abstractobjects.JobAnnouncementDao;
import it.model.dao.abstractobjects.RecruiterDao;
import it.model.dao.abstractobjects.StudentDao;
import it.model.dao.abstractobjects.UserDao;
import it.model.dao.concreteobjects.FileSystemJobAnnouncementDao;
import it.model.dao.concreteobjects.FileSystemRecruiterDao;
import it.model.dao.concreteobjects.FileSystemStudentDao;
import it.model.dao.concreteobjects.FileSystemUserDao;

public class FileSystemFactoryDao extends AbstractFactoryDaoSingleton {

    @Override
    public UserDao createUserDao() {
        return new FileSystemUserDao();
    }

    @Override
    public JobAnnouncementDao createJobAnnouncementDao() {
        return new FileSystemJobAnnouncementDao();
    }

    @Override
    public StudentDao createStudentDao() {
        return new FileSystemStudentDao();
    }

    @Override
    public RecruiterDao createRecruiterDao() {
        return new FileSystemRecruiterDao();
    }

}

package org.example.togetjob.model.dao.concretefactorydao;

import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.dao.abstractobjects.StudentDao;
import org.example.togetjob.model.dao.abstractobjects.UserDao;
import org.example.togetjob.model.dao.concreteobjects.DataBaseJobAnnouncementDao;
import org.example.togetjob.model.dao.concreteobjects.DataBaseRecruiterDao;
import org.example.togetjob.model.dao.concreteobjects.DataBaseStudentDao;
import org.example.togetjob.model.dao.concreteobjects.DataBaseUserDao;

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
        return new DataBaseStudentDao();
    }

    @Override
    public RecruiterDao createRecruiterDao() {
        return new DataBaseRecruiterDao();
    }

}

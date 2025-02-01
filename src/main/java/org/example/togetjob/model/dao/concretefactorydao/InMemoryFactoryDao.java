package org.example.togetjob.model.dao.concretefactorydao;

import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.dao.abstractobjects.StudentDao;
import org.example.togetjob.model.dao.abstractobjects.UserDao;
import org.example.togetjob.model.dao.concreteobjects.InMemoryJobAnnouncementDao;
import org.example.togetjob.model.dao.concreteobjects.InMemoryRecruiterDao;
import org.example.togetjob.model.dao.concreteobjects.InMemoryStudentDao;
import org.example.togetjob.model.dao.concreteobjects.InMemoryUserDao;

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

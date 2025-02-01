package org.example.togetjob.model.dao.concretefactorydao;

import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.dao.abstractobjects.StudentDao;
import org.example.togetjob.model.dao.abstractobjects.UserDao;
import org.example.togetjob.model.dao.concreteobjects.FileSystemJobAnnouncementDao;
import org.example.togetjob.model.dao.concreteobjects.FileSystemRecruiterDao;
import org.example.togetjob.model.dao.concreteobjects.FileSystemStudentDao;
import org.example.togetjob.model.dao.concreteobjects.FileSystemUserDao;

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

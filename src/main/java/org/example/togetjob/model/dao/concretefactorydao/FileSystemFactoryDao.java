package org.example.togetjob.model.dao.concretefactorydao;

import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.*;
import org.example.togetjob.model.dao.concreteobjects.*;

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

    @Override
    public JobApplicationDao createJobApplicationDao() {return new FileSystemJobApplicationDao();}

}

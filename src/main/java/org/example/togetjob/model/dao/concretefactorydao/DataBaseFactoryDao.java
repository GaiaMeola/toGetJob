package org.example.togetjob.model.dao.concretefactorydao;

import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.*;
import org.example.togetjob.model.dao.concreteobjects.*;

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

    @Override
    public JobApplicationDao createJobApplicationDao() {return new DataBaseJobApplicationDao();}


}

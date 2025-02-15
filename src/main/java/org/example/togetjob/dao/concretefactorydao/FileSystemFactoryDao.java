package org.example.togetjob.dao.concretefactorydao;

import org.example.togetjob.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.dao.abstractobjects.*;
import org.example.togetjob.dao.concreteobjects.*;

public class FileSystemFactoryDao extends AbstractFactoryDaoSingleton {

    @Override
    public UserDao createUserDao() { return new FileSystemUserDao(); }

    @Override
    public JobAnnouncementDao createJobAnnouncementDao() { return new FileSystemJobAnnouncementDao(); }

    @Override
    public StudentDao createStudentDao() { return new FileSystemStudentDao(); }

    @Override
    public RecruiterDao createRecruiterDao() { return new FileSystemRecruiterDao(); }

    @Override
    public JobApplicationDao createJobApplicationDao() { return new FileSystemJobApplicationDao(); }

    @Override
    public InterviewSchedulingDao createInterviewSchedulingDao() { return new FileSystemInterviewSchedulingDao(); }

}
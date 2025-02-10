package org.example.togetjob.model.dao.concretefactorydao;

import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.*;
import org.example.togetjob.model.dao.concreteobjects.*;

public class InMemoryFactoryDao extends AbstractFactoryDaoSingleton {

    @Override
    public UserDao createUserDao() { return new InMemoryUserDao(); }

    @Override
    public JobAnnouncementDao createJobAnnouncementDao() { return new InMemoryJobAnnouncementDao(); }

    @Override
    public StudentDao createStudentDao() { return new InMemoryStudentDao(); }

    @Override
    public RecruiterDao createRecruiterDao() { return new InMemoryRecruiterDao(); }

    @Override
    public JobApplicationDao createJobApplicationDao() { return new InMemoryJobApplicationDao(); }

    @Override
    public InterviewSchedulingDao createInterviewSchedulingDao() { return new InMemoryInterviewSchedulingDao(); }
}
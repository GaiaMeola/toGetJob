package org.example.togetjob.model.dao.concretefactorydao;

import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.*;
import org.example.togetjob.model.dao.concreteobjects.*;

public class InMemoryFactoryDao extends AbstractFactoryDaoSingleton {
    private final UserDao userDao = new InMemoryUserDao();
    private final JobAnnouncementDao jobAnnouncementDao = new InMemoryJobAnnouncementDao();
    private final StudentDao studentDao = new InMemoryStudentDao();
    private final RecruiterDao recruiterDao = new InMemoryRecruiterDao();
    private final JobApplicationDao jobApplicationDao = new InMemoryJobApplicationDao();
    private final InterviewSchedulingDao interviewSchedulingDao = new InMemoryInterviewSchedulingDao();

    @Override
    public UserDao createUserDao() { return userDao; }
    //NOSONAR

    @Override
    public JobAnnouncementDao createJobAnnouncementDao() { return jobAnnouncementDao; }
    //NOSONAR

    @Override
    public StudentDao createStudentDao() { return studentDao; }
    //NOSONAR

    @Override
    public RecruiterDao createRecruiterDao() { return recruiterDao; }
    //NOSONAR

    @Override
    public JobApplicationDao createJobApplicationDao() { return jobApplicationDao; }
    //NOSONAR

    @Override
    public InterviewSchedulingDao createInterviewSchedulingDao() { return interviewSchedulingDao; }
    //NOSONAR
}
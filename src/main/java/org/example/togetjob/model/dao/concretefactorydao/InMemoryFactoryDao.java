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

    @Override
    public JobAnnouncementDao createJobAnnouncementDao() { return jobAnnouncementDao; }

    @Override
    public StudentDao createStudentDao() { return studentDao; }

    @Override
    public RecruiterDao createRecruiterDao() { return recruiterDao; }

    @Override
    public JobApplicationDao createJobApplicationDao() { return jobApplicationDao; }

    @Override
    public InterviewSchedulingDao createInterviewSchedulingDao() { return interviewSchedulingDao; }
}
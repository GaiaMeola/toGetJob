package org.example.togetjob.model.dao.concretefactorydao;

import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.*;
import org.example.togetjob.model.dao.concreteobjects.*;

public class DataBaseFactoryDao extends AbstractFactoryDaoSingleton {
    private final UserDao userDao = new DataBaseUserDao();
    private final JobAnnouncementDao jobAnnouncementDao = new DataBaseJobAnnouncementDao();
    private final StudentDao studentDao = new DataBaseStudentDao();
    private final RecruiterDao recruiterDao = new DataBaseRecruiterDao();
    private final JobApplicationDao jobApplicationDao = new DataBaseJobApplicationDao();
    private final InterviewSchedulingDao interviewSchedulingDao = new DataBaseInterviewSchedulingDao();

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

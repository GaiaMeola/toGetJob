package org.example.togetjob.model.dao.concretefactorydao;

import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.*;
import org.example.togetjob.model.dao.concreteobjects.*;

public class FileSystemFactoryDao extends AbstractFactoryDaoSingleton {
    private final UserDao userDao = new FileSystemUserDao();
    private final JobAnnouncementDao jobAnnouncementDao = new FileSystemJobAnnouncementDao();
    private final StudentDao studentDao = new FileSystemStudentDao();
    private final RecruiterDao recruiterDao = new FileSystemRecruiterDao();
    private final JobApplicationDao jobApplicationDao = new FileSystemJobApplicationDao();
    private final InterviewSchedulingDao interviewSchedulingDao = new FileSystemInterviewSchedulingDao();

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

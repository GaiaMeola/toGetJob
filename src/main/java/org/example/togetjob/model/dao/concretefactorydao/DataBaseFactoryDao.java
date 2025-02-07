package org.example.togetjob.model.dao.concretefactorydao;

import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.*;
import org.example.togetjob.model.dao.concreteobjects.*;

public class DataBaseFactoryDao extends AbstractFactoryDaoSingleton {

    private final DataBaseUserDao userDao;
    private final DataBaseJobAnnouncementDao jobAnnouncementDao;
    private final DataBaseStudentDao studentDao;
    private final DataBaseRecruiterDao recruiterDao;
    private final DataBaseJobApplicationDao jobApplicationDao;
    private final DataBaseInterviewSchedulingDao interviewSchedulingDao;

    public DataBaseFactoryDao() {
        this.userDao = new DataBaseUserDao();
        this.recruiterDao = new DataBaseRecruiterDao();
        this.studentDao = new DataBaseStudentDao();
        this.jobAnnouncementDao = new DataBaseJobAnnouncementDao(recruiterDao);
        this.jobApplicationDao = new DataBaseJobApplicationDao(jobAnnouncementDao, studentDao);
        this.interviewSchedulingDao = new DataBaseInterviewSchedulingDao();
    }

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

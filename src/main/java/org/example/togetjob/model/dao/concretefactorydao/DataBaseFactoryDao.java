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
        this.recruiterDao = new DataBaseRecruiterDao(userDao);
        this.studentDao = new DataBaseStudentDao(userDao, null);
        this.jobAnnouncementDao = new DataBaseJobAnnouncementDao(recruiterDao);
        this.jobApplicationDao = new DataBaseJobApplicationDao(jobAnnouncementDao, studentDao);
        this.interviewSchedulingDao = new DataBaseInterviewSchedulingDao(jobAnnouncementDao, studentDao);

        //loop so:
        (studentDao).setJobApplicationDao(jobApplicationDao);

    }

    @Override
    public UserDao createUserDao() { return userDao; }//NOSONAR

    @Override
    public JobAnnouncementDao createJobAnnouncementDao() { return jobAnnouncementDao; }//NOSONAR

    @Override
    public StudentDao createStudentDao() { return studentDao; }//NOSONAR

    @Override
    public RecruiterDao createRecruiterDao() { return recruiterDao; }//NOSONAR

    @Override
    public JobApplicationDao createJobApplicationDao() { return jobApplicationDao; }//NOSONAR

    @Override
    public InterviewSchedulingDao createInterviewSchedulingDao() { return interviewSchedulingDao; }//NOSONAR
}

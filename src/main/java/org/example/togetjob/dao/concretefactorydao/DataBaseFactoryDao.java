package org.example.togetjob.dao.concretefactorydao;

import org.example.togetjob.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.dao.abstractobjects.*;
import org.example.togetjob.dao.concreteobjects.*;

public class DataBaseFactoryDao extends AbstractFactoryDaoSingleton {

    private final DataBaseUserDao userDao;
    private final DataBaseJobAnnouncementDao jobAnnouncementDao;
    private final DataBaseStudentDao studentDao;
    private final DataBaseRecruiterDao recruiterDao;
    private final DataBaseJobApplicationDao jobApplicationDao;

    public DataBaseFactoryDao() {
        this.userDao = new DataBaseUserDao();
        this.recruiterDao = new DataBaseRecruiterDao(userDao);
        this.studentDao = new DataBaseStudentDao(userDao, null);
        this.jobAnnouncementDao = new DataBaseJobAnnouncementDao(recruiterDao);
        this.jobApplicationDao = new DataBaseJobApplicationDao(jobAnnouncementDao, studentDao);
        //loop so:
        (studentDao).setJobApplicationDao(jobApplicationDao);

    }

    @Override
    public UserDao createUserDao() { return userDao; }

    @Override
    public JobAnnouncementDao createJobAnnouncementDao() { return jobAnnouncementDao; }

    @Override
    public StudentDao createStudentDao() { return studentDao; }

    @Override
    public DataBaseRecruiterDao createRecruiterDao() { return recruiterDao; }

    @Override
    public JobApplicationDao createJobApplicationDao() { return jobApplicationDao; }

}

package org.example.togetjob.model.dao.concretefactorydao;

import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.*;
import org.example.togetjob.model.dao.concreteobjects.*;

import java.util.Map;
import java.util.HashMap;

public class DataBaseFactoryDao extends AbstractFactoryDaoSingleton {


    private final Map<Class<?>, Object> daoMap = new HashMap<>();

    public DataBaseFactoryDao() {

        DataBaseUserDao userDao = new DataBaseUserDao();
        DataBaseRecruiterDao recruiterDao = new DataBaseRecruiterDao(userDao);
        DataBaseStudentDao studentDao = new DataBaseStudentDao(userDao, null);
        DataBaseJobAnnouncementDao jobAnnouncementDao = new DataBaseJobAnnouncementDao(recruiterDao);
        DataBaseJobApplicationDao jobApplicationDao = new DataBaseJobApplicationDao(jobAnnouncementDao, studentDao);
        DataBaseInterviewSchedulingDao interviewSchedulingDao = new DataBaseInterviewSchedulingDao(jobAnnouncementDao, studentDao);


        studentDao.setJobApplicationDao(jobApplicationDao);


        daoMap.put(UserDao.class, userDao);
        daoMap.put(JobAnnouncementDao.class, jobAnnouncementDao);
        daoMap.put(StudentDao.class, studentDao);
        daoMap.put(RecruiterDao.class, recruiterDao);
        daoMap.put(JobApplicationDao.class, jobApplicationDao);
        daoMap.put(InterviewSchedulingDao.class, interviewSchedulingDao);
    }

    @Override
    public UserDao createUserDao() {
        return getDao(UserDao.class);
    }

    @Override
    public JobAnnouncementDao createJobAnnouncementDao() {
        return getDao(JobAnnouncementDao.class);
    }

    @Override
    public StudentDao createStudentDao() {
        return getDao(StudentDao.class);
    }

    @Override
    public RecruiterDao createRecruiterDao() {
        return getDao(RecruiterDao.class);
    }

    @Override
    public JobApplicationDao createJobApplicationDao() {
        return getDao(JobApplicationDao.class);
    }

    @Override
    public InterviewSchedulingDao createInterviewSchedulingDao() {
        return getDao(InterviewSchedulingDao.class);
    }

    private <T> T getDao(Class<T> daoClass) {
        Object dao = daoMap.get(daoClass);
        if (dao == null) {
            throw new IllegalStateException("DAO not found: " + daoClass.getName());
        }
        return daoClass.cast(dao);
    }
}

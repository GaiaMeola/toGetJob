package org.example.togetjob.model.dao.concretefactorydao;

import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.*;
import org.example.togetjob.model.dao.concreteobjects.*;

import java.util.Map;
import java.util.HashMap;

public class InMemoryFactoryDao extends AbstractFactoryDaoSingleton {

    private final Map<Class<?>, Object> daoMap = new HashMap<>();

    public InMemoryFactoryDao() {
        // Creazione degli oggetti DAO
        InMemoryUserDao userDao = new InMemoryUserDao();
        InMemoryRecruiterDao recruiterDao = new InMemoryRecruiterDao();
        InMemoryStudentDao studentDao = new InMemoryStudentDao();
        InMemoryJobAnnouncementDao jobAnnouncementDao = new InMemoryJobAnnouncementDao();
        InMemoryJobApplicationDao jobApplicationDao = new InMemoryJobApplicationDao();
        InMemoryInterviewSchedulingDao interviewSchedulingDao = new InMemoryInterviewSchedulingDao();

        // Popolamento della mappa
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

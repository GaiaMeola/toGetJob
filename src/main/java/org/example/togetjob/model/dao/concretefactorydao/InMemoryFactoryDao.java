package org.example.togetjob.model.dao.concretefactorydao;

import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.*;
import org.example.togetjob.model.dao.concreteobjects.*;

import java.util.Map;
import java.util.HashMap;

public class InMemoryFactoryDao extends AbstractFactoryDaoSingleton {

    private final Map<Class<?>, Object> daoMap = new HashMap<>();

    public InMemoryFactoryDao() {
        daoMap.put(UserDao.class, new InMemoryUserDao());
        daoMap.put(JobAnnouncementDao.class, new InMemoryJobAnnouncementDao());
        daoMap.put(StudentDao.class, new InMemoryStudentDao());
        daoMap.put(RecruiterDao.class, new InMemoryRecruiterDao());
        daoMap.put(JobApplicationDao.class, new InMemoryJobApplicationDao());
        daoMap.put(InterviewSchedulingDao.class, new InMemoryInterviewSchedulingDao());
    }

    @Override
    public UserDao createUserDao() {
        return (UserDao) daoMap.get(UserDao.class);
    }

    @Override
    public JobAnnouncementDao createJobAnnouncementDao() {
        return (JobAnnouncementDao) daoMap.get(JobAnnouncementDao.class);
    }

    @Override
    public StudentDao createStudentDao() {
        return (StudentDao) daoMap.get(StudentDao.class);
    }

    @Override
    public RecruiterDao createRecruiterDao() {
        return (RecruiterDao) daoMap.get(RecruiterDao.class);
    }

    @Override
    public JobApplicationDao createJobApplicationDao() {
        return (JobApplicationDao) daoMap.get(JobApplicationDao.class);
    }

    @Override
    public InterviewSchedulingDao createInterviewSchedulingDao() {
        return (InterviewSchedulingDao) daoMap.get(InterviewSchedulingDao.class);
    }
}
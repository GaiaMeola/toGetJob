package org.example.togetjob.model.dao.concretefactorydao;

import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.*;
import org.example.togetjob.model.dao.concreteobjects.*;

import java.util.Map;
import java.util.HashMap;

public class FileSystemFactoryDao extends AbstractFactoryDaoSingleton {

    private final Map<Class<?>, Object> daoMap = new HashMap<>();

    public FileSystemFactoryDao() {
        daoMap.put(UserDao.class, new FileSystemUserDao());
        daoMap.put(JobAnnouncementDao.class, new FileSystemJobAnnouncementDao());
        daoMap.put(StudentDao.class, new FileSystemStudentDao());
        daoMap.put(RecruiterDao.class, new FileSystemRecruiterDao());
        daoMap.put(JobApplicationDao.class, new FileSystemJobApplicationDao());
        daoMap.put(InterviewSchedulingDao.class, new FileSystemInterviewSchedulingDao());
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
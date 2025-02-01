package org.example.togetjob.controller.student;

import org.example.togetjob.bean.JobAnnouncementSearchBean;
import org.example.togetjob.bean.JobApplicationBean;
import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.dao.abstractobjects.JobApplicationDao;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.model.factory.JobApplicationFactory;

import java.time.LocalDate;
import java.util.List;

public class SendAJobApplication {

    //private final JobApplicationDao jobAnnouncementDao;

    public List<JobAnnouncementSearchBean> filterJobAnnouncement (){

        return List.of() ;

    }

    public boolean sendAJobApplication (JobApplicationBean jobApplicationBean){


        return false ;

    }



}

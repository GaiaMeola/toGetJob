package org.example.togetjob.controller.recruiter;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.factory.JobAnnouncementFactory;
import org.example.togetjob.pattern.observer.RecruiterObserverStudent;
import org.example.togetjob.session.SessionManager;

import java.util.*;

public class PublishAJobAnnouncementController {

    private final JobAnnouncementDao jobAnnouncementDao;

    public PublishAJobAnnouncementController(){
      this.jobAnnouncementDao = AbstractFactoryDaoSingleton.getFactoryDao().createJobAnnouncementDao();
    }

    public boolean publishJobAnnouncement(JobAnnouncementBean jobAnnouncementBean){

        Recruiter recruiter = getRecruiterFromSession();
        int workingHours ;
        double salary ;

        if (jobAnnouncementDao.jobAnnouncementExists(jobAnnouncementBean.getJobTitle(), recruiter)) {
            return false;  // false if user exists
        }

        try {
            workingHours = Integer.parseInt(jobAnnouncementBean.getWorkingHours());
            salary = Double.parseDouble(jobAnnouncementBean.getSalary());
        } catch (NumberFormatException e) {
            return false;
        }


        JobAnnouncement jobAnnouncement = JobAnnouncementFactory.createJobAnnouncement(
                jobAnnouncementBean.getJobTitle(),
                jobAnnouncementBean.getJobType(),
                jobAnnouncementBean.getRole(),
                jobAnnouncementBean.getLocation(),
                workingHours,
                jobAnnouncementBean.getCompanyName(),
                salary,
                jobAnnouncementBean.getDescription(),
                recruiter
        );

        // register recruiter who publishes the job announcement
        jobAnnouncement.getJobApplicationCollection().attach(new RecruiterObserverStudent(recruiter));
        return jobAnnouncementDao.saveJobAnnouncement(jobAnnouncement);

    }

    private Recruiter getRecruiterFromSession() {
        return SessionManager.getInstance().getRecruiterFromSession();
    }

    public boolean changeJobAnnouncementStatus(JobAnnouncementBean jobAnnouncementBean, boolean isActive){

        Recruiter recruiter = getRecruiterFromSession();
        Optional<JobAnnouncement> jobAnnouncementOptional = jobAnnouncementDao.getJobAnnouncement(jobAnnouncementBean.getJobTitle(), recruiter);

        if(jobAnnouncementOptional.isPresent()){
            JobAnnouncement jobAnnouncement = jobAnnouncementOptional.get();
            jobAnnouncement.setActive(isActive);
            return jobAnnouncementDao.updateJobAnnouncement(jobAnnouncement);
        }

        return false;
    }

    public boolean deleteJobAnnouncement(JobAnnouncementBean jobAnnouncementBean){
        Recruiter recruiter = getRecruiterFromSession();
        Optional<JobAnnouncement> jobAnnouncementOptional = jobAnnouncementDao.getJobAnnouncement(jobAnnouncementBean.getJobTitle(), recruiter);

        return jobAnnouncementOptional.filter(jobAnnouncementDao::deleteJobAnnouncement).isPresent();
    }

    public List<JobAnnouncementBean> getJobAnnouncement() {
        JobAnnouncementService jobAnnouncementService = new JobAnnouncementService(jobAnnouncementDao);

        List<JobAnnouncementBean> jobAnnouncements = jobAnnouncementService.getJobAnnouncementsForCurrentRecruiter();

        return Objects.requireNonNullElse(jobAnnouncements, Collections.emptyList());

    }
}

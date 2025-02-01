package org.example.togetjob.controller.recruiter;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.factory.JobAnnouncementFactory;
import org.example.togetjob.pattern_observer.observer.RecruiterObserver;
import org.example.togetjob.session.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PublishAJobAnnouncementController {

    private final JobAnnouncementDao jobAnnouncementDao;

    public PublishAJobAnnouncementController(){
      this.jobAnnouncementDao = AbstractFactoryDaoSingleton.getFactoryDao().createJobAnnouncementDao();
    }

    public boolean publishJobAnnouncement(JobAnnouncementBean jobAnnouncementBean){

        Recruiter recruiter = getRecruiterFromSession();
        int workingHours = 0;
        double salary = 0;

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
        jobAnnouncement.getJobApplicationCollection().attach(new RecruiterObserver(recruiter));
        return jobAnnouncementDao.saveJobAnnouncement(jobAnnouncement);

    }

    private Recruiter getRecruiterFromSession() {
        // Recruiter from session
        return (Recruiter) SessionManager.getInstance().getCurrentUser();
    }

    public boolean deactivateJobAnnouncement(JobAnnouncementBean jobAnnouncementBean){

        Recruiter recruiter = getRecruiterFromSession();
        Optional<JobAnnouncement> jobAnnouncementOptional = jobAnnouncementDao.getJobAnnouncement(jobAnnouncementBean.getJobTitle(), recruiter);

        if(jobAnnouncementOptional.isPresent()){
            JobAnnouncement jobAnnouncement = jobAnnouncementOptional.get();
            jobAnnouncement.setActive(false);
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
        Recruiter recruiter = getRecruiterFromSession();
        List<JobAnnouncement> jobAnnouncements = jobAnnouncementDao.getAllJobAnnouncements(recruiter);

        List<JobAnnouncementBean> jobAnnouncementBeans = new ArrayList<>();
        for (JobAnnouncement job : jobAnnouncements) {

            JobAnnouncementBean bean = new JobAnnouncementBean(
                    job.getJobTitle(),
                    job.getJobType(),
                    job.getRole(),
                    job.getLocation(),
                    String.valueOf(job.getWorkingHours()),  // String
                    job.getCompanyName(),
                    String.valueOf(job.getSalary()),        // String
                    job.getDescription(),
                    job.getActive(),
                    recruiter.getUsername()
            );

            jobAnnouncementBeans.add(bean);
        }
        return jobAnnouncementBeans;
    }


}

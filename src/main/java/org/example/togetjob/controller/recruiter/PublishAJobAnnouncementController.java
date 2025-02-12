package org.example.togetjob.controller.recruiter;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.controller.LoginController;
import org.example.togetjob.exceptions.InvalidSalaryException;
import org.example.togetjob.exceptions.InvalidWorkingHourException;
import org.example.togetjob.exceptions.JobAnnouncementAlreadyExists;
import org.example.togetjob.exceptions.UserNotLoggedException;
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
    private final LoginController loginController;

    public PublishAJobAnnouncementController(){
      this.jobAnnouncementDao = AbstractFactoryDaoSingleton.getFactoryDao().createJobAnnouncementDao();
      this.loginController = new LoginController();
    }

    public boolean publishJobAnnouncement(JobAnnouncementBean jobAnnouncementBean){

        Recruiter recruiter = getRecruiterFromSession();
        int workingHours ;
        double salary ;

        if(isUserLogged()){
            throw new UserNotLoggedException();
        }

        if (jobAnnouncementDao.jobAnnouncementExists(jobAnnouncementBean.getJobTitle(), recruiter)) {
            throw new JobAnnouncementAlreadyExists("A job announcement with this title already exists.");  // false if job announcement exists
        }

        try {
            workingHours = Integer.parseInt(jobAnnouncementBean.getWorkingHours());
        } catch (NumberFormatException e) {
            throw new InvalidWorkingHourException("Working hours must be greater than 0.");
        }

        try {
            salary = Double.parseDouble(jobAnnouncementBean.getSalary());
        } catch (NumberFormatException e) {
            throw new InvalidSalaryException("Working hours must be greater than 0.");
        }

        JobAnnouncement jobAnnouncement = JobAnnouncementFactory.createJobAnnouncement(
                jobAnnouncementBean.getJobTitle(),
                jobAnnouncementBean.getJobType(),
                jobAnnouncementBean.getRole(),
                jobAnnouncementBean.getLocation(),
                workingHours,
                jobAnnouncementBean.getCompanyName(),
                salary
        );

        JobAnnouncementFactory.completeJobAnnouncement(
                jobAnnouncement,
                jobAnnouncementBean.getDescription(),
                recruiter,
                true
        ); //default

        // register recruiter who publishes the job announcement
        jobAnnouncement.getJobApplicationCollection().attach(new RecruiterObserverStudent(recruiter));
        return jobAnnouncementDao.saveJobAnnouncement(jobAnnouncement);

    }

    private Recruiter getRecruiterFromSession() {
        return SessionManager.getInstance().getRecruiterFromSession();
    }

    public boolean changeJobAnnouncementStatus(JobAnnouncementBean jobAnnouncementBean, boolean isActive){

        if(isUserLogged()){
            throw new UserNotLoggedException();
        }

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

        if(isUserLogged()){
            throw new UserNotLoggedException();
        }

        Recruiter recruiter = getRecruiterFromSession();
        Optional<JobAnnouncement> jobAnnouncementOptional = jobAnnouncementDao.getJobAnnouncement(jobAnnouncementBean.getJobTitle(), recruiter);

        return jobAnnouncementOptional.filter(jobAnnouncementDao::deleteJobAnnouncement).isPresent();
    }

    public List<JobAnnouncementBean> getJobAnnouncement() {

        if(isUserLogged()){
            throw new UserNotLoggedException();
        }

        JobAnnouncementService jobAnnouncementService = new JobAnnouncementService(jobAnnouncementDao);

        List<JobAnnouncementBean> jobAnnouncements = jobAnnouncementService.getJobAnnouncementsForCurrentRecruiter();

        return Objects.requireNonNullElse(jobAnnouncements, Collections.emptyList());

    }

    public boolean isUserLogged() throws UserNotLoggedException {
        if (!loginController.isUserLogged()) {
            throw new UserNotLoggedException();
        }
        return false;
    }
}

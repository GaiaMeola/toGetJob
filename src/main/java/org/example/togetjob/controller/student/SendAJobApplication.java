package org.example.togetjob.controller.student;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.JobAnnouncementSearchBean;
import org.example.togetjob.bean.JobApplicationBean;
import org.example.togetjob.exceptions.ConfigException;
import org.example.togetjob.exceptions.JobAnnouncementNotFoundException;
import org.example.togetjob.exceptions.NotificationException;
import org.example.togetjob.exceptions.RecruiterNotFoundException;
import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.dao.abstractobjects.JobApplicationDao;
import org.example.togetjob.model.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.entity.*;
import org.example.togetjob.model.factory.JobApplicationFactory;
import org.example.togetjob.model.factory.NotificationFactory;
import org.example.togetjob.pattern.Notification;
import org.example.togetjob.pattern.observer.RecruiterObserver;
import org.example.togetjob.pattern.subject.JobApplicationCollectionSubject;
import org.example.togetjob.session.SessionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SendAJobApplication {

    private final JobAnnouncementDao jobAnnouncementDao;
    private final JobApplicationDao jobApplicationDao;
    private final RecruiterDao recruiterDao;
    private final JobApplicationCollectionSubject jobApplicationCollection;

    public SendAJobApplication() {
        this.jobAnnouncementDao = AbstractFactoryDaoSingleton.getFactoryDao().createJobAnnouncementDao();
        this.jobApplicationDao = AbstractFactoryDaoSingleton.getFactoryDao().createJobApplicationDao();
        this.recruiterDao = AbstractFactoryDaoSingleton.getFactoryDao().createRecruiterDao();
        this.jobApplicationCollection = new JobApplicationCollectionSubject();
    }

    public List<JobAnnouncementBean> showFilteredJobAnnouncements(JobAnnouncementSearchBean jobAnnouncementSearchBean) {
        //all job announcements
        List<JobAnnouncement> allAnnouncements = jobAnnouncementDao.getAllJobAnnouncements();

        List<JobAnnouncement> filteredAnnouncements = allAnnouncements.stream()
                .filter(announcement -> filterByTitle(announcement, jobAnnouncementSearchBean.getJobTitle()))
                .filter(announcement -> filterByJobType(announcement, jobAnnouncementSearchBean.getJobType()))
                .filter(announcement -> filterByRole(announcement, jobAnnouncementSearchBean.getRole()))
                .filter(announcement -> filterByLocation(announcement, jobAnnouncementSearchBean.getLocation()))
                .filter(announcement -> filterByWorkingHours(announcement, jobAnnouncementSearchBean.getWorkingHours()))
                .filter(announcement -> filterByCompanyName(announcement, jobAnnouncementSearchBean.getCompanyName()))
                .filter(announcement -> filterBySalary(announcement, jobAnnouncementSearchBean.getSalary()))
                .toList();

        return filteredAnnouncements.stream()
                .map(this::convertToJobAnnouncementBean)
                .toList();

    }

    private JobAnnouncementBean convertToJobAnnouncementBean(JobAnnouncement jobAnnouncement) {

        JobAnnouncementBean jobAnnouncementBean = new JobAnnouncementBean();

        jobAnnouncementBean.setJobTitle(jobAnnouncement.getJobTitle() != null ? jobAnnouncement.getJobTitle() : "Unknown Title");
        jobAnnouncementBean.setJobType(jobAnnouncement.getJobType() != null ? jobAnnouncement.getJobType() : "Unknown Type");
        jobAnnouncementBean.setRole(jobAnnouncement.getRole() != null ? jobAnnouncement.getRole() : "Unknown Role");
        jobAnnouncementBean.setLocation(jobAnnouncement.getLocation() != null ? jobAnnouncement.getLocation() : "Unknown Location");
        jobAnnouncementBean.setCompanyName(jobAnnouncement.getCompanyName() != null ? jobAnnouncement.getCompanyName() : "Unknown Company");
        jobAnnouncementBean.setDescription(jobAnnouncement.getDescription() != null ? jobAnnouncement.getDescription() : "No Description");


        //map
        jobAnnouncementBean.setActive(jobAnnouncement.getActive() != null && jobAnnouncement.getActive());
        jobAnnouncementBean.setRecruiterUsername(jobAnnouncement.getRecruiter().getUsername());
        jobAnnouncementBean.setWorkingHours(jobAnnouncement.getWorkingHours() != 0 ? String.valueOf(jobAnnouncement.getWorkingHours()) : "Not Defined");
        jobAnnouncementBean.setSalary(jobAnnouncement.getSalary() != 0.0 ? String.valueOf(jobAnnouncement.getSalary()) : "Not Defined");

        return jobAnnouncementBean;

    }

    public JobAnnouncementBean showJobAnnouncementDetails(JobAnnouncementBean jobAnnouncementBean) throws RecruiterNotFoundException {

        String recruiterUsername = jobAnnouncementBean.getRecruiterUsername();
        Optional<Recruiter> recruiterOpt = recruiterDao.getRecruiter(recruiterUsername);

        if (recruiterOpt.isEmpty()) {
            throw new RecruiterNotFoundException("Recruiter not found for username: " + recruiterUsername);
        }

        Recruiter recruiter = recruiterOpt.get();

        Optional<JobAnnouncement> jobAnnouncement = jobAnnouncementDao.getJobAnnouncement(jobAnnouncementBean.getJobTitle(), recruiter);

        if (jobAnnouncement.isPresent()) {
            return convertToJobAnnouncementBean(jobAnnouncement.get());
        } else {
            throw new JobAnnouncementNotFoundException("Job Announcement Not Found");
        }
    }

    public JobApplicationBean showJobApplicationForm(JobAnnouncementBean jobAnnouncementBean) {

        JobApplicationBean form = new JobApplicationBean();

        form.setJobTitle(jobAnnouncementBean.getJobTitle());
        form.setRecruiterUsername(jobAnnouncementBean.getRecruiterUsername());
        form.setStatus(Status.PENDING);
        form.setStudentUsername(getStudentFromSession().getUsername());

        form.setCoverLetter("");
        return form;
    }

    public boolean sendAJobApplication(JobApplicationBean jobApplicationBean) {

        // Student who wants to send a job application to a job announcement
        Student student = getStudentFromSession();

        //Recruiter who publishes the job announcement
        Recruiter recruiter = recruiterDao.getRecruiter(jobApplicationBean.getRecruiterUsername())
                .orElseThrow(() -> new IllegalArgumentException("Error: Recruiter not found."));

        // Job Announcement
        JobAnnouncement jobAnnouncement = jobAnnouncementDao.getJobAnnouncement(jobApplicationBean.getJobTitle(), recruiter)
                .orElseThrow(() -> new IllegalArgumentException("Error: JobAnnouncement not found.")); // Job Announcement Found

        if (jobApplicationDao.getJobApplication(student, jobAnnouncement).isPresent()) {
            return false; // Job Application already sent
        }

        JobApplication jobApplication = JobApplicationFactory.createJobApplication(student, jobApplicationBean.getCoverLetter(), jobAnnouncement);
        jobApplicationDao.saveJobApplication(jobApplication); // Persistence

        try {
            sendNotificationToRecruiter(jobApplication);
        } catch (NotificationException e) {
            return false;
        }

        return true;
    }

    private void sendNotificationToRecruiter(JobApplication jobApplication) throws NotificationException {

        Recruiter recruiter = jobApplication.getJobAnnouncement().getRecruiter();

        try {
            Notification notification = NotificationFactory.createNotification();
            RecruiterObserver recruiterObserver = new RecruiterObserver(recruiter, notification);

            jobApplicationCollection.attach(recruiterObserver); // observer
            jobApplicationCollection.addJobApplication(jobApplication); // notify

        } catch (ConfigException e) {
            throw new NotificationException("Error during the configuration", e);
        }

    }

    private Optional<JobApplication> getJobApplication(JobApplicationBean jobApplicationBean) {

        Student student = getStudentFromSession();

        Recruiter recruiter = recruiterDao.getRecruiter(jobApplicationBean.getRecruiterUsername())
                .orElseThrow(() -> new IllegalArgumentException("Error: Recruiter not found."));

        JobAnnouncement jobAnnouncement = jobAnnouncementDao.getJobAnnouncement(jobApplicationBean.getJobTitle(), recruiter)
                .orElseThrow(() -> new IllegalArgumentException("Error: Job Announcement not found."));

        return jobApplicationDao.getJobApplication(student, jobAnnouncement);

    }

    private Status getStatusJobApplication(JobApplicationBean jobApplicationBean) {

        Optional<JobApplication> jobApplicationOpt = getJobApplication(jobApplicationBean);
        if (jobApplicationOpt.isEmpty()) {

            throw new IllegalArgumentException("Error: job Application not Found.");

        }

        JobApplication jobApplication = jobApplicationOpt.get();
        return jobApplication.getStatus();

    }


    public boolean modifyJobApplication(JobApplicationBean jobApplicationBean) {

        Status status = getStatusJobApplication(jobApplicationBean);

        if (status.equals(Status.PENDING)) {
            Optional<JobApplication> jobApplicationOPT = getJobApplication(jobApplicationBean);
            //Job application existing

            if (jobApplicationOPT.isEmpty()) {
                return false;
            }

            JobApplication oldJobApplication = jobApplicationOPT.get();

            JobApplication newJobApplication = JobApplicationFactory.createJobApplication(
                    oldJobApplication.getStudent(),
                    jobApplicationBean.getCoverLetter(),
                    oldJobApplication.getJobAnnouncement()
            );


            jobApplicationDao.saveJobApplication(newJobApplication);
            return true;
        }

        return false;
    }


    public boolean deleteJobApplication(JobApplicationBean jobApplicationBean) {

        Optional<JobApplication> jobApplicationOPT = getJobApplication(jobApplicationBean);

        if (jobApplicationOPT.isEmpty()) {
            return false;
        }

        JobApplication jobApplication = jobApplicationOPT.get();

        if (!jobApplication.getStatus().equals(Status.PENDING)) {
            return false; //job application already managed
        }

        jobApplicationDao.deleteJobApplication(jobApplication);
        jobApplicationCollection.removeJobApplication(jobApplication);

        return true;

    }

    public boolean acceptJobApplication(JobApplicationBean jobApplicationBean) {

        Optional<JobApplication> jobApplicationOPT = getJobApplication(jobApplicationBean);

        if (jobApplicationOPT.isEmpty()) {
            throw new IllegalArgumentException("Error: not found.");
        }

        JobApplication jobApplication = jobApplicationOPT.get();

        if (!jobApplication.getStatus().equals(Status.PENDING)) {
            return false; //job application already managed
        }

        jobApplication.setStatus(Status.ACCEPTED);
        jobApplicationDao.saveJobApplication(jobApplication);

        return true;

    }

    public boolean rejectJobApplication(JobApplicationBean jobApplicationBean) {

        Optional<JobApplication> jobApplicationOPT = getJobApplication(jobApplicationBean);

        if (jobApplicationOPT.isEmpty()) {
            throw new IllegalArgumentException("Error: not found.");
        }

        JobApplication jobApplication = jobApplicationOPT.get();

        if (!jobApplication.getStatus().equals(Status.PENDING)) {
            return false; //job application already managed
        }

        jobApplication.setStatus(Status.REJECTED);
        jobApplicationDao.saveJobApplication(jobApplication);

        return true;

    }

   /* public List<JobApplicationBean> viewPendingJobApplications(JobAnnouncementBean jobAnnouncementBean) {

        List<JobApplication> pendingApplications = getPendingApplicationsForJob(jobAnnouncementBean);

        if (pendingApplications.isEmpty()) {
            return new ArrayList<>(); //No applications
        }

        List<JobApplicationBean> jobApplicationBeans = new ArrayList<>();

        for (JobApplication application : pendingApplications) {
            JobApplicationBean jobApplicationBean = convertToJobApplicationBean(application);
            jobApplicationBeans.add(jobApplicationBean);
        }

        return jobApplicationBeans;
    }

    private List<JobApplication> getPendingApplicationsForJob(JobAnnouncementBean jobAnnouncementBean){

        List<JobApplication> pendingApplications = new ArrayList<>();

        for(JobApplication application: jobAnnouncementBean.getJobApplications()){
            if (application.getStatus() == JobApplication.Status.PENDING_APPROVAL) {
                pendingApplications.add(application);
            }
        }

        return pendingApplications;

    } da rivedere e rifare */

    //student session for the job application

    private Student getStudentFromSession() {
        // Student from session
        return (Student) SessionManager.getInstance().getCurrentUser();
    }

    //method to filter

    private boolean filterByTitle(JobAnnouncement announcement, String jobTitle) {
        return jobTitle == null || jobTitle.isEmpty() || announcement.getJobTitle().toLowerCase().contains(jobTitle.toLowerCase());
    }

    private boolean filterByLocation(JobAnnouncement announcement, String location) {
        return location == null || location.isEmpty() || announcement.getLocation().toLowerCase().contains(location.toLowerCase());
    }

    private boolean filterByRole(JobAnnouncement announcement, String role) {
        return role == null || role.isEmpty() || announcement.getRole().toLowerCase().contains(role.toLowerCase());
    }

    private boolean filterByJobType(JobAnnouncement announcement, String jobType) {
        return jobType == null || jobType.isEmpty() || announcement.getJobType().toLowerCase().contains(jobType.toLowerCase());
    }

    private boolean filterBySalary(JobAnnouncement announcement, String salary) {
        if (salary == null || salary.isEmpty()) {
            return true; //no filter
        }
        try {
            double salaryFilter = Double.parseDouble(salary);
            return announcement.getSalary() >= salaryFilter;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean filterByWorkingHours(JobAnnouncement announcement, String workingHours) {
        if (workingHours == null || workingHours.isEmpty()) {
            return true; //no filter
        }
        try {
            int workingHoursFilter = Integer.parseInt(workingHours);
            return announcement.getWorkingHours() >= workingHoursFilter;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean filterByCompanyName(JobAnnouncement announcement, String companyName) {
        return companyName == null || companyName.isEmpty() || announcement.getCompanyName().toLowerCase().contains(companyName.toLowerCase());
    }

}

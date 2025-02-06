package org.example.togetjob.controller.student;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.JobAnnouncementSearchBean;
import org.example.togetjob.bean.JobApplicationBean;
import org.example.togetjob.exceptions.*;
import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.dao.abstractobjects.JobApplicationDao;
import org.example.togetjob.model.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.entity.*;
import org.example.togetjob.model.factory.JobApplicationFactory;
import org.example.togetjob.model.factory.NotificationFactory;
import org.example.togetjob.pattern.observer.RecruiterObserverStudent;
import org.example.togetjob.pattern.subject.JobApplicationCollectionSubjectRecruiter;
import org.example.togetjob.session.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SendAJobApplication {

    private final JobAnnouncementDao jobAnnouncementDao;
    private final JobApplicationDao jobApplicationDao;
    private final RecruiterDao recruiterDao;
    private final JobApplicationCollectionSubjectRecruiter jobApplicationCollection;

    public SendAJobApplication() {
        this.jobAnnouncementDao = AbstractFactoryDaoSingleton.getFactoryDao().createJobAnnouncementDao();
        this.jobApplicationDao = AbstractFactoryDaoSingleton.getFactoryDao().createJobApplicationDao();
        this.recruiterDao = AbstractFactoryDaoSingleton.getFactoryDao().createRecruiterDao();
        this.jobApplicationCollection = new JobApplicationCollectionSubjectRecruiter();
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

    private List<JobApplicationBean> convertToJobApplicationBeans(List<JobApplication> jobApplications) {
        List<JobApplicationBean> jobApplicationBeans = new ArrayList<>();

        for (JobApplication jobApplication : jobApplications) {
            JobApplicationBean jobApplicationBean = new JobApplicationBean();

            jobApplicationBean.setJobTitle(jobApplication.getJobAnnouncement().getJobTitle());
            jobApplicationBean.setStudentUsername(jobApplication.getStudent().getUsername());
            jobApplicationBean.setCoverLetter(jobApplication.getCoverLetter());
            jobApplicationBean.setRecruiterUsername(jobApplication.getJobAnnouncement().getRecruiter().getUsername());
            jobApplicationBean.setStatus(jobApplication.getStatus());
            jobApplicationBeans.add(jobApplicationBean);
        }

        return jobApplicationBeans;
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

        // Check if the job announcement is still active
        if (jobAnnouncement.getActive() == null || !jobAnnouncement.getActive()) {
            throw new JobAnnouncementNotActiveException("This job announcement is no longer active.");
        }

        // Check if the student has already applied for this job
        if (jobApplicationDao.getJobApplication(student, jobAnnouncement).isPresent()) {
            throw new JobApplicationAlreadySentException("You have already applied for this job.");
        }

        JobApplication jobApplication = JobApplicationFactory.createJobApplication(student, jobApplicationBean.getCoverLetter(), jobAnnouncement);
        jobApplicationDao.saveJobApplication(jobApplication); // Persistence

        try {
            RecruiterObserverStudent recruiterObserver = new RecruiterObserverStudent(recruiter, NotificationFactory.createNotification("A new job application has been submitted!"));
            jobApplicationCollection.attach(recruiterObserver);
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }

        try {
            sendNotificationToRecruiter(jobApplication);
        } catch (NotificationException e) {
            return false;
        }

        return true;
    }

    public List<JobApplicationBean> getAllJobApplication(){

        Student student = getStudentFromSession();

        if (student == null) {
            throw new IllegalStateException("Error: No student is currently logged in.");
        }

        List<JobApplication> jobApplications = jobApplicationDao.getAllJobApplications(student);
        return convertToJobApplicationBeans(jobApplications);

    }

    public boolean modifyJobApplication(JobApplicationBean jobApplicationBean) {

        Status status = getStatusJobApplication(jobApplicationBean);

        if (status.equals(Status.PENDING)) {
            Optional<JobApplication> jobApplicationOPT = getJobApplication(jobApplicationBean);
            //Job application existing

            if (jobApplicationOPT.isEmpty()) {
                return false;
            }

            JobApplication jobApplication = jobApplicationOPT.get();
            jobApplication.setCoverLetter(jobApplicationBean.getCoverLetter());  // Modifica direttamente i campi
            jobApplicationDao.saveJobApplication(jobApplication);
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

    public List<JobApplicationBean> getJobApplicationsForRecruiter(JobAnnouncementBean jobAnnouncementBean){

        Recruiter recruiter = getRecruiterFromSession();

        Optional<JobAnnouncement> jobAnnouncementOpt = jobAnnouncementDao.getJobAnnouncement(jobAnnouncementBean.getJobTitle(), recruiter);
        if (jobAnnouncementOpt.isEmpty()) {
            throw new IllegalStateException("Error: No job announcement found for the recruiter with the specified title.");
        }

        JobAnnouncement jobAnnouncement = jobAnnouncementOpt.get();

        // all the job applications sent to the job announcement
        List<JobApplication> jobApplications = jobApplicationDao.getJobApplicationsByAnnouncement(jobAnnouncement);
        return convertToJobApplicationBeans(jobApplications);

    }


    public boolean updateJobApplicationStatus(JobApplicationBean jobApplicationBean, Status status) {


        JobAnnouncement jobAnnouncement = getJobAnnouncementFromBean(jobApplicationBean);

        List<JobApplication> jobApplications = jobApplicationDao.getJobApplicationsByAnnouncement(jobAnnouncement);
        Optional<JobApplication> jobApplicationOpt = jobApplications.stream()
                .filter(jobApplication -> jobApplication.getStudent().getUsername().equals(jobApplicationBean.getStudentUsername()))
                .findFirst();
        if (jobApplicationOpt.isEmpty()) {
            throw new IllegalArgumentException("Error: Job Application not found for the specified student.");
        }
        JobApplication jobApplication = jobApplicationOpt.get();
        if (!jobApplication.getStatus().equals(Status.PENDING)) {
            return false; // Already Managed
        }
        jobApplication.setStatus(status); // (ACCEPTED or REJECTED)
        jobApplicationDao.saveJobApplication(jobApplication);

        return true;

    }


    private JobAnnouncement getJobAnnouncementFromBean(JobApplicationBean jobApplicationBean) {
        //recruiter from session
        Recruiter recruiter = getRecruiterFromSession();

        // job announcement
        Optional<JobAnnouncement> jobAnnouncementOpt = jobAnnouncementDao.getJobAnnouncement(jobApplicationBean.getJobTitle(), recruiter);

        if (jobAnnouncementOpt.isEmpty()) {
            throw new IllegalArgumentException("Error: No job announcement found for the recruiter with the specified title.");
        }

        return jobAnnouncementOpt.get();
    }



    private void sendNotificationToRecruiter(JobApplication jobApplication) throws NotificationException {

        try {
            jobApplicationCollection.addJobApplication(jobApplication); // notify
        } catch (NotificationException e) {
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

    public Recruiter getLoggedRecruiter() {
        return getRecruiterFromSession();
    }

    //student session for the job application

    private Student getStudentFromSession() {
        // Student from session
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (!(currentUser instanceof Student)) {
            throw new IllegalStateException("Error: No student is currently logged in.");
        }
        return (Student) currentUser;
    }

    private Recruiter getRecruiterFromSession() {
        // Recruiter from session
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (!(currentUser instanceof Recruiter)) {
            throw new IllegalStateException("Error: No recruiter is currently logged in.");
        }
        return (Recruiter) currentUser;
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

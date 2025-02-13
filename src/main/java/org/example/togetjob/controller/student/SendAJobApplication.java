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
import org.example.togetjob.session.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SendAJobApplication {

    private final JobAnnouncementDao jobAnnouncementDao;
    private final JobApplicationDao jobApplicationDao;
    private final RecruiterDao recruiterDao;

    public SendAJobApplication() {
        this.jobAnnouncementDao = AbstractFactoryDaoSingleton.getFactoryDao().createJobAnnouncementDao();
        this.jobApplicationDao = AbstractFactoryDaoSingleton.getFactoryDao().createJobApplicationDao();
        this.recruiterDao = AbstractFactoryDaoSingleton.getFactoryDao().createRecruiterDao();
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

        jobAnnouncementBean.setJobTitle(jobAnnouncement.obtainJobTitle() != null ? jobAnnouncement.obtainJobTitle() : "Unknown Title");
        jobAnnouncementBean.setJobType(jobAnnouncement.obtainJobType() != null ? jobAnnouncement.obtainJobType() : "Unknown Type");
        jobAnnouncementBean.setRole(jobAnnouncement.obtainJobRole() != null ? jobAnnouncement.obtainJobRole() : "Unknown Role");
        jobAnnouncementBean.setLocation(jobAnnouncement.obtainLocation() != null ? jobAnnouncement.obtainLocation() : "Unknown Location");
        jobAnnouncementBean.setCompanyName(jobAnnouncement.obtainCompanyName() != null ? jobAnnouncement.obtainCompanyName() : "Unknown Company");
        jobAnnouncementBean.setDescription(jobAnnouncement.obtainDescription() != null ? jobAnnouncement.obtainDescription() : "No Description");


        //map
        jobAnnouncementBean.setActive(jobAnnouncement.isJobActive() != null && jobAnnouncement.isJobActive());
        jobAnnouncementBean.setRecruiterUsername(jobAnnouncement.getRecruiter().obtainUsername());
        jobAnnouncementBean.setWorkingHours(jobAnnouncement.obtainWorkingHours() != 0 ? String.valueOf(jobAnnouncement.obtainWorkingHours()) : "Not Defined");
        jobAnnouncementBean.setSalary(jobAnnouncement.obtainSalary() != 0.0 ? String.valueOf(jobAnnouncement.obtainSalary()) : "Not Defined");

        return jobAnnouncementBean;

    }

    private List<JobApplicationBean> convertToJobApplicationBeans(List<JobApplication> jobApplications) {
        List<JobApplicationBean> jobApplicationBeans = new ArrayList<>();

        for (JobApplication jobApplication : jobApplications) {
            JobApplicationBean jobApplicationBean = new JobApplicationBean();

            jobApplicationBean.setJobTitle(jobApplication.getJobAnnouncement().obtainJobTitle());
            jobApplicationBean.setStudentUsername(jobApplication.getStudent().obtainUsername());
            jobApplicationBean.setCoverLetter(jobApplication.obtainCoverLetter());
            jobApplicationBean.setRecruiterUsername(jobApplication.getJobAnnouncement().getRecruiter().obtainUsername());
            jobApplicationBean.setStatus(jobApplication.obtainStatus());
            jobApplicationBeans.add(jobApplicationBean);
        }

        return jobApplicationBeans;
    }

    public JobApplicationBean showJobApplicationForm(JobAnnouncementBean jobAnnouncementBean) {

        JobApplicationBean form = new JobApplicationBean();

        form.setJobTitle(jobAnnouncementBean.getJobTitle());
        form.setRecruiterUsername(jobAnnouncementBean.getRecruiterUsername());
        form.setStatus(Status.PENDING);
        form.setStudentUsername(SessionManager.getInstance().getStudentFromSession().obtainUsername());

        form.setCoverLetter("");
        return form;
    }

    public boolean sendAJobApplication(JobApplicationBean jobApplicationBean) throws RecruiterNotFoundException , JobAnnouncementNotFoundException , JobAnnouncementNotActiveException , JobApplicationAlreadySentException {


        // Student who wants to send a job application to a job announcement
        Student student = SessionManager.getInstance().getStudentFromSession();

        //Recruiter who publishes the job announcement
        Recruiter recruiter = recruiterDao.getRecruiter(jobApplicationBean.getRecruiterUsername())
                .orElseThrow(() -> new RecruiterNotFoundException("Error: Recruiter not found."));

        // Job Announcement
        JobAnnouncement jobAnnouncement = jobAnnouncementDao.getJobAnnouncement(jobApplicationBean.getJobTitle(), recruiter)
                .orElseThrow(() -> new JobAnnouncementNotFoundException("Error: JobAnnouncement not found.")); // Job Announcement Found

        // Check if the job announcement is still active
        if (jobAnnouncement.isJobActive() == null || !jobAnnouncement.isJobActive()) {
            throw new JobAnnouncementNotActiveException("This job announcement is no longer active.");
        }

        // Check if the student has already applied for this job
        if (jobApplicationDao.getJobApplication(student, jobAnnouncement).isPresent()) {
            throw new JobApplicationAlreadySentException("You have already applied for this job.");
        }

        JobApplication jobApplication = JobApplicationFactory.createJobApplication(student, jobApplicationBean.getCoverLetter(), jobAnnouncement);
        jobApplicationDao.saveJobApplication(jobApplication); // Persistence



        return true;
    }

    public List<JobApplicationBean> getAllJobApplication(){

        Student student = SessionManager.getInstance().getStudentFromSession();

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
            jobApplication.setCoverLetter(jobApplicationBean.getCoverLetter());  // Modify
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

        if (!jobApplication.obtainStatus().equals(Status.PENDING)) {
            return false; //job application already managed
        }

        jobApplicationDao.deleteJobApplication(jobApplication);

        return true;

    }

    public List<JobApplicationBean> getJobApplicationsForRecruiter(JobAnnouncementBean jobAnnouncementBean){

        Recruiter recruiter = SessionManager.getInstance().getRecruiterFromSession();

        Optional<JobAnnouncement> jobAnnouncementOpt = jobAnnouncementDao.getJobAnnouncement(jobAnnouncementBean.getJobTitle(), recruiter);
        if (jobAnnouncementOpt.isEmpty()) {
            return new ArrayList<>();
        }

        JobAnnouncement jobAnnouncement = jobAnnouncementOpt.get();

        // all the job applications sent to the job announcement
        List<JobApplication> jobApplications = jobApplicationDao.getJobApplicationsByAnnouncement(jobAnnouncement);
        return convertToJobApplicationBeans(jobApplications);

    }


    public boolean updateJobApplicationStatus(JobApplicationBean jobApplicationBean, Status status) throws JobApplicationNotFoundException {


        JobAnnouncement jobAnnouncement = getJobAnnouncementFromBean(jobApplicationBean);

        List<JobApplication> jobApplications = jobApplicationDao.getJobApplicationsByAnnouncement(jobAnnouncement);
        Optional<JobApplication> jobApplicationOpt = jobApplications.stream()
                .filter(jobApplication -> jobApplication.getStudent().obtainUsername().equals(jobApplicationBean.getStudentUsername()))
                .findFirst();
        if (jobApplicationOpt.isEmpty()) {
            throw new JobApplicationNotFoundException("Error: Job Application not found for the specified student.");
        }
        JobApplication jobApplication = jobApplicationOpt.get();
        if (!jobApplication.obtainStatus().equals(Status.PENDING)) {
            return false; // Already Managed
        }
        jobApplication.setStatus(status); // (ACCEPTED or REJECTED)
        jobApplicationDao.updateJobApplication(jobApplication);

        return true;

    }


    private JobAnnouncement getJobAnnouncementFromBean(JobApplicationBean jobApplicationBean) {
        //recruiter from session
        Recruiter recruiter = SessionManager.getInstance().getRecruiterFromSession();

        // job announcement
        Optional<JobAnnouncement> jobAnnouncementOpt = jobAnnouncementDao.getJobAnnouncement(jobApplicationBean.getJobTitle(), recruiter);

        if (jobAnnouncementOpt.isEmpty()) {
            throw new IllegalArgumentException("Error: No job announcement found for the recruiter with the specified title.");
        }

        return jobAnnouncementOpt.get();
    }



    private Optional<JobApplication> getJobApplication(JobApplicationBean jobApplicationBean) {
        Student student = SessionManager.getInstance().getStudentFromSession();
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
        return jobApplication.obtainStatus();

    }

    //method to filter

    private boolean filterByTitle(JobAnnouncement announcement, String jobTitle) {
        return jobTitle == null || jobTitle.isEmpty() || announcement.obtainJobTitle().toLowerCase().contains(jobTitle.toLowerCase());
    }

    private boolean filterByLocation(JobAnnouncement announcement, String location) {
        return location == null || location.isEmpty() || announcement.obtainLocation().toLowerCase().contains(location.toLowerCase());
    }

    private boolean filterByRole(JobAnnouncement announcement, String role) {
        return role == null || role.isEmpty() || announcement.obtainJobRole().toLowerCase().contains(role.toLowerCase());
    }

    private boolean filterByJobType(JobAnnouncement announcement, String jobType) {
        return jobType == null || jobType.isEmpty() || announcement.obtainJobType().toLowerCase().contains(jobType.toLowerCase());
    }

    private boolean filterBySalary(JobAnnouncement announcement, String salary) {
        if (salary == null || salary.isEmpty()) {
            return true; //no filter
        }
        try {
            double salaryFilter = Double.parseDouble(salary);
            return announcement.obtainSalary() >= salaryFilter;
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
            return announcement.obtainWorkingHours() >= workingHoursFilter;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean filterByCompanyName(JobAnnouncement announcement, String companyName) {
        return companyName == null || companyName.isEmpty() || announcement.obtainCompanyName().toLowerCase().contains(companyName.toLowerCase());
    }

}

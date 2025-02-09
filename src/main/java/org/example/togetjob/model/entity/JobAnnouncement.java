package org.example.togetjob.model.entity;

import org.example.togetjob.pattern.subject.JobApplicationCollectionSubjectRecruiter;

public class JobAnnouncement {

    private final String jobTitle;
    private final String jobType;
    private final String role;
    private final String location;
    private final int workingHours;
    private final String companyName;
    private final double salary;
    private String description;
    private Boolean isActive;
    private Recruiter recruiter;
    private final JobApplicationCollectionSubjectRecruiter jobApplicationCollection;

    public JobAnnouncement(String jobTitle, String jobType, String role, String location,
                           int workingHours, String companyName, double salary) {
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.role = role;
        this.location = location;
        this.workingHours = workingHours;
        this.companyName = companyName;
        this.salary = salary;
        this.recruiter = null;

        this.jobApplicationCollection = new JobApplicationCollectionSubjectRecruiter();
    }

    public void setRecruiter(Recruiter recruiter) {
        this.recruiter = recruiter;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public String getJobType() {
        return jobType;
    }

    public String getRole() {
        return role;
    }

    public String getLocation() {
        return location;
    }

    public int getWorkingHours() {
        return workingHours;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getSalary() {
        return salary;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Recruiter getRecruiter() {
        return recruiter;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public JobApplicationCollectionSubjectRecruiter getJobApplicationCollection() {
        return jobApplicationCollection;
    }

    public void setDescription(String description) { this.description = description; }
}
package org.example.togetjob.model.entity;


public class JobAnnouncement {

    private final String jobTitle;
    private final String jobType;
    private final String jobRole;
    private final String location;
    private final int workingHours;
    private final String companyName;
    private final double salary;
    private String description;
    private Boolean isActive;
    private Recruiter recruiter;

    public JobAnnouncement(String jobTitle, String jobType, String jobRole, String location,
                           int workingHours, String companyName, double salary) {
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.jobRole = jobRole;
        this.location = location;
        this.workingHours = workingHours;
        this.companyName = companyName;
        this.salary = salary;
        this.recruiter = null;

    }

    public void setRecruiter(Recruiter recruiter) {
        this.recruiter = recruiter;
    }

    public String obtainJobTitle() {
        return jobTitle;
    }

    public String obtainJobType() {
        return jobType;
    }

    public String obtainJobRole() {
        return jobRole;
    }

    public String obtainLocation() {
        return location;
    }

    public int obtainWorkingHours() {
        return workingHours;
    }

    public String obtainCompanyName() {
        return companyName;
    }

    public double obtainSalary() {
        return salary;
    }

    public String obtainDescription() {
        return description;
    }

    public Boolean isJobActive() {
        return isActive;
    }

    public Recruiter getRecruiter() {
        return recruiter;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public void setDescription(String description) { this.description = description; }
}
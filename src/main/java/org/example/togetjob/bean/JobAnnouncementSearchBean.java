package org.example.togetjob.bean;

public class JobAnnouncementSearchBean {

    private String jobTitle;
    private final String jobType;
    private String role;
    private String location;
    private final String workingHours;
    private final String companyName;
    private final String salary;

    public JobAnnouncementSearchBean(String jobTitle, String jobType, String role, String location, String workingHours, String companyName, String salary) {
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.role = role;
        this.location = location;
        this.workingHours = workingHours;
        this.companyName = companyName;
        this.salary = salary;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getJobType() {
        return jobType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getSalary() {
        return salary;
    }
}

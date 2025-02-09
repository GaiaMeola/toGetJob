package org.example.togetjob.bean;

public class JobAnnouncementBean {

    private String jobTitle;
    private String jobType;
    private String role;
    private String location;
    private String workingHours;
    private String companyName;
    private String salary;
    private String description;
    private boolean isActive;
    private String recruiterUsername;

    public JobAnnouncementBean(){
        this.jobTitle = null;
        this.jobType = null;
        this.role = null;
        this.location = null;
        this.workingHours = null;
        this.companyName = null;
        this.salary = null;
        this.description = null;
        this.recruiterUsername = null;
        this.isActive = true;
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

    public void setJobType(String jobType) {
        this.jobType = jobType;
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

    public void setWorkingHours(String workingHours) {
        this.workingHours = workingHours;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getRecruiterUsername() {
        return recruiterUsername;
    }

    public void setRecruiterUsername(String recruiterUsername) {
        this.recruiterUsername = recruiterUsername;
    }
}

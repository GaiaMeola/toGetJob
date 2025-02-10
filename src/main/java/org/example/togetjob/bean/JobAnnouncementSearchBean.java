package org.example.togetjob.bean;

public class JobAnnouncementSearchBean {

    private String jobTitle;
    private String jobType;
    private String role;
    private String location;
    private String workingHours;
    private String companyName;
    private String salary;

    public JobAnnouncementSearchBean() {
        /* builder */
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

    public void setJobType(String jobType) { this.jobType = jobType; }

    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }

    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public void setSalary(String salary) { this.salary = salary; }
}

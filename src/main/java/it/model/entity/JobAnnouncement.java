package it.model.entity;

public class JobAnnouncement {

    private String jobTitle;
    private String jobType;
    private String role;
    private String location;
    private int workingHours;
    private String companyName;
    private double salary;
    private String description;

    public JobAnnouncement(String jobTitle, String jobType, String role, String location, int workingHours, String companyName, double salary, String description) {
        this.jobTitle = jobTitle;
        this.jobType = jobType;
        this.role = role;
        this.location = location;
        this.workingHours = workingHours;
        this.companyName = companyName;
        this.salary = salary;
        this.description = description;
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

}

package org.example.togetjob.bean;

import org.example.togetjob.exceptions.*;

public class JobAnnouncementBaseBean {

    protected String jobTitle;
    protected String jobType;
    protected String role;
    protected String location;
    protected String workingHours;
    protected String companyName;
    protected String salary;

    public JobAnnouncementBaseBean() {
        // Default constructor
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        if (jobTitle == null || jobTitle.trim().isEmpty()) {
            throw new InvalidJobTitleException("Job Title cannot be empty or null.");
        }
        this.jobTitle = jobTitle;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        if (jobType == null || jobType.trim().isEmpty()) {
            throw new InvalidJobTypeException("Job Type cannot be empty or null.");
        }
        if (!jobType.equalsIgnoreCase("part-time") && !jobType.equalsIgnoreCase("full-time")) {
            throw new InvalidJobTypeException("Job Type must be either 'part-time' or 'full-time'.");
        }
        this.jobType = jobType;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (role == null || role.trim().isEmpty()) {
            throw new InvalidJobTitleException("Role cannot be empty or null.");
        }
        this.role = role;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        if (location == null || location.trim().isEmpty()) {
            throw new InvalidJobTitleException("Location cannot be empty or null.");
        }
        this.location = location;
    }

    public String getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(String workingHours) {
        if (workingHours == null || workingHours.trim().isEmpty()) {
            throw new InvalidWorkingHoursException("Working Hours cannot be empty or null.");
        }
        try {
            int hours = Integer.parseInt(workingHours);
            if (hours <= 0 || hours >= 24) {
                throw new InvalidWorkingHoursException("Working Hours must be a positive integer less than 24.");
            }
        } catch (NumberFormatException e) {
            throw new InvalidWorkingHoursException("Working Hours must be a valid integer.");
        }
        this.workingHours = workingHours;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        if (companyName == null || companyName.trim().isEmpty()) {
            throw new InvalidJobTitleException("Company Name cannot be empty or null.");
        }
        this.companyName = companyName;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        if (salary == null || salary.trim().isEmpty()) {
            throw new InvalidSalaryException("Salary cannot be empty or null.");
        }
        try {
            double salaryValue = Double.parseDouble(salary);
            if (salaryValue <= 0) {
                throw new InvalidSalaryException("Salary must be a positive number.");
            }
        } catch (NumberFormatException e) {
            throw new InvalidSalaryException("Salary must be a valid number.");
        }
        this.salary = salary;
    }
}

package org.example.togetjob.bean;

public class JobAnnouncementSearchBean extends JobAnnouncementBaseBean {

    public JobAnnouncementSearchBean() {
        super();
    }

    @Override
    public void setJobTitle(String jobTitle) {
        if (jobTitle != null && !jobTitle.trim().isEmpty()) {
            super.setJobTitle(jobTitle);
        } else {
            this.jobTitle = null;
        }
    }

    @Override
    public void setJobType(String jobType) {
        if (jobType != null && !jobType.trim().isEmpty()) {
            super.setJobType(jobType);
        } else {
            this.jobType = null;
        }
    }

    @Override
    public void setRole(String role) {
        if (role != null && !role.trim().isEmpty()) {
            super.setRole(role);
        } else {
            this.role = null;
        }
    }

    @Override
    public void setLocation(String location) {
        if (location != null && !location.trim().isEmpty()) {
            super.setLocation(location);
        } else {
            this.location = null;
        }
    }

    @Override
    public void setWorkingHours(String workingHours) {
        if (workingHours != null && !workingHours.trim().isEmpty()) {
            super.setWorkingHours(workingHours);
        } else {
            this.workingHours = null;
        }
    }

    @Override
    public void setCompanyName(String companyName) {
        if (companyName != null && !companyName.trim().isEmpty()) {
            super.setCompanyName(companyName);
        } else {
            this.companyName = null;
        }
    }

    @Override
    public void setSalary(String salary) {
        if (salary != null && !salary.trim().isEmpty()) {
            super.setSalary(salary);
        } else {
            this.salary = null;
        }
    }
}

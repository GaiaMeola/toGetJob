package org.example.togetjob.bean;

public class JobAnnouncementBean extends JobAnnouncementBaseBean {

    private String description;
    private boolean isActive;
    private String recruiterUsername;

    public JobAnnouncementBean() {
        this.isActive = true;
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

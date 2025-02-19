package org.example.togetjob.bean;

import org.example.togetjob.exceptions.InvalidJobDescriptionException;

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
        if (description == null || description.trim().isEmpty()) {
            throw new InvalidJobDescriptionException("Description cannot be empty or null.");
        }
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

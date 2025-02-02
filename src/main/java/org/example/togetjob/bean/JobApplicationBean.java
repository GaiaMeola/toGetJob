package org.example.togetjob.bean;

import org.example.togetjob.model.entity.Status;

public class JobApplicationBean {

    private String jobTitle;
    private String studentUsername;
    private String coverLetter;
    private String recruiterUsername;
    private Status status ;

    public JobApplicationBean(String jobTitle, String studentUsername, String coverLetter, String recruiterUsername) {
        this.jobTitle = jobTitle;
        this.studentUsername = studentUsername;
        this.coverLetter = coverLetter;
        this.recruiterUsername = recruiterUsername;
    }

    public JobApplicationBean() {
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getRecruiterUsername() {
        return recruiterUsername;
    }

    public void setRecruiterUsername(String recruiterUsername) {
        this.recruiterUsername = recruiterUsername;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status= status;
    }
}

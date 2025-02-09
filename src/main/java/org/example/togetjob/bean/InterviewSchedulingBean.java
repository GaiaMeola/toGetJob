package org.example.togetjob.bean;

public class InterviewSchedulingBean {

    private String studentUsername;
    private String jobTitle;
    private String companyName;
    private String interviewDateTime;
    private String location;

    public InterviewSchedulingBean() {
        this.studentUsername = null;
        this.jobTitle = null;
        this.companyName = null;
        this.interviewDateTime = null;
        this.location = null;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getInterviewDateTime() {
        return interviewDateTime;
    }

    public void setInterviewDateTime(String interviewDateTime) {
        this.interviewDateTime = interviewDateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}

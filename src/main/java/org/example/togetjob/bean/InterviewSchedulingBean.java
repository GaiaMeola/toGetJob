package org.example.togetjob.bean;

public class InterviewSchedulingBean {

    private String studentUsername;
    private String jobTitle;
    private String companyName;
    private String interviewDateTime;
    private String location;

    public InterviewSchedulingBean() {
    }

    public InterviewSchedulingBean(String studentUsername, String jobTitle, String companyName, String interviewDateTime, String location) {
        this.studentUsername = studentUsername;
        this.jobTitle = jobTitle;
        this.companyName = companyName;
        this.interviewDateTime = interviewDateTime;
        this.location = location;
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

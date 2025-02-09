package org.example.togetjob.bean;

public class InterviewSchedulingStudentInfoBean {

    private String subject;
    private String greeting;
    private String introduction;
    private String jobTitle;
    private String companyName;
    private String interviewDateTime;
    private String location;
    private String studentUsername;

    public InterviewSchedulingStudentInfoBean() {

        this.subject = null;
        this.greeting = null;
        this.introduction = null;
        this.jobTitle = null;
        this.companyName = null;
        this.interviewDateTime = null;
        this.location = null;
        this. studentUsername = null;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public void setInterviewDateTime(String interviewDateTime) {
        this.interviewDateTime = interviewDateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getGreeting() {
        return greeting;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getInterviewDateTime() {
        return interviewDateTime;
    }

    public String getStudentUsername() {
        return studentUsername;
    }
}

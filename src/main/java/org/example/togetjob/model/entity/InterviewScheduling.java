package org.example.togetjob.model.entity;

import java.time.LocalDateTime;

public class InterviewScheduling {

    private final String subject;
    private final String greeting;
    private final String introduction;
    private LocalDateTime interviewDateTime;
    private String location;
    private Student candidate;
    private JobAnnouncement jobAnnouncement;

    public InterviewScheduling(Student candidate, JobAnnouncement jobAnnouncement, String jobTitle, String companyName, LocalDateTime interviewDateTime, String location) {
        this.subject = "Invitation for Interview – " + jobTitle + " at " + companyName;
        this.greeting = "Dear " + candidate.obtainName() + ",";
        this.introduction = "We are pleased to inform you that after reviewing your application for the " + jobTitle + " position, we would like to invite you to an interview.";
        this.interviewDateTime = interviewDateTime;
        this.location = location;
        this.candidate = candidate;
        this.jobAnnouncement = jobAnnouncement;
    }

    public InterviewScheduling(String subject, String greeting, String introduction, LocalDateTime interviewDateTime, String location, Student candidate, JobAnnouncement jobAnnouncement) {
        this.subject = subject;
        this.greeting = greeting;
        this.introduction = introduction;
        this.interviewDateTime = interviewDateTime;
        this.location = location;
        this.candidate = candidate;
        this.jobAnnouncement = jobAnnouncement;
    }

    public Student getCandidate() {
        return candidate;
    }

    public String obtainLocation() {
        return location;
    }

    public LocalDateTime obtainInterviewDateTime() {
        return interviewDateTime;
    }

    public String obtainIntroduction() {
        return introduction;
    }

    public String obtainGreeting() {
        return greeting;
    }

    public String obtainSubject() {
        return subject;
    }

    public JobAnnouncement getJobAnnouncement() { return jobAnnouncement;}

    public void setJobAnnouncement(JobAnnouncement jobAnnouncement) {
        this.jobAnnouncement = jobAnnouncement;
    }

    public void setInterviewDateTime(LocalDateTime interviewDateTime) {
        this.interviewDateTime = interviewDateTime;
    }

    public void setLocation(String location) { this.location = location; }

    public void setCandidate(Student candidate) { this.candidate = candidate; }
}

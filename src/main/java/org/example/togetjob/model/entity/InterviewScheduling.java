package org.example.togetjob.model.entity;

import java.time.LocalDateTime;

public class InterviewScheduling {

    private final String subject;
    private final String greeting;
    private final String introduction;
    private final LocalDateTime interviewDateTime;
    private final String location;
    private final Student candidate;
    private final JobAnnouncement jobAnnouncement;

    public InterviewScheduling(Student candidate, JobAnnouncement jobAnnouncement, String jobTitle, String companyName, LocalDateTime interviewDateTime, String location) {
        this.subject = "Invitation for Interview – " + jobTitle + " at " + companyName;
        this.greeting = "Dear " + candidate.getName() + ",";
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

    public String getLocation() {
        return location;
    }

    public LocalDateTime getInterviewDateTime() {
        return interviewDateTime;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getGreeting() {
        return greeting;
    }

    public String getSubject() {
        return subject;
    }

    public JobAnnouncement getJobAnnouncement() { return jobAnnouncement;}
}

package org.example.togetjob.model.entity;

import java.time.LocalDateTime;

public class InterviewScheduling {

    private String subject;
    private String greeting;
    private String introduction;
    private LocalDateTime interviewDateTime;
    private String location;
    private Student candidate;

    public InterviewScheduling(Student candidate, String jobTitle, String companyName, LocalDateTime interviewDateTime, String location) {
        this.subject = "Invitation for Interview – " + jobTitle + " at " + companyName;
        this.greeting = "Dear " + candidate.getName() + ",";
        this.introduction = "We are pleased to inform you that after reviewing your application for the " + jobTitle + " position, we would like to invite you to an interview.";
        this.interviewDateTime = interviewDateTime;
        this.location = location;
        this.candidate = candidate;
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
}

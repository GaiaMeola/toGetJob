package org.example.togetjob.model.entity;

import java.time.LocalDate;

public class JobApplication {

    private LocalDate applicationDate;
    private Student student;
    private Status status;
    private String coverLetter;
    private JobAnnouncement jobAnnouncement;

    public JobApplication(Student student, String coverLetter, JobAnnouncement jobAnnouncement) {
        this.applicationDate = LocalDate.now();
        this.student = student;
        this.status = Status.PENDING;
        this.coverLetter = coverLetter;
        this.jobAnnouncement = jobAnnouncement;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDate applicationDate) {
        this.applicationDate = applicationDate;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }

    public JobAnnouncement getJobAnnouncement() {
        return jobAnnouncement;
    }

    public void setJobAnnouncement(JobAnnouncement jobAnnouncement) {
        this.jobAnnouncement = jobAnnouncement;
    }
}

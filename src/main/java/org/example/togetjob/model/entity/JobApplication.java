package org.example.togetjob.model.entity;

import java.time.LocalDate;

public class JobApplication {

    private final LocalDate applicationDate;
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

    public JobApplication(LocalDate localDate , Student student, Status status , String coverLetter, JobAnnouncement jobAnnouncement) {
        this.applicationDate = localDate;
        this.student = student;
        this.status = status;
        this.coverLetter = coverLetter;
        this.jobAnnouncement = jobAnnouncement;
    }

    public JobApplication(LocalDate applicationDate, Student student, Status status, String coverLetter) {
        this.applicationDate = applicationDate;
        this.student = student;
        this.status = status;
        this.coverLetter = coverLetter;
        this.jobAnnouncement = null;
    }

    public JobApplication(LocalDate applicationDate, Status status, String coverLetter, JobAnnouncement jobAnnouncement) {
        this.applicationDate = applicationDate;
        this.status = status;
        this.coverLetter = coverLetter;
        this.jobAnnouncement = jobAnnouncement;
    }

    public LocalDate getApplicationDate() {
        return applicationDate;
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

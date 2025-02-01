package org.example.togetjob.model.entity;

import java.time.LocalDate;

public class JobApplication {

    private LocalDate applicationDate;
    private Student student;
    private Status status;
    private String coverLetter;


    public JobApplication(LocalDate applicationDate, Student student, Status status, String coverLetter) {
        this.applicationDate = applicationDate;
        this.student = student;
        this.status = status;
        this.coverLetter = coverLetter;
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
}

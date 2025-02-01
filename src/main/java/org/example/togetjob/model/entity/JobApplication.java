package org.example.togetjob.model.entity;

import java.time.LocalDate;

public class JobApplication {

    private LocalDate applicationDate;
    private Student student;
    private Status status;
    private JobApplication jobApplication ;

    public JobApplication(LocalDate applicationDate, Student student, Status status ) {
        this.applicationDate = applicationDate;
        this.student = student;
        this.status = Status.PENDING;

    }

    public LocalDate getApplicationDate() {
        return applicationDate;
    }

    public Student getStudent() {
        return student;
    }

    public Status getStatus() {
        return status;
    }

    pu

}

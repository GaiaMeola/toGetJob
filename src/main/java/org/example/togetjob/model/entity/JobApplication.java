package org.example.togetjob.model.entity;

import java.time.LocalDate;

public class JobApplication {

    private LocalDate applicationDate;
    private Student student;
    private Status status;

    public JobApplication(LocalDate applicationDate, Student student, Status status) {
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

    public void manageJobApplication(Status newStatus){
        if(this.status == Status.PENDING){
            if(newStatus == Status.ACCEPTED || newStatus == Status.REJECTED){
                this.status = newStatus;
                System.out.println("Candidatura gestita con successo.");
            } else{
                System.out.println("Stato non valido per la candidatura.");
            }
        } else {
            System.out.println("Impossibile modificare lo stato della candidatura.");
        }

    }

}

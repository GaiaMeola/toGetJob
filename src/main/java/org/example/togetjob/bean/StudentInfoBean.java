package org.example.togetjob.bean;

import java.time.LocalDate;

public class StudentInfoBean extends StudentInfoBaseBean {

    private String username;
    private LocalDate dateOfBirth;
    private String phoneNumber;

    public StudentInfoBean() {
        super();
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

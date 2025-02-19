package org.example.togetjob.bean;

import org.example.togetjob.exceptions.InvalidDateOfBirthException;
import org.example.togetjob.exceptions.InvalidPhoneNumberException;

import java.time.LocalDate;
import java.time.Period;

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

    public void setDateOfBirth(LocalDate dateOfBirth) throws InvalidDateOfBirthException {
        if (dateOfBirth == null) {
            throw new InvalidDateOfBirthException("Date of birth cannot be null.");
        }

        if (dateOfBirth.isAfter(LocalDate.now())) {
            throw new InvalidDateOfBirthException("Date of birth cannot be in the future.");
        }

        int age = Period.between(dateOfBirth, LocalDate.now()).getYears();
        if (age < 18) {
            throw new InvalidDateOfBirthException("You must be at least 18 years old.");
        }

        this.dateOfBirth = dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) throws InvalidPhoneNumberException {
        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            throw new InvalidPhoneNumberException("Phone number cannot be empty.");
        }

        if (!phoneNumber.matches("\\d{10,15}")) {
            throw new InvalidPhoneNumberException("Phone number must be between 10 and 15 digits and contain only numbers.");
        }

        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

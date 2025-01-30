package it.model.entity;

import java.time.LocalDate;
import java.util.List;

public class Student extends User{

    private LocalDate dateOfBirth;
    private String phoneNumber;
    private List<String> degrees;
    private List<String> courseAttended;
    private List<String> certifications;
    private List<String> workExperiences;
    private List<String> skills;
    private String availability;
    private List<JobApplication> jobApplications;

    public Student(String username, String password, String name, String surname, String emailAddress, Role role) {
        super(name, surname, username, emailAddress, password, role); // Chiamata alla superclasse User
    }

    public Student(String name, String surname, String username, String emailAddress, String password, Role role, LocalDate dateOfBirth, String phoneNumber, List<String> degrees, List<String> courseAttended, List<String> certifications, List<String> workExperiences, List<String> skills, String availability, List<JobApplication> jobApplications) {
        super(name, surname, username, emailAddress, password, role);
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.degrees = degrees;
        this.courseAttended = courseAttended;
        this.certifications = certifications;
        this.workExperiences = workExperiences;
        this.skills = skills;
        this.availability = availability;
        this.jobApplications = jobApplications;
    }


    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public List<String> getDegrees() {
        return degrees;
    }

    public List<String> getCourseAttended() {
        return courseAttended;
    }

    public List<String> getCertifications() {
        return certifications;
    }

    public List<String> getWorkExperiences() {
        return workExperiences;
    }

    public List<String> getSkills() {
        return skills;
    }

    public String getAvailability() {
        return availability;
    }

    public List<JobApplication> getJobApplications() {
        return jobApplications;
    }
}

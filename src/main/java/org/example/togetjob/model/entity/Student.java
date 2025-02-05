package org.example.togetjob.model.entity;

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


    public Student(String name, String surname, String username, String emailAddress, String password, Role role) {
        super(name, surname, username, emailAddress, password, role);
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

    @Override
    public String toString() {
        return "Student{" +
                "username='" + getUsername() + '\'' +
                ", email='" + getEmailAddress() + '\'' +
                ", degrees=" + degrees +
                '}';
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDegrees(List<String> degrees) {
        this.degrees = degrees;
    }

    public void setCourseAttended(List<String> courseAttended) {
        this.courseAttended = courseAttended;
    }

    public void setCertifications(List<String> certifications) {
        this.certifications = certifications;
    }

    public void setWorkExperiences(List<String> workExperiences) {
        this.workExperiences = workExperiences;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public void setJobApplications(List<JobApplication> jobApplications) {
        this.jobApplications = jobApplications;
    }
}

package org.example.togetjob.bean;

import java.time.LocalDate;
import java.util.List;

public class StudentInfoBean {

    private String username;
    private LocalDate dateOfBirth;
    private String phoneNumber;
    private List<String> degrees;
    private List<String> courseAttended;
    private List<String> certifications;
    private List<String> workExperiences;
    private List<String> skills;
    private String availability;

    public StudentInfoBean(LocalDate dateOfBirth, String phoneNumber, List<String> degrees, List<String> courseAttended, List<String> certifications, List<String> workExperiences, List<String> skills, String availability) {
        this.dateOfBirth = dateOfBirth;
        this.phoneNumber = phoneNumber;
        this.degrees = degrees;
        this.courseAttended = courseAttended;
        this.certifications = certifications;
        this.workExperiences = workExperiences;
        this.skills = skills;
        this.availability = availability;
    }

    public StudentInfoBean() {
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

    public List<String> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<String> degrees) {
        this.degrees = degrees;
    }

    public List<String> getCourseAttended() {
        return courseAttended;
    }

    public void setCourseAttended(List<String> courseAttended) {
        this.courseAttended = courseAttended;
    }

    public List<String> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<String> certifications) {
        this.certifications = certifications;
    }

    public List<String> getWorkExperiences() {
        return workExperiences;
    }

    public void setWorkExperiences(List<String> workExperiences) {
        this.workExperiences = workExperiences;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

package org.example.togetjob.model.entity;

import java.time.LocalDate;
import java.util.List;

public class Student extends User{

    private LocalDate dateOfBirth;
    private String phoneNumber;
    private List<String> degrees;
    private List<String> coursesAttended;
    private List<String> certifications;
    private List<String> workExperiences;
    private List<String> skills;
    private String availability;
    private List<JobApplication> jobApplications;

    public Student(String name, String surname, String username, String emailAddress, String password, Role role) {
        super(name, surname, username, emailAddress, password, role); // User
    }

    public Student(String name, String surname, String username, String emailAddress, String password, Role role, LocalDate dateOfBirth) {
        super(name, surname, username, emailAddress, password, role);
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate obtainDateOfBirth() {
        return dateOfBirth;
    }

    public String obtainPhoneNumber() {
        return phoneNumber;
    }

    public List<String> obtainDegrees() {
        return degrees;
    }

    public List<String> obtainCoursesAttended() {
        return coursesAttended;
    }

    public List<String> obtainCertifications() {
        return certifications;
    }

    public List<String> obtainWorkExperiences() {
        return workExperiences;
    }

    public List<String> obtainSkills() {
        return skills;
    }

    public String obtainAvailability() {
        return availability;
    }

    public List<JobApplication> obtainJobApplications() {
        return jobApplications;
    }

    public void setJobApplications(List<JobApplication> jobApplications) {
        this.jobApplications = jobApplications;
    }

    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void setDegrees(List<String> degrees) { this.degrees = degrees; }

    public void setCoursesAttended(List<String> coursesAttended) { this.coursesAttended = coursesAttended; }

    public void setCertifications(List<String> certifications) { this.certifications = certifications; }

    public void setWorkExperiences(List<String> workExperiences) { this.workExperiences = workExperiences; }

    public void setSkills(List<String> skills) { this.skills = skills; }

    public void setAvailability(String availability) { this.availability = availability; }

    //public void removeJobApplication(JobApplication jobApplication){}

    //public void addJobApplication(JobApplication jobApplication){}

}

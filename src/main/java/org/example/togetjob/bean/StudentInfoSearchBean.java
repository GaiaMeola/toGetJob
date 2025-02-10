package org.example.togetjob.bean;

import java.util.List;

public class StudentInfoSearchBean {

    private List<String> degrees;
    private List<String> coursesAttended;
    private List<String> certifications;
    private List<String> workExperiences;
    private List<String> skills;
    private String availability;

    public StudentInfoSearchBean() {
        /* builder */
    }

    public List<String> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<String> degrees) {
        this.degrees = degrees;
    }

    public List<String> getCoursesAttended() {
        return coursesAttended;
    }

    public void setCoursesAttended(List<String> coursesAttended) {
        this.coursesAttended = coursesAttended;
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
}

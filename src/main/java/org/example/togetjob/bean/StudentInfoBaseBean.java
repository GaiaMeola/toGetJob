package org.example.togetjob.bean;

import org.example.togetjob.exceptions.*;

import java.util.ArrayList;
import java.util.List;

public abstract class StudentInfoBaseBean {

    protected List<String> degrees;
    protected List<String> coursesAttended;
    protected List<String> certifications;
    protected List<String> workExperiences;
    protected List<String> skills;
    protected String availability;

    protected StudentInfoBaseBean() {
        /* builder */
    }

    // Getter e Setter per degrees
    public List<String> getDegrees() {
        return degrees;
    }

    public void setDegrees(List<String> degreesList) throws InvalidDegreeListException {
        if (degreesList == null || degreesList.isEmpty()) {
            throw new InvalidDegreeListException("Degrees list must contain at least one degree.");
        }

        List<String> mutableList = new ArrayList<>(degreesList);
        mutableList.replaceAll(String::trim);

        if (mutableList.stream().anyMatch(String::isEmpty)) {
            throw new InvalidDegreeListException("Degree list must contain non-empty items.");
        }

        this.degrees = mutableList;
    }

    // Getter e Setter per coursesAttended
    public List<String> getCoursesAttended() {
        return coursesAttended;
    }

    public void setCoursesAttended(List<String> coursesList) throws InvalidCourseListException {
        if (coursesList == null || coursesList.isEmpty()) {
            throw new InvalidCourseListException("Courses attended list must contain at least one course.");
        }

        List<String> mutableList = new ArrayList<>(coursesList);
        mutableList.replaceAll(String::trim);

        if (mutableList.stream().anyMatch(String::isEmpty)) {
            throw new InvalidCourseListException("Course list must contain non-empty items.");
        }

        this.coursesAttended = mutableList;
    }

    // Getter e Setter per certifications
    public List<String> getCertifications() {
        return certifications;
    }

    public void setCertifications(List<String> certificationsList) throws InvalidCertificationListException {
        if (certificationsList == null || certificationsList.isEmpty()) {
            throw new InvalidCertificationListException("Certifications list must contain at least one certification.");
        }

        List<String> mutableList = new ArrayList<>(certificationsList);
        mutableList.replaceAll(String::trim);

        if (mutableList.stream().anyMatch(String::isEmpty)) {
            throw new InvalidCertificationListException("Certification list must contain non-empty items.");
        }

        this.certifications = mutableList;
    }

    // Getter e Setter per workExperiences
    public List<String> getWorkExperiences() {
        return workExperiences;
    }

    public void setWorkExperiences(List<String> workExperienceList) throws InvalidWorkExperienceListException {
        if (workExperienceList == null || workExperienceList.isEmpty()) {
            throw new InvalidWorkExperienceListException("Work experience list must contain at least one work experience.");
        }

        List<String> mutableList = new ArrayList<>(workExperienceList);
        mutableList.replaceAll(String::trim);

        if (mutableList.stream().anyMatch(String::isEmpty)) {
            throw new InvalidWorkExperienceListException("Work experience list must contain non-empty items.");
        }

        this.workExperiences = mutableList;
    }

    // Getter e Setter per skills
    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skillsList) throws InvalidSkillListException {
        if (skillsList == null || skillsList.isEmpty()) {
            throw new InvalidSkillListException("Skills list must contain at least one skill.");
        }

        List<String> mutableList = new ArrayList<>(skillsList);
        mutableList.replaceAll(String::trim);

        if (mutableList.stream().anyMatch(String::isEmpty)) {
            throw new InvalidSkillListException("Skills list must contain non-empty items.");
        }

        this.skills = mutableList;
    }

    // Getter e Setter per availability
    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) throws InvalidAvailabilityException {
        if (availability != null && !availability.isEmpty()) {
            availability = availability.trim().toLowerCase();
            if (!availability.equalsIgnoreCase("full-time") && !availability.equalsIgnoreCase("part-time")) {
                throw new InvalidAvailabilityException("Availability must be either 'full-time' or 'part-time'.");
            }
        }
        this.availability = availability;
    }
}

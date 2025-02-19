package org.example.togetjob.bean;

import java.util.List;

public class StudentInfoSearchBean extends StudentInfoBaseBean {

    public StudentInfoSearchBean() {
        super();
    }

    @Override
    public void setDegrees(List<String> degreesList) {
        if (degreesList != null && !degreesList.isEmpty()) {
            try {
                super.setDegrees(degreesList);
            } catch (Exception e) {
                this.degrees = null;
            }
        } else {
            this.degrees = null;
        }
    }

    @Override
    public void setCoursesAttended(List<String> coursesList) {
        if (coursesList != null && !coursesList.isEmpty()) {
            try {
                super.setCoursesAttended(coursesList);
            } catch (Exception e) {
                this.coursesAttended = null;
            }
        } else {
            this.coursesAttended = null;
        }
    }

    @Override
    public void setCertifications(List<String> certificationsList) {
        if (certificationsList != null && !certificationsList.isEmpty()) {
            try {
                super.setCertifications(certificationsList);
            } catch (Exception e) {
                this.certifications = null;
            }
        } else {
            this.certifications = null;
        }
    }

    @Override
    public void setWorkExperiences(List<String> workExperienceList) {
        if (workExperienceList != null && !workExperienceList.isEmpty()) {
            try {
                super.setWorkExperiences(workExperienceList);
            } catch (Exception e) {
                this.workExperiences = null;
            }
        } else {
            this.workExperiences = null;
        }
    }

    @Override
    public void setSkills(List<String> skillsList) {
        if (skillsList != null && !skillsList.isEmpty()) {
            try {
                super.setSkills(skillsList);
            } catch (Exception e) {
                this.skills = null;
            }
        } else {
            this.skills = null;
        }
    }

    @Override
    public void setAvailability(String availability) {
        if (availability != null && !availability.trim().isEmpty()) {
            try {
                super.setAvailability(availability);
            } catch (Exception e) {
                this.availability = null;
            }
        } else {
            this.availability = null;
        }
    }
}

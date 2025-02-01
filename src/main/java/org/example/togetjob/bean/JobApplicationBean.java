package org.example.togetjob.bean;

import org.example.togetjob.model.entity.Student;

public class JobApplicationBean {

    private String jobTitle;
    private String studentUsername;
    private String coverLetter;


    public String getStudentUsername() {
        return studentUsername;
    }

    public void setStudentUsername(String studentUsername) {
        this.studentUsername = studentUsername;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }
}

package org.example.togetjob.model.factory;

import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.model.entity.Role;
import org.example.togetjob.model.entity.Student;

public class StudentFactory {

    private StudentFactory() {
    }

    public static Student createStudent(RegisterUserBean userBean, StudentInfoBean studentInfoBean) {
        Student student = new Student(
                userBean.getName(),
                userBean.getSurname(),
                userBean.getUsername(),
                userBean.getEmailAddress(),
                userBean.getPassword(),
                Role.STUDENT,
                studentInfoBean.getDateOfBirth()
        );

        student.setPhoneNumber(studentInfoBean.getPhoneNumber());
        student.setDegrees(studentInfoBean.getDegrees());
        student.setCoursesAttended(studentInfoBean.getCoursesAttended());
        student.setCertifications(studentInfoBean.getCertifications());
        student.setWorkExperiences(studentInfoBean.getWorkExperiences());
        student.setSkills(studentInfoBean.getSkills());
        student.setAvailability(studentInfoBean.getAvailability());
        student.setJobApplications(null);

        return student;
    }
}

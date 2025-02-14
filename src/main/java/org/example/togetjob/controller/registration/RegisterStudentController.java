package org.example.togetjob.controller.registration;

import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.dao.abstractobjects.StudentDao;
import org.example.togetjob.model.entity.Role;
import org.example.togetjob.model.entity.Student;
import org.example.togetjob.model.entity.User;

public class RegisterStudentController extends AbstractRegisterController{

    private final StudentDao studentDao;
    private final StudentInfoBean studentInfoBean;

    public RegisterStudentController(StudentInfoBean studentInfoBean){
        this.studentDao = AbstractFactoryDaoSingleton.getFactoryDao().createStudentDao();
        this.studentInfoBean = studentInfoBean;
    }

    @Override
    protected User createUser(RegisterUserBean userBean) {

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

    @Override
    protected void saveUserSpecificData(User user) {
        studentDao.saveStudent((Student) user);
    }
}

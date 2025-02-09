package org.example.togetjob.controller.registration;

import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.StudentDao;
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
        return new Student(
                userBean.getName(), userBean.getSurname(),userBean.getUsername(), userBean.getEmailAddress(), userBean.getPassword(), Role.STUDENT,
                studentInfoBean.getDateOfBirth(), studentInfoBean.getPhoneNumber(),
                studentInfoBean.getDegrees(), studentInfoBean.getCoursesAttended(),
                studentInfoBean.getCertifications(), studentInfoBean.getWorkExperiences(),
                studentInfoBean.getSkills(), studentInfoBean.getAvailability(), null // No jobApplication
        );
    }

    @Override
    protected void saveUserSpecificData(User user) {
        studentDao.saveStudent((Student) user);
    }
}

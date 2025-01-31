package it.controller.registration;

import it.bean.RegisterUserBean;
import it.bean.StudentInfoBean;
import it.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import it.model.dao.abstractobjects.StudentDao;
import it.model.entity.Role;
import it.model.entity.Student;
import it.model.entity.User;

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
                userBean.getUsername(), userBean.getPassword(), userBean.getName(), userBean.getSurname(),
                userBean.getEmailAddress(), Role.STUDENT,
                studentInfoBean.getDateOfBirth(), studentInfoBean.getPhoneNumber(),
                studentInfoBean.getDegrees(), studentInfoBean.getCourseAttended(),
                studentInfoBean.getCertifications(), studentInfoBean.getWorkExperiences(),
                studentInfoBean.getSkills(), studentInfoBean.getAvailability(), null // No jobApplication
        );
    }

    @Override
    protected void saveUserSpecificData(User user) {
        studentDao.saveStudent((Student) user);
    }
}

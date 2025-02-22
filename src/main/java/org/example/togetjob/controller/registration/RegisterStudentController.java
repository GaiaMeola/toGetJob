package org.example.togetjob.controller.registration;

import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.dao.abstractobjects.StudentDao;
import org.example.togetjob.model.entity.Student;
import org.example.togetjob.model.entity.User;
import org.example.togetjob.model.factory.StudentFactory;

public class RegisterStudentController extends AbstractRegisterController{

    private final StudentDao studentDao;
    private final StudentInfoBean studentInfoBean;

    public RegisterStudentController(StudentInfoBean studentInfoBean){
        this.studentDao = AbstractFactoryDaoSingleton.getFactoryDao().createStudentDao();
        this.studentInfoBean = studentInfoBean;
    }

    @Override
    protected User createUser(RegisterUserBean userBean) {
        return StudentFactory.createStudent(userBean, studentInfoBean);
    }

    @Override
    protected void saveUserSpecificData(User user) {
        studentDao.saveStudent((Student) user);
    }
}

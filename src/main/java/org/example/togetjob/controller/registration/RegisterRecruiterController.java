package org.example.togetjob.controller.registration;

import org.example.togetjob.bean.RecruiterInfoBean;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.exceptions.DatabaseException;
import org.example.togetjob.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.entity.User;
import org.example.togetjob.model.factory.RecruiterFactory;

public class RegisterRecruiterController extends AbstractRegisterController{

    private final RecruiterDao recruiterDao;
    private final RecruiterInfoBean recruiterInfoBean;

    public RegisterRecruiterController(RecruiterInfoBean recruiterInfoBean) throws DatabaseException {
        this.recruiterDao = AbstractFactoryDaoSingleton.getFactoryDao().createRecruiterDao();
        this.recruiterInfoBean = recruiterInfoBean;
    }

    @Override
    protected User createUser(RegisterUserBean userBean) {
        return RecruiterFactory.createRecruiter(
                userBean.getName(),
                userBean.getSurname(),
                userBean.getUsername(),
                userBean.getEmailAddress(),
                userBean.getPassword(),
                recruiterInfoBean.getCompanies()
        );
    }
    @Override
    protected void saveUserSpecificData(User user) throws DatabaseException {
        recruiterDao.saveRecruiter((Recruiter) user);
    }
}

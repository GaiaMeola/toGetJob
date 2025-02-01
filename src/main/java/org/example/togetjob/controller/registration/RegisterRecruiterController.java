package org.example.togetjob.controller.registration;

import org.example.togetjob.bean.RecruiterInfoBean;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.entity.Role;
import org.example.togetjob.model.entity.User;

public class RegisterRecruiterController extends AbstractRegisterController{

    private final RecruiterDao recruiterDao;
    private final RecruiterInfoBean recruiterInfoBean;

    public RegisterRecruiterController(RecruiterInfoBean recruiterInfoBean){
        this.recruiterDao = AbstractFactoryDaoSingleton.getFactoryDao().createRecruiterDao();
        this.recruiterInfoBean = recruiterInfoBean;
    }

    @Override
    protected User createUser(RegisterUserBean userBean) {
        return new Recruiter(
                userBean.getUsername(), userBean.getPassword(), userBean.getName(), userBean.getSurname(),
                userBean.getEmailAddress(), Role.RECRUITER, recruiterInfoBean.getCompanies(), null
        ); //No Collaborators
    }

    @Override
    protected void saveUserSpecificData(User user) {
        recruiterDao.saveRecruiter((Recruiter) user);
    }
}

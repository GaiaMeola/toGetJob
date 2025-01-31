package it.controller.registration;

import it.bean.RecruiterInfoBean;
import it.bean.RegisterUserBean;
import it.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import it.model.dao.abstractobjects.RecruiterDao;
import it.model.entity.Recruiter;
import it.model.entity.Role;
import it.model.entity.User;

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

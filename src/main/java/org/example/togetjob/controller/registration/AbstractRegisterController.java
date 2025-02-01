package org.example.togetjob.controller.registration;

import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.UserDao;
import org.example.togetjob.model.entity.User;

public abstract class AbstractRegisterController {

    protected final UserDao userDao;

    public AbstractRegisterController(){
        this.userDao = AbstractFactoryDaoSingleton.getFactoryDao().createUserDao();
    }

    public boolean registerUser(RegisterUserBean userBean){
        if(userDao.userExists(userBean.getUsername())){
            return false; //User already exists
        }

        User user = createUser(userBean);
        boolean saved = userDao.saveUser(user);

        if(saved){
            saveUserSpecificData(user);
        }

        return saved;
    }

    protected abstract User createUser(RegisterUserBean userBean);

    protected abstract void saveUserSpecificData(User user);

}

package org.example.togetjob.controller;

import org.example.togetjob.bean.LoginUserBean;
import org.example.togetjob.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.model.dao.abstractobjects.UserDao;
import org.example.togetjob.model.entity.User;
import org.example.togetjob.session.SessionManager;

public class LoginController {

    private final UserDao userDao;

    public LoginController(){
        this.userDao = AbstractFactoryDaoSingleton.getFactoryDao().createUserDao();
    }

    public boolean loginUser(LoginUserBean loginUserBean){

        User user = userDao.getUser(loginUserBean.getUsername()).orElse(null);

        if(user == null || !(user.getPassword().equalsIgnoreCase(loginUserBean.getPassword()))){
            return false; // User not found
        }

        SessionManager.getInstance().setCurrentUser(user);
        return true;
    }

}

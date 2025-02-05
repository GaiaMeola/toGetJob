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

        System.out.println(loginUserBean.getUsername());

        User user = userDao.getUser(loginUserBean.getUsername()).orElse(null);


        if(user == null || !(user.getPassword().equalsIgnoreCase(loginUserBean.getPassword()))){
            if (user == null) {
                return false;
            }
            return false; // User not found
        }

        System.out.println(user.getUsername());
        System.out.println(user.getPassword());

        SessionManager.getInstance().setCurrentUser(user);
        return true;
    }

}

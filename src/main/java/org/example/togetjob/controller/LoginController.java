package org.example.togetjob.controller;

import org.example.togetjob.bean.LoginUserBean;
import org.example.togetjob.exceptions.UserNotFoundException;
import org.example.togetjob.exceptions.WrongPasswordException;
import org.example.togetjob.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.dao.abstractobjects.UserDao;
import org.example.togetjob.model.entity.User;
import org.example.togetjob.session.SessionManager;

public class LoginController {

    private final UserDao userDao;

    public LoginController(){
        this.userDao = AbstractFactoryDaoSingleton.getFactoryDao().createUserDao();
    }

    public boolean loginUser(LoginUserBean loginUserBean) throws UserNotFoundException, WrongPasswordException {

        User user = userDao.getUser(loginUserBean.getUsername()).orElse(null);

        if(user == null){ //User not found
            throw new UserNotFoundException("User not found: " + loginUserBean.getUsername());
        }

        if(!(user.obtainPassword().equalsIgnoreCase(loginUserBean.getPassword()))){
            throw new WrongPasswordException("Incorrect password for user: " + loginUserBean.getUsername());
        }

        SessionManager.getInstance().setCurrentUser(user);
        return true;
    }

}

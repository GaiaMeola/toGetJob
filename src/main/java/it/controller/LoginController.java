package it.controller;

import it.bean.LoginUserBean;
import it.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import it.model.dao.abstractobjects.UserDao;
import it.model.entity.User;

public class LoginController {

    private UserDao userDao;

    public LoginController(){
        this.userDao = AbstractFactoryDaoSingleton.getFactoryDao().createUserDao();
    }

    public boolean loginUser(LoginUserBean loginUserBean){

        User user = userDao.getUser(loginUserBean.getUsername()).orElse(null);

        if(user == null){
            return false; //User does not exists
        }

        if(user.getPassword().equalsIgnoreCase(loginUserBean.getPassword())){
            return true; //Login successful
        } else {
            return false;
        }
    }

    public String getUserRole(LoginUserBean loginUserBean){

        User user = userDao.getUser(loginUserBean.getUsername()).orElse(null);

        if(user != null){
            return user.getRole().name();
        }

        return null; //User does not exist

    }

}

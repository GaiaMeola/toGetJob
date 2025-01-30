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
            System.out.println("User does not exist.");
            return false;
        }

        if(user.getPassword().equalsIgnoreCase(loginUserBean.getPassword())){
            System.out.println("Login successful! Welcome to toGetJob !");
            return true;
        } else {
            System.out.println("Incorrect password. Please, try again");
            return false;
        }
    }
}

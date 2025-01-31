package it.controller;

import it.bean.LoginUserBean;
import it.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import it.model.dao.abstractobjects.UserDao;
import it.model.entity.User;
import session.SessionManager;

public class LoginController {

    private UserDao userDao;

    public LoginController(){
        this.userDao = AbstractFactoryDaoSingleton.getFactoryDao().createUserDao();
    }

    public boolean loginUser(LoginUserBean loginUserBean){

        User user = userDao.getUser(loginUserBean.getUsername()).orElse(null);

        if(user == null || !user.getPassword().equalsIgnoreCase(loginUserBean.getPassword())){
            return false; // User non esiste o password errata
        }

        SessionManager.getInstance().setCurrentUser(user);
        return true;
    }

    public String getUserRole(LoginUserBean loginUserBean){

        User user = SessionManager.getInstance().getCurrentUser();
        return (user != null) ? user.getRole().name() : null; //User does not exist

    }

}

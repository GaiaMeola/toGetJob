package org.example.togetjob.view.boundary;

import org.example.togetjob.bean.LoginUserBean;
import org.example.togetjob.controller.LoginController;
import org.example.togetjob.exceptions.UserNotFoundException;
import org.example.togetjob.exceptions.WrongPasswordException;
import org.example.togetjob.session.SessionManager;

public class LoginBoundary {

    private final LoginController loginController;

    public LoginBoundary(){
        this.loginController = new LoginController();
    }

    public boolean login(String username, String password) throws UserNotFoundException, WrongPasswordException {
        LoginUserBean userBean = new LoginUserBean();
        userBean.setUsername(username);
        userBean.setPassword(password);
        return loginController.loginUser(userBean);
    }

    public void logout() {
        SessionManager.getInstance().clearSession();
    }

    public String getUserRole() {
        return SessionManager.getInstance().getCurrentUser().obtainRole().name();
    }

}

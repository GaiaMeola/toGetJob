package org.example.togetjob.boundary;

import org.example.togetjob.bean.LoginUserBean;
import org.example.togetjob.controller.LoginController;
import org.example.togetjob.session.SessionManager;

public class LoginBoundary {

    private final LoginController loginController;

    public LoginBoundary(){
        this.loginController = new LoginController();
    }

    public boolean login(String username, String password) {
        LoginUserBean userBean = new LoginUserBean(username, password);
        return loginController.loginUser(userBean);
    }

    public void logout() {
        SessionManager.getInstance().clearSession();
    }

    public String getUserRole() {
        return SessionManager.getInstance().getCurrentUser().getRole().name();
    }

}

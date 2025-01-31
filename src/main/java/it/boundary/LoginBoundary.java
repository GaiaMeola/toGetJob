package it.boundary;

import it.bean.LoginUserBean;
import it.controller.LoginController;
import session.SessionManager;

public class LoginBoundary {

    private final LoginController loginController;

    public LoginBoundary(){
        this.loginController = new LoginController();
    }

    public boolean login(String username, String password) {
        LoginUserBean userBean = new LoginUserBean(username, password);
        boolean success = loginController.loginUser(userBean);

        if (success) {
            return true;
        } else {
            return false;
        }
    }

    public void logout() {
        SessionManager.getInstance().clearSession();
        System.out.println("Logout successful!");
    }

    public String getUserRole() {
        return SessionManager.getInstance().getCurrentUser().getRole().name();
    }

}

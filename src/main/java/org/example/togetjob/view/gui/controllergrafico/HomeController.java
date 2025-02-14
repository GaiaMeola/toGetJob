package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.session.SessionManager;
import org.example.togetjob.view.boundary.LoginBoundary;
import org.example.togetjob.view.GUIContext;

public class HomeController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    private final LoginBoundary loginBoundary;
    private GUIContext context;

    public HomeController(){
        this.loginBoundary = new LoginBoundary();
    }

    public void initialize(GUIContext context){
        this.context = context;
    }

    public void setContext(GUIContext context) {
        this.context = context;
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            Printer.print("Username or password cannot be empty!");
            return;
        }

        boolean loginSuccess = loginBoundary.login(username, password);

        if (loginSuccess) {
            Printer.print("Login successful!");

            String userRole = SessionManager.getInstance().getCurrentUser().obtainRole().name();
            if ("STUDENT".equalsIgnoreCase(userRole)) {
                context.goNext("student_home");
            } else if ("RECRUITER".equalsIgnoreCase(userRole)) {
                context.goNext("recruiter_home");
            } else {
                Printer.print("Unknown user role: " + userRole);
            }
        } else {
            Printer.print("Login failed!");
        }
    }

    @FXML
    private void handleRegister() {
        if (context != null) {
            Printer.print("Context is initialized!");
            context.goNext("register");
        } else {
            Printer.print("Context is not initialized!");
        }
    }
}

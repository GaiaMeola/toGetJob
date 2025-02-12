package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.session.SessionManager;
import org.example.togetjob.view.boundary.LoginBoundary;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.concretestate.HomeRecruiterState;
import org.example.togetjob.view.gui.concretestate.HomeStudentState;
import org.example.togetjob.view.gui.concretestate.RegisterUserState;

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
            navigateToHome();
        } else {
            Printer.print("Login failed!");
        }
    }

    @FXML
    private void handleRegister() {
        if (context != null) {
            Printer.print("Context is initialized!");
            context.setState(new RegisterUserState(context));
            context.showMenu();
        } else {
            Printer.print("Context is not initialized!");
        }
    }

    private void navigateToHome() {
        String userRole = SessionManager.getInstance().getCurrentUser().obtainRole().name();

        if ("STUDENT".equalsIgnoreCase(userRole)) {
            context.setState(new HomeStudentState(context));
        } else if ("RECRUITER".equalsIgnoreCase(userRole)) {
            context.setState(new HomeRecruiterState(context));
        } else {
            Printer.print("Unknown user role: " + userRole);
            return;
        }

        context.showMenu();
    }

}

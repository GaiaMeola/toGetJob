package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.session.SessionManager;
import org.example.togetjob.view.boundary.LoginBoundary;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.exceptions.UserNotFoundException;
import org.example.togetjob.exceptions.WrongPasswordException;

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

        try {

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
            }

        } catch (UserNotFoundException e) {
            showErrorAlert("User not found", "The username you entered does not exist.");
        } catch (WrongPasswordException e) {
            showErrorAlert("Wrong password", "The password you entered is incorrect.");
        } catch (Exception e) {
            showErrorAlert("Login failed", "An unexpected error occurred. Please try again.");
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

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

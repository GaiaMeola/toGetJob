package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.GUIContext;

public class RegisterController {

    private static final String GO_HOME = "go_home";

    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField confirmPasswordField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField roleField;

    private GUIContext context;

    public RegisterController() {
        Printer.print("RegisterController created, but context is not yet set!");
    }

    public void setContext(GUIContext context) {
        this.context = context;
        Printer.print("Context set in RegisterController: " + context);
    }

    @FXML
    private void handleBackButton() {
        if (context != null) {
            Printer.print("Going back to Home...");
            context.goNext(GO_HOME);
        } else {
            Printer.print("Context is NOT initialized in RegisterController!");
        }
    }

    @FXML
    private void handleHomeButton() {
        if (context != null) {
            Printer.print("Going to Home screen...");
            context.goNext(GO_HOME);
        } else {
            Printer.print("Context is NOT initialized in RegisterController!");
        }
    }

    @FXML
    private void handleContinue() {
        if (context == null) {
            Printer.print("Cannot continue, context is NULL!");
            return;
        }

        String username = usernameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String name = nameField.getText();
        String surname = surnameField.getText();
        String email = emailField.getText();
        String role = roleField.getText();


        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty() || surname.isEmpty() || email.isEmpty() || role.isEmpty()) {
            showErrorAlert("Missing Fields", "All fields must be filled out!");
            return;
        }


        if (!password.equals(confirmPassword)) {
            showErrorAlert("Password Mismatch", "Passwords do not match!");
            return;
        }

        if (!role.equalsIgnoreCase("student") && !role.equalsIgnoreCase("recruiter")) {
            showErrorAlert("Invalid Role", "Please select 'student' or 'recruiter' as your role.");
            return;
        }

        if (!email.contains("@")) {
            showErrorAlert("Invalid Email", "Please enter a valid email address (must contain '@').");
            return;
        }

        RegisterUserBean user = new RegisterUserBean();
        user.setUsername(username);
        user.setPassword(password);
        user.setName(name);
        user.setSurname(surname);
        user.setRole(role);
        user.setEmail(email);
        Printer.print("User created: " + user);

        context.set("user", user);

        if (role.equalsIgnoreCase("student")) {
            Printer.print("Redirecting to Student registration...");
            context.goNext("register_student");
        } else if (role.equalsIgnoreCase("recruiter")) {
            Printer.print("Redirecting to Recruiter registration...");
            context.goNext("register_recruiter");
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

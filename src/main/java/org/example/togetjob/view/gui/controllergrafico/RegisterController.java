package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.concretestate.HomeState;
import org.example.togetjob.view.gui.concretestate.RegisterRecruiterState;
import org.example.togetjob.view.gui.concretestate.RegisterStudentState;

public class RegisterController {

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
            context.setState(new HomeState(context));
            context.showMenu();
        } else {
            Printer.print("Context is NOT initialized in RegisterController!");
        }
    }

    @FXML
    private void handleHomeButton() {
        if (context != null) {
            Printer.print("Going to Home screen...");
            context.setState(new HomeState(context));
            context.showMenu();
        } else {
            Printer.print("Context is NOT initialized in RegisterController!");
        }
    }

    @FXML
    private void handleContinueButton() {
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
            Printer.print("All fields must be filled out!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            Printer.print("Passwords do not match!");
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

        if (role.equalsIgnoreCase("Student")) {
            Printer.print("Redirecting to Student registration...");
            context.setState(new RegisterStudentState(user, context));
        } else if (role.equalsIgnoreCase("Recruiter")) {
            Printer.print("Redirecting to Recruiter registration...");
            context.setState(new RegisterRecruiterState(user, context));
        } else {
            Printer.print("Invalid role selected!");
            return;
        }

        context.showMenu();
    }
}

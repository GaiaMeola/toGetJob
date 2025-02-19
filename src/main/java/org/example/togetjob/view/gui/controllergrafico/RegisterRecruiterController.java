package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import org.example.togetjob.bean.RecruiterInfoBean;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.exceptions.InvalidCompanyListException;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.boundary.RegisterBoundary;
import org.example.togetjob.state.GUIContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterRecruiterController {

    @FXML private TextField companiesField;

    private GUIContext context;
    private RegisterUserBean userBean;
    private final RegisterBoundary registerBoundary = new RegisterBoundary();

    public void setContext(GUIContext context) {
        this.context = context;
    }

    public void setUserBean(RegisterUserBean userBean) {
        this.userBean = userBean;
    }

    @FXML
    private void handleContinueButton() {
        // Splitting the companies entered into the field by comma and converting to an ArrayList
        List<String> companies = new ArrayList<>(Arrays.asList(companiesField.getText().split(",")));

        // Trim spaces from each company
        companies.replaceAll(String::trim);

        try {
            // Creating a recruiter info bean and setting the companies list
            RecruiterInfoBean recruiterInfoBean = new RecruiterInfoBean();
            recruiterInfoBean.setCompanies(companies);  // Will throw exception if invalid

            // Register the user with the provided recruiter information
            boolean registrationSuccess = registerBoundary.registerUser(userBean, recruiterInfoBean);

            if (registrationSuccess) {
                Printer.print("Recruiter successfully registered: " + recruiterInfoBean);
                context.goNext("go_home_recruiter");
            } else {
                showErrorAlert("Registration Failed", "Registration failed. Please try again later.");
            }
        } catch (InvalidCompanyListException e) {
            // Catch the specific exception for invalid company input
            showErrorAlert("Invalid Companies", e.getMessage());
        } catch (Exception e) {
            showErrorAlert("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    @FXML
    private void handleBackButton() {
        if (context != null) {
            Printer.print("Going back to Home...");
            context.goNext("go_home");
        } else {
            Printer.print("Context is NOT initialized in RegisterRecruiterController!");
        }
    }

    // Method to show error alerts
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

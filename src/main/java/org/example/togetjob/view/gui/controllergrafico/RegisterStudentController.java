package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.exceptions.*;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.boundary.RegisterBoundary;
import org.example.togetjob.state.GUIContext;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

public class RegisterStudentController {

    @FXML private TextField phoneField;
    @FXML private DatePicker birthDateField;
    @FXML private TextField availabilityField;
    @FXML private TextField degreesField;
    @FXML private TextField coursesField;
    @FXML private TextField certificationsField;
    @FXML private TextField workExperienceField;
    @FXML private TextField skillsField;

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
        String phone = phoneField.getText().trim();
        String availability = availabilityField.getText().trim();

        // Create the student info bean
        StudentInfoBean studentInfoBean = new StudentInfoBean();

        try {
            // Setting properties for the StudentInfoBean (validations will happen inside the setters)
            studentInfoBean.setUsername(userBean.getUsername());
            studentInfoBean.setDateOfBirth(birthDateField.getValue());  // This will check if over 18
            studentInfoBean.setPhoneNumber(phone);                      // This will check phone format
            studentInfoBean.setAvailability(availability);              // This will check if valid availability

            // Setting list-based properties (e.g., degrees, certifications, etc.)
            studentInfoBean.setDegrees(parseListFromTextField(degreesField));
            studentInfoBean.setCoursesAttended(parseListFromTextField(coursesField));
            studentInfoBean.setCertifications(parseListFromTextField(certificationsField));
            studentInfoBean.setWorkExperiences(parseListFromTextField(workExperienceField));
            studentInfoBean.setSkills(parseListFromTextField(skillsField));

            // Now attempt to register the user with the provided student info
            boolean registrationSuccess = registerBoundary.registerUser(userBean, studentInfoBean);

            if (registrationSuccess) {
                Printer.print("Student successfully registered: " + studentInfoBean);
                context.goNext("register_student_complete");
            } else {
                showErrorAlert("Registration Failed", "Registration failed. Please try again later.");
            }
        } catch (InvalidDateOfBirthException e) {
            showErrorAlert("Invalid Date of Birth", "The date of birth is invalid. You must be at least 18 years old.");
        } catch (InvalidPhoneNumberException e) {
            showErrorAlert("Invalid Phone Number", "The phone number you entered is invalid. It must be between 10 and 15 digits.");
        } catch (InvalidAvailabilityException e) {
            showErrorAlert("Invalid Availability", "The availability must be either 'full-time' or 'part-time'.");
        } catch (InvalidDegreeListException e) {
            showErrorAlert("Invalid Degrees", "The degrees list cannot be empty and must contain valid degrees.");
        } catch (InvalidCourseListException e) {
            showErrorAlert("Invalid Courses", "The courses attended list cannot be empty and must contain valid courses.");
        } catch (InvalidCertificationListException e) {
            showErrorAlert("Invalid Certifications", "The certifications list cannot be empty and must contain valid certifications.");
        } catch (InvalidWorkExperienceListException e) {
            showErrorAlert("Invalid Work Experiences", "The work experience list cannot be empty and must contain valid experiences.");
        } catch (InvalidSkillListException e) {
            showErrorAlert("Invalid Skills", "The skills list cannot be empty and must contain valid skills.");
        } catch (Exception e) {
            showErrorAlert("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
        }
    }

    private List<String> parseListFromTextField(TextField textField) {
        String text = textField.getText().trim();
        // Return an empty list if no input
        if (text.isEmpty()) return new ArrayList<>();
        // Otherwise, split by commas and trim spaces
        return new ArrayList<>(Arrays.asList(text.split(",")));
    }

    @FXML
    private void handleBackButton() {
        if (context != null) {
            Printer.print("Going back to Home...");
            context.goNext("go_home");
        } else {
            Printer.print("Context is NOT initialized in RegisterStudentController!");
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

package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.boundary.RegisterBoundary;
import org.example.togetjob.state.GUIContext;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

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
    private void handleContinue() {
        String phone = phoneField.getText();
        String availability = availabilityField.getText().trim();

        // Check if the essential fields are filled
        if (phone.isEmpty() || availability.isEmpty()) {
            showErrorAlert("Missing Fields", "Phone and availability fields must be filled out!");
            return;
        }

        availability = availability.replace(" ", "").toLowerCase();
        // Check if the availability is either 'full-time' or 'part-time'
        if (!availability.equalsIgnoreCase("fulltime") && !availability.equalsIgnoreCase("parttime")) {
            showErrorAlert("Invalid Availability", "Availability must be 'full-time' or 'part-time'.");
            return;
        }


        // Check if student is over 18 years old
        if (birthDateField.getValue() == null || !isOver18(birthDateField.getValue())) {
            showErrorAlert("Invalid Age", "You must be over 18 years old to register.");
            return;
        }

        // Create the student info bean and register the student
        StudentInfoBean studentInfoBean = getStudentInfoBean(phone, availability);

        boolean registrationSuccess = registerBoundary.registerUser(userBean, studentInfoBean);

        if (registrationSuccess) {
            Printer.print("Student successfully registered: " + studentInfoBean);
            context.goNext("register_student_complete");
        } else {
            showErrorAlert("Registration Failed", "Registration failed. Please try again later.");
        }
    }

    @NotNull
    private StudentInfoBean getStudentInfoBean(String phone, String availability) {
        List<String> degrees = List.of(degreesField.getText().split(","));
        List<String> coursesAttended = List.of(coursesField.getText().split(","));
        List<String> certifications = List.of(certificationsField.getText().split(","));
        List<String> workExperiences = List.of(workExperienceField.getText().split(","));
        List<String> skills = List.of(skillsField.getText().split(","));

        StudentInfoBean studentInfoBean = new StudentInfoBean();
        studentInfoBean.setUsername(userBean.getUsername());
        studentInfoBean.setDateOfBirth(birthDateField.getValue());
        studentInfoBean.setPhoneNumber(phone);
        studentInfoBean.setAvailability(availability);
        studentInfoBean.setDegrees(degrees);
        studentInfoBean.setCoursesAttended(coursesAttended);
        studentInfoBean.setCertifications(certifications);
        studentInfoBean.setWorkExperiences(workExperiences);
        studentInfoBean.setSkills(skills);
        return studentInfoBean;
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

    // Check if student is over 18 years old
    private boolean isOver18(LocalDate birthDate) {
        LocalDate currentDate = LocalDate.now();
        Period age = Period.between(birthDate, currentDate);
        return age.getYears() > 18;
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

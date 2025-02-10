package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.boundary.RegisterBoundary;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.concretestate.HomeStudentState;
import org.jetbrains.annotations.NotNull;

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
    @FXML private Button continueButton;

    private GUIContext context;
    private RegisterUserBean userBean;
    private final RegisterBoundary registerBoundary = new RegisterBoundary();

    public RegisterStudentController() {
        /* */
    }

    public void setContext(GUIContext context) {
        this.context = context;
    }

    public void setUserBean(RegisterUserBean userBean) {
        this.userBean = userBean;
    }

    @FXML
    private void handleContinueButton() {

        String phone = phoneField.getText();
        String availability = availabilityField.getText();

        StudentInfoBean studentInfoBean = getStudentInfoBean(phone, availability);


        if (phone.isEmpty() || availability.isEmpty()) {
            Printer.print("Phone and availability fields must be filled out!");
            return;
        }


        boolean registrationSuccess = registerBoundary.registerUser(userBean, studentInfoBean);


        if (registrationSuccess) {
            Printer.print("Student successfully registered: " + studentInfoBean);
            context.setState(new HomeStudentState(context));
            context.showMenu();
        } else {
            Printer.print("Registration failed!");
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
}

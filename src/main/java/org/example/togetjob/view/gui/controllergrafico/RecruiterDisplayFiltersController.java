package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.togetjob.bean.StudentInfoSearchBean;

import java.util.List;

public class RecruiterDisplayFiltersController {


    @FXML
    private TextField degreesField ;
    @FXML
    private TextField coursesAttendedField ;
    @FXML
    private TextField certificationsField ;
    @FXML
    private TextField workExperiencesField;
    @FXML
    private TextField skillsField ;
    @FXML
    private TextField availabilityField ;



    @FXML
    private void handleProceedButton(){

        List<String> degrees = List.of(degreesField.getText().split(","));
        List<String> courses = List.of(coursesAttendedField.getText().split(","));
        List<String> certifications = List.of(certificationsField.getText().split(","));
        List<String> workExperiences = List.of(workExperiencesField.getText().split(","));
        List<String> skils = List.of(skillsField.getText().split(","));
        String availability = availabilityField.getText() ;

        StudentInfoSearchBean studentInfoSearchBean = new StudentInfoSearchBean() ;

        studentInfoSearchBean.setDegrees(degrees);
        studentInfoSearchBean.setCoursesAttended(courses);
        studentInfoSearchBean.setCertifications(certifications);
        studentInfoSearchBean.setWorkExperiences(workExperiences);
        studentInfoSearchBean.setSkills(skils);
        studentInfoSearchBean.setAvailability(availability);


    }

}

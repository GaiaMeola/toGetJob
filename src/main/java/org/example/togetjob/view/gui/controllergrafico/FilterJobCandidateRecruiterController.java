package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.togetjob.bean.StudentInfoSearchBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.GUIContext;

import java.util.List;
import java.util.ArrayList;

public class FilterJobCandidateRecruiterController {

    @FXML
    private TextField degreesField;
    @FXML
    private TextField coursesAttendedField;
    @FXML
    private TextField certificationsField;
    @FXML
    private TextField workField;
    @FXML
    private TextField skillsField;
    @FXML
    private TextField availabilityField;

    private GUIContext context;

    public void setContext(GUIContext context) {
        this.context = context;
    }

    @FXML
    private void handleProceedButton() {
        if (context == null) {
            Printer.print("ERROR: context is NULL in FilterJobCandidateRecruiterController!");
            return;
        }

        String degreesInput = degreesField.getText();
        String coursesInput = coursesAttendedField.getText();
        String certificationsInput = certificationsField.getText();
        String workInput = workField.getText();
        String skillsInput = skillsField.getText();
        String availabilityInput = availabilityField.getText();

        StudentInfoSearchBean studentInfoSearchBean = new StudentInfoSearchBean();
        studentInfoSearchBean.setDegrees(parseTextFieldInput(degreesInput));
        studentInfoSearchBean.setCoursesAttended(parseTextFieldInput(coursesInput));
        studentInfoSearchBean.setCertifications(parseTextFieldInput(certificationsInput));
        studentInfoSearchBean.setWorkExperiences(parseTextFieldInput(workInput));
        studentInfoSearchBean.setSkills(parseTextFieldInput(skillsInput));
        studentInfoSearchBean.setAvailability(availabilityInput);
        context.set("studentInfoSearch", studentInfoSearchBean);
        context.goNext("filterJobCandidates");
    }

    @FXML
    private void handleGoBack() {
        if (context != null) {
            Printer.print("Going back to HomeRecruiter...");
            context.goNext("homeRecruiter");
        } else {
            Printer.print("Context is NOT initialized in FilterJobCandidateRecruiterController!");
        }
    }

    private List<String> parseTextFieldInput(String input) {
        if (input != null && !input.trim().isEmpty()) {

            List<String> resultList = new ArrayList<>();
            String[] items = input.split(",");
            for (String item : items) {
                resultList.add(item.trim());
            }
            return resultList;
        }
        return new ArrayList<>();
    }
}

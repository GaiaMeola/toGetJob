package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.StudentInfoSearchBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.concretestate.ContactAJobCandidateState;
import org.example.togetjob.view.gui.concretestate.HomeRecruiterState;

import java.util.Arrays;
import java.util.List;

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
    private JobAnnouncementBean jobAnnouncementBean;

    public void setContext(GUIContext context) {
        this.context = context;
    }

    public void setJobAnnouncement(JobAnnouncementBean jobAnnouncementBean) {
        this.jobAnnouncementBean = jobAnnouncementBean;
    }

    @FXML
    private void handleProceedButton() {
        if (jobAnnouncementBean == null) {
            Printer.print("ERROR: jobAnnouncementBean is NULL in FilterJobCandidateRecruiterController!");
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

        if (context == null) {
            Printer.print("ERROR: context is NULL in FilterJobCandidateRecruiterController!");
            return;
        }

        context.setState(new ContactAJobCandidateState(context, studentInfoSearchBean, jobAnnouncementBean));
        context.showMenu();
    }

    @FXML
    private void handleGoBack() {
        if (context != null) {
            Printer.print("Going back to RecruiterHome...");
            context.setState(new HomeRecruiterState(context));
            context.showMenu();
        } else {
            Printer.print("Context is NOT initialized in FilterJobCandidateRecruiterController!");
        }
    }

    private List<String> parseTextFieldInput(String input) {
        if (input != null && !input.trim().isEmpty()) {
            return Arrays.stream(input.split(","))
                    .map(String::trim)
                    .toList();
        }
        return List.of();
    }
}

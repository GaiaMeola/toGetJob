package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.example.togetjob.bean.JobAnnouncementSearchBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.view.gui.concretestate.FilteredJobAnnouncementsState;
import org.example.togetjob.view.gui.concretestate.HomeStudentState;

public class FilterJobAnnouncementStudentController {

    @FXML
    private TextField jobTitleField;
    @FXML
    private TextField jobTypeField;
    @FXML
    private TextField roleField;
    @FXML
    private TextField locationField;
    @FXML
    private TextField workingHoursField;
    @FXML
    private TextField companyNameField;
    @FXML
    private TextField salaryField;

    private static final String INVALID_INPUT = "Invalid Input";

    private GUIContext context;

    public void setContext(GUIContext context) {
        this.context = context;
    }

    @FXML
    private void handleProceedButton() {
        Printer.print("Button pressed!");

        String workingHours = workingHoursField.getText().trim();
        String salary = salaryField.getText().trim();
        StringBuilder errorMessages = new StringBuilder();

        if (!workingHours.isEmpty() && !isValidNumber(workingHours)) {
            errorMessages.append("Working hours must be a positive integer.\n");
        }

        if (!salary.isEmpty() && !isValidPositiveDouble(salary)) {
            errorMessages.append("Salary must be a positive number.\n");
        }

        if (errorMessages.length() > 0) {
            showErrorAlert(errorMessages.toString());
            return;
        }

        JobAnnouncementSearchBean jobAnnouncementSearchBean = new JobAnnouncementSearchBean();
        jobAnnouncementSearchBean.setJobTitle(jobTitleField.getText().trim());
        jobAnnouncementSearchBean.setJobType(jobTypeField.getText().trim());
        jobAnnouncementSearchBean.setCompanyName(companyNameField.getText().trim());
        jobAnnouncementSearchBean.setLocation(locationField.getText().trim());
        jobAnnouncementSearchBean.setWorkingHours(workingHours.isEmpty() ? null : workingHours);
        jobAnnouncementSearchBean.setSalary(salary.isEmpty() ? null : salary);
        jobAnnouncementSearchBean.setRole(roleField.getText().trim());

        context.setState(new FilteredJobAnnouncementsState(context, jobAnnouncementSearchBean));
        context.showMenu();
    }

    private boolean isValidNumber(String input) {
        try {
            int value = Integer.parseInt(input);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidPositiveDouble(String input) {
        try {
            double value = Double.parseDouble(input);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @FXML
    private void handleGoBack() {
        if (context != null) {
            Printer.print("Going back to StudentHome...");
            context.setState(new HomeStudentState(context));
            context.showMenu();
        } else {
            Printer.print("Context is NOT initialized in FilterJobAnnouncement!");
        }
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(INVALID_INPUT);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

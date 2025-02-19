package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import org.example.togetjob.bean.JobAnnouncementSearchBean;
import org.example.togetjob.exceptions.*;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.view.gui.concretestate.FilteredJobAnnouncementsState;

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

    private GUIContext context;

    public void setContext(GUIContext context) {
        this.context = context;
    }

    @FXML
    private void handleProceedButton() {
        Printer.print("Button pressed!");

        JobAnnouncementSearchBean jobAnnouncementSearchBean = new JobAnnouncementSearchBean();
        try {
            jobAnnouncementSearchBean.setJobTitle(jobTitleField.getText().trim());
            jobAnnouncementSearchBean.setJobType(jobTypeField.getText().trim());
            jobAnnouncementSearchBean.setCompanyName(companyNameField.getText().trim());
            jobAnnouncementSearchBean.setLocation(locationField.getText().trim());
            jobAnnouncementSearchBean.setWorkingHours(workingHoursField.getText().trim().isEmpty() ? null : workingHoursField.getText().trim());
            jobAnnouncementSearchBean.setSalary(salaryField.getText().trim().isEmpty() ? null : salaryField.getText().trim());
            jobAnnouncementSearchBean.setRole(roleField.getText().trim());

            context.setState(new FilteredJobAnnouncementsState(context, jobAnnouncementSearchBean));
            context.showMenu();

        } catch (InvalidJobTitleException e) {
            showErrorAlert("Invalid Job Title", e.getMessage());
        } catch (InvalidJobTypeException e) {
            showErrorAlert("Invalid Job Type", e.getMessage());
        } catch (InvalidWorkingHoursException e) {
            showErrorAlert("Invalid Working Hours", e.getMessage());
        } catch (InvalidSalaryException e) {
            showErrorAlert("Invalid Salary", e.getMessage());
        } catch (InvalidRoleException e) {
            showErrorAlert("Invalid Role", e.getMessage());
        } catch(InvalidCompanyNameException e){
                showErrorAlert("Invalid Company Name", e.getMessage());
        } catch(Exception e){
                showErrorAlert("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
            }
        }

    @FXML
    private void handleGoBack() {
        if (context != null) {
            Printer.print("Going back to StudentHome...");
            context.goNext("student_home");
        } else {
            Printer.print("Context is NOT initialized in FilterJobAnnouncement!");
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

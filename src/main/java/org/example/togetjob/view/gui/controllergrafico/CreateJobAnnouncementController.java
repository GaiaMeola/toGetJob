package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.exceptions.*;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.boundary.PublishAJobAnnouncementRecruiterBoundary;
import org.example.togetjob.state.GUIContext;

public class CreateJobAnnouncementController {

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
    @FXML
    private TextField descriptionField;

    private final PublishAJobAnnouncementRecruiterBoundary boundary = new PublishAJobAnnouncementRecruiterBoundary();
    private GUIContext context;

    public void setContext(GUIContext context) {
        this.context = context;
    }

    @FXML
    private void handleProceedButton() {
        Printer.print("Button pressed!");

        JobAnnouncementBean jobAnnouncementBean = new JobAnnouncementBean();

        jobAnnouncementBean.setJobTitle(jobTitleField.getText().trim());
        jobAnnouncementBean.setJobType(jobTypeField.getText().trim());
        jobAnnouncementBean.setRole(roleField.getText().trim());
        jobAnnouncementBean.setLocation(locationField.getText().trim());
        jobAnnouncementBean.setWorkingHours(workingHoursField.getText().trim());
        jobAnnouncementBean.setCompanyName(companyNameField.getText().trim());
        jobAnnouncementBean.setSalary(salaryField.getText().trim());
        jobAnnouncementBean.setDescription(descriptionField.getText().trim());

        try {
            // Try to validate and publish the job announcement
            boundary.publishJobAnnouncement(jobAnnouncementBean);

            // If successful, notify and navigate
            Printer.print("Job announcement published!");
            showSuccessAlert();
            context.goNext("jobPublished");
        } catch (InvalidJobTitleException e) {
            showErrorAlert("Invalid Job Title", e.getMessage());
        } catch (InvalidJobTypeException e) {
            showErrorAlert("Invalid Job Type", e.getMessage());
        } catch (InvalidRoleException e) {
            showErrorAlert("Invalid Role", e.getMessage());
        } catch (InvalidLocationException e) {
            showErrorAlert("Invalid Location", e.getMessage());
        } catch (InvalidWorkingHoursException e) {
            showErrorAlert("Invalid Working Hours", e.getMessage());
        } catch (InvalidSalaryException e) {
            showErrorAlert("Invalid Salary", e.getMessage());
        } catch (InvalidCompanyNameException e) {
            showErrorAlert("Invalid Company Name", e.getMessage());
        } catch (JobAnnouncementAlreadyExists e) {
            showErrorAlert("Job Announcement Exists", "A job announcement with this title already exists.");
        } catch (Exception e) {
            showErrorAlert("Unexpected Error", "An unexpected error occurred: " + e.getMessage());
            Printer.print("Unexpected error: " + e.getMessage());
        }
    }

    @FXML
    private void handleGoBack() {
        if (context != null) {
            Printer.print("Going back to RecruiterHome...");
            context.goNext("homeRecruiter");
        } else {
            Printer.print("Context is NOT initialized in CreateJobAnnouncementController!");
        }
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccessAlert() {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Job Announcement Published");
        alert.setHeaderText(null);
        alert.setContentText("The job announcement has been successfully published.");
        alert.showAndWait();
    }
}

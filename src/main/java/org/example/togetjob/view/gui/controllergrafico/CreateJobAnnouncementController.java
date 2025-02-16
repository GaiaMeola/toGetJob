package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.exceptions.InvalidSalaryException;
import org.example.togetjob.exceptions.InvalidWorkingHourException;
import org.example.togetjob.exceptions.JobAnnouncementAlreadyExists;
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
        if (context == null) {
            Printer.print("ERROR: context is NULL in CreateJobAnnouncementController!");
            return;
        }

        String jobTitle = jobTitleField.getText();
        String jobType = jobTypeField.getText();
        String role = roleField.getText();
        String location = locationField.getText();
        String workingHours = workingHoursField.getText();
        String companyName = companyNameField.getText();
        String salary = salaryField.getText();
        String description = descriptionField.getText();


        if (jobTitle.isEmpty() || jobType.isEmpty() || role.isEmpty() || location.isEmpty() || workingHours.isEmpty() || companyName.isEmpty() || salary.isEmpty() || description.isEmpty()) {
            Printer.print("No field can be empty!");
            showErrorAlert("Validation Error", "Please fill in all the fields.");
            return;
        }


        try {
            int workingHoursInt = Integer.parseInt(workingHours);
            if (workingHoursInt <= 0) {
                throw new InvalidWorkingHourException("Working hours must be greater than 0.");
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Input", "Working hours must be a valid number.");
            return;
        } catch (InvalidWorkingHourException e) {
            showErrorAlert("Invalid Working Hours", e.getMessage());
            return;
        }


        try {
            double salaryDouble = Double.parseDouble(salary);
            if (salaryDouble <= 0) {
                throw new InvalidSalaryException("Salary must be a valid positive number.");
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Invalid Input", "Salary must be a valid number.");
            return;
        } catch (InvalidSalaryException e) {
            showErrorAlert("Invalid Salary", e.getMessage());
            return;
        }


        JobAnnouncementBean jobAnnouncementBean = new JobAnnouncementBean();
        jobAnnouncementBean.setActive(true);
        jobAnnouncementBean.setJobTitle(jobTitle);
        jobAnnouncementBean.setJobType(jobType);
        jobAnnouncementBean.setCompanyName(companyName);
        jobAnnouncementBean.setDescription(description);
        jobAnnouncementBean.setLocation(location);
        jobAnnouncementBean.setWorkingHours(workingHours);
        jobAnnouncementBean.setSalary(salary);
        jobAnnouncementBean.setRole(role);


        try {
            boolean creation = boundary.publishJobAnnouncement(jobAnnouncementBean);

            if (creation) {
                Printer.print("Job announcement published!");
                showSuccessAlert();
                context.set("jobAnnouncement", jobAnnouncementBean);
                context.goNext("jobPublished");
            } else {
                Printer.print("ERROR: Job announcement NOT published!");
                showErrorAlert("Publish Error", "Failed to publish job announcement.");
            }
        } catch (JobAnnouncementAlreadyExists e) {
            showErrorAlert("Job Announcement Exists", "A job announcement with this title already exists.");
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
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Job Announcement Published");
        alert.setHeaderText(null);
        alert.setContentText("The job announcement has been successfully published.");
        alert.showAndWait();
    }
}

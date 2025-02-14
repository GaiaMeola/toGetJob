package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.boundary.PublishAJobAnnouncementRecruiterBoundary;
import org.example.togetjob.view.GUIContext;

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

        boolean creation = boundary.publishJobAnnouncement(jobAnnouncementBean);

        if (creation) {
            Printer.print("Job announcement published!");
            context.set("jobAnnouncement", jobAnnouncementBean);
            context.goNext("jobPublished");
        } else {
            Printer.print("ERROR: Job announcement NOT published!");
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
}

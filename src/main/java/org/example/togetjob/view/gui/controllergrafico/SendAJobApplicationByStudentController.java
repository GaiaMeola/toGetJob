package org.example.togetjob.view.gui.controllergrafico;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.JobAnnouncementSearchBean;
import org.example.togetjob.bean.JobApplicationBean;
import org.example.togetjob.exceptions.*;
import org.example.togetjob.model.entity.Status;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.session.SessionManager;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.view.boundary.SendAJobApplicationStudentBoundary;

import java.util.List;

public class SendAJobApplicationByStudentController {

    private static final String COMMON_FONT_STYLE = "-fx-font-family: 'Apple Gothic'; -fx-font-size: 18px; -fx-text-fill: #2980b9;";
    private static final String BUTTON_STYLE = "-fx-background-color: #b3d9ff; -fx-text-fill: #2980b9; " +
            "-fx-border-radius: 5; -fx-border-color: #2980b9; -fx-border-width: 2; " +
            "-fx-cursor: hand; -fx-font-weight: bold; -fx-background-radius: 5;" +
            "-fx-font-size: 14px; -fx-padding: 10 20;";
    private static final String BOLD_JOB_TITLE_STYLE = "-fx-font-family: 'Apple Gothic'; -fx-font-size: 18px; -fx-text-fill: #2980b9; -fx-font-weight: bold;";
    private static final String JOB_DETAIL_STYLE = "-fx-font-family: 'Apple Gothic'; -fx-font-size: 16px; -fx-text-fill: #2980b9;";
    private static final String JOB_DETAIL_STYLE_TITLE = "-fx-font-family: 'Apple Gothic'; -fx-font-weight: bold; -fx-font-size: 16px; -fx-text-fill: #b3d9ff;";

    @FXML
    private TableView<JobAnnouncementBean> jobsTable;
    @FXML
    private TableColumn<JobAnnouncementBean, String> jobTitleColumn;
    @FXML
    private TableColumn<JobAnnouncementBean, String> companyColumn;
    @FXML
    private TableColumn<JobAnnouncementBean, Void> actionsColumn;

    private GUIContext context;


    private JobAnnouncementSearchBean jobAnnouncementSearchBean;
    private final SendAJobApplicationStudentBoundary boundary = new SendAJobApplicationStudentBoundary();


    public void setJobAnnouncementSearchBean(JobAnnouncementSearchBean jobAnnouncementSearchBean) {
        this.jobAnnouncementSearchBean = jobAnnouncementSearchBean;
        if (jobsTable != null) {
            loadJobAnnouncements();
        }
    }

    @FXML
    private void initialize() { // NOSONAR
        // This method is called automatically by the FXML loader to set up the table columns.
        setupTableColumns();
    }

    private void setupTableColumns() {
        jobTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJobTitle()));
        companyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCompanyName()));

        jobTitleColumn.setPrefWidth(300);
        companyColumn.setPrefWidth(250);
        actionsColumn.setPrefWidth(200);

        jobTitleColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String jobTitle, boolean empty) {
                super.updateItem(jobTitle, empty);
                if (empty || jobTitle == null) {
                    setText(null);
                } else {
                    setText(jobTitle);
                    setStyle(BOLD_JOB_TITLE_STYLE);
                }
            }
        });

        companyColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(String companyName, boolean empty) {
                super.updateItem(companyName, empty);
                if (empty || companyName == null) {
                    setText(null);
                } else {
                    setText(companyName);
                    setStyle(COMMON_FONT_STYLE);
                }
            }
        });

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewDetailsButton = new Button("View Details");

            {  // NOSONAR
                viewDetailsButton.setStyle(BUTTON_STYLE);
                viewDetailsButton.setOnAction(event -> {
                    JobAnnouncementBean job = getTableView().getItems().get(getIndex());
                    openJobDetailsPopup(job);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(viewDetailsButton);
                    hbox.setAlignment(Pos.CENTER);
                    hbox.setSpacing(10);
                    setGraphic(hbox);
                }
            }
        });
    }

    public void loadJobAnnouncements() {
        if (jobAnnouncementSearchBean == null) {
            Printer.print("ERROR: jobAnnouncementSearchBean is NULL");
            return;
        }
        List<JobAnnouncementBean> jobList = boundary.getJobAnnouncements(jobAnnouncementSearchBean);
        ObservableList<JobAnnouncementBean> observableList = FXCollections.observableArrayList(jobList);
        jobsTable.setItems(observableList);
    }

    private void openJobDetailsPopup(JobAnnouncementBean job) {
        // Create a new Stage for the job details
        Stage detailsStage = new Stage();
        detailsStage.setTitle("Job Details: " + job.getJobTitle());

        // Create a GridPane to hold the content of the details popup
        GridPane grid = new GridPane();
        grid.setVgap(10);  // Vertical gap between rows
        grid.setHgap(20);  // Horizontal gap between columns
        grid.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: white;");

        // Title of the job details
        Label jobTitleLabel = new Label("Job Title:");
        jobTitleLabel.setStyle(JOB_DETAIL_STYLE_TITLE);
        Label jobTitleValue = new Label(job.getJobTitle());
        jobTitleValue.setStyle(JOB_DETAIL_STYLE);

        // Company Name
        Label companyLabel = new Label("Company:");
        companyLabel.setStyle(JOB_DETAIL_STYLE_TITLE);
        Label companyValue = new Label(job.getCompanyName());
        companyValue.setStyle(JOB_DETAIL_STYLE);

        // Job details labels (Role, Type, etc.)
        Label roleLabel = new Label("Role:");
        roleLabel.setStyle(JOB_DETAIL_STYLE_TITLE);
        Label roleValue = new Label(job.getRole());
        roleValue.setStyle(JOB_DETAIL_STYLE);

        Label typeLabel = new Label("Type:");
        typeLabel.setStyle(JOB_DETAIL_STYLE_TITLE);
        Label typeValue = new Label(job.getJobType());
        typeValue.setStyle(JOB_DETAIL_STYLE);

        Label locationLabel = new Label("Location:");
        locationLabel.setStyle(JOB_DETAIL_STYLE_TITLE);
        Label locationValue = new Label(job.getLocation());
        locationValue.setStyle(JOB_DETAIL_STYLE);

        Label salaryLabel = new Label("Salary:");
        salaryLabel.setStyle(JOB_DETAIL_STYLE_TITLE);
        Label salaryValue = new Label(job.getSalary());
        salaryValue.setStyle(JOB_DETAIL_STYLE);

        // New fields: Description, Status, Recruiter
        Label descriptionLabel = new Label("Description:");
        descriptionLabel.setStyle(JOB_DETAIL_STYLE_TITLE);
        Label descriptionValue = new Label(job.getDescription());
        descriptionValue.setStyle(JOB_DETAIL_STYLE);

        Label statusLabel = new Label("Status:");
        statusLabel.setStyle(JOB_DETAIL_STYLE_TITLE);
        Label statusValue = new Label(job.isActive() ? "Active" : "Inactive");
        statusValue.setStyle(JOB_DETAIL_STYLE);

        Label recruiterLabel = new Label("Recruiter:");
        recruiterLabel.setStyle(JOB_DETAIL_STYLE_TITLE);
        Label recruiterValue = new Label(job.getRecruiterUsername());
        recruiterValue.setStyle(JOB_DETAIL_STYLE);

        // Add all labels and values to the GridPane
        grid.add(jobTitleLabel, 0, 0);
        grid.add(jobTitleValue, 1, 0);
        grid.add(companyLabel, 0, 1);
        grid.add(companyValue, 1, 1);
        grid.add(roleLabel, 0, 2);
        grid.add(roleValue, 1, 2);
        grid.add(typeLabel, 0, 3);
        grid.add(typeValue, 1, 3);
        grid.add(locationLabel, 0, 4);
        grid.add(locationValue, 1, 4);
        grid.add(salaryLabel, 0, 5);
        grid.add(salaryValue, 1, 5);
        grid.add(descriptionLabel, 0, 6);
        grid.add(descriptionValue, 1, 6);
        grid.add(statusLabel, 0, 7);
        grid.add(statusValue, 1, 7);
        grid.add(recruiterLabel, 0, 8);
        grid.add(recruiterValue, 1, 8);

        // Create a HBox for buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        Button applyButton = new Button("Apply");
        applyButton.setStyle(BUTTON_STYLE);
        applyButton.setOnAction(event -> openJobApplicationForm(job));

        buttonBox.getChildren().addAll(applyButton);

        // Add buttons to the GridPane
        grid.add(buttonBox, 0, 9, 2, 1);  // Span across two columns

        // Set the scene and show the stage
        Scene scene = new Scene(grid, 450, 450);
        detailsStage.setScene(scene);
        detailsStage.show();
    }

    private void openJobApplicationForm(JobAnnouncementBean job) {

        Dialog<JobApplicationBean> dialog = new Dialog<>();
        dialog.setTitle("Apply for: " + job.getJobTitle());
        dialog.setHeaderText("Submit your application for " + job.getCompanyName());

        Label coverLetterLabel = new Label("Cover Letter:");
        coverLetterLabel.setStyle(COMMON_FONT_STYLE + "-fx-font-weight: bold;");

        TextArea coverLetterField = new TextArea();
        coverLetterField.setPromptText("Write your cover letter here...");
        coverLetterField.setPrefRowCount(5);
        coverLetterField.setStyle(COMMON_FONT_STYLE);

        VBox formBox = new VBox(10, coverLetterLabel, coverLetterField);
        formBox.setStyle("-fx-padding: 20;");

        dialog.getDialogPane().setContent(formBox);
        dialog.getDialogPane().getButtonTypes().add(
                new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE)
        );


        dialog.setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                handleJobApplicationSubmission(job, coverLetterField.getText().trim(), dialog);
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void handleJobApplicationSubmission(JobAnnouncementBean job, String coverLetter, Dialog<JobApplicationBean> dialog) {
        try {
            JobApplicationBean applicationBean = new JobApplicationBean();
            applicationBean.setJobTitle(job.getJobTitle());
            applicationBean.setRecruiterUsername(job.getRecruiterUsername());
            applicationBean.setCoverLetter(coverLetter);
            applicationBean.setStudentUsername(SessionManager.getInstance().getStudentFromSession().obtainUsername());
            applicationBean.setStatus(Status.PENDING);

            boolean success = boundary.sendAJobApplication(applicationBean);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Application submitted successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to submit application.");
            }

            Platform.runLater(dialog::close);

        } catch (Exception e) {
            handleApplicationError(e, dialog);
        }
    }

    private void handleApplicationError(Exception e, Dialog<JobApplicationBean> dialog) {
        switch (e) {
            case RecruiterNotFoundException recruiterNotFoundException ->
                    showAlert(Alert.AlertType.ERROR, "Error: Recruiter not found. Please try again later.");
            case JobAnnouncementNotFoundException jobAnnouncementNotFoundException ->
                    showAlert(Alert.AlertType.ERROR, "Error: Job announcement not found. It may have been removed.");
            case JobAnnouncementNotActiveException jobAnnouncementNotActiveException ->
                    showAlert(Alert.AlertType.ERROR, "Error: This job announcement is no longer active.");
            case JobApplicationAlreadySentException jobApplicationAlreadySentException ->
                    showAlert(Alert.AlertType.WARNING, "Warning: You have already applied for this job.");
            case UnauthorizedAccessException unauthorizedAccessException ->
                    showAlert(Alert.AlertType.ERROR, "Error: You are not authorized to apply for this job.");
            case DatabaseException databaseException ->
                    showAlert(Alert.AlertType.ERROR, "Error: A database error occurred. Please try again later.");
            default -> { // NOSONAR
                showAlert(Alert.AlertType.ERROR, "Unexpected error: " + e.getMessage());
            }
        }


        Platform.runLater(dialog::close);
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setTitle("Notification");
        alert.showAndWait();
    }

    public void setContext(GUIContext context) {
        this.context = context;
    }

    public GUIContext getContext() {
        return context;
    }

    @FXML
    private void handleGoBack() {
        if (context != null) {
            Printer.print("Going back to Home...");
            context.goNext("go_home");
        } else {
            Printer.print("Context is NOT initialized in SendAJobApplicationByStudentController!");
        }
    }

}

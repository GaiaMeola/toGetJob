package org.example.togetjob.view.gui.controllergrafico;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
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
            "-fx-font-size: 18px; -fx-padding: 14 24;";
    private static final String JOB_DETAIL_STYLE = "-fx-font-family: 'Apple Gothic'; -fx-font-size: 18px; -fx-text-fill: #2980b9;";
    private static final String JOB_DETAIL_STYLE_TITLE = "-fx-font-family: 'Apple Gothic'; -fx-font-weight: bold; -fx-font-size: 18px; -fx-text-fill: #b3d9ff;";


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
    private void initialize() {
        setupTableColumns();
    }

    private void setupTableColumns() {
        jobTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJobTitle()));
        companyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCompanyName()));

        jobsTable.setPrefSize(600, 400);
        jobsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //NOSONAR
        jobsTable.setStyle("-fx-background-color: #f0f8ff; -fx-border-radius: 10; -fx-padding: 10px;");

        jobTitleColumn.setPrefWidth(250);
        companyColumn.setPrefWidth(200);
        actionsColumn.setPrefWidth(170);

        jobTitleColumn.setCellFactory(param -> createTableCellStyle());
        companyColumn.setCellFactory(param -> createTableCellStyle());

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewDetailsButton = new Button("View Details");

            { //NOSONAR
                viewDetailsButton.setStyle(BUTTON_STYLE);
                viewDetailsButton.setOnAction(event -> {
                    JobAnnouncementBean job = getTableView().getItems().get(getIndex());
                    openJobDetailsPopup(job);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : new HBox(viewDetailsButton));
            }
        });
    }

    private TableCell<JobAnnouncementBean, String> createTableCellStyle() {
        return new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item);
                setStyle(COMMON_FONT_STYLE + " -fx-padding: 12px;");
            }
        };
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
        Stage detailsStage = new Stage();
        detailsStage.setTitle("Job Details: " + job.getJobTitle());

        GridPane grid = createJobDetailsGrid(job);

        Button applyButton = new Button("Apply");
        applyButton.setStyle(BUTTON_STYLE);
        applyButton.setOnAction(event -> openJobApplicationForm(job));

        HBox buttonBox = new HBox(12, applyButton);
        buttonBox.setAlignment(Pos.CENTER);

        grid.add(buttonBox, 0, 9, 2, 1);  // Span across two columns

        Scene scene = new Scene(grid, 350, 450);
        detailsStage.setScene(scene);
        detailsStage.show();
    }

    private GridPane createJobDetailsGrid(JobAnnouncementBean job) {
        GridPane grid = new GridPane();
        grid.setVgap(12);
        grid.setHgap(25);
        grid.setStyle("-fx-padding: 20; -fx-alignment: center; -fx-background-color: white; -fx-font-family: 'AppleGothic';");


        ColumnConstraints col1 = new ColumnConstraints();
        col1.setPercentWidth(40);
        ColumnConstraints col2 = new ColumnConstraints();
        col2.setPercentWidth(60);

        grid.getColumnConstraints().addAll(col1, col2);


        addJobDetailToGrid(grid, "Job Title:", job.getJobTitle(), 0);
        addJobDetailToGrid(grid, "Company:", job.getCompanyName(), 1);
        addJobDetailToGrid(grid, "Role:", job.getRole(), 2);
        addJobDetailToGrid(grid, "Type:", job.getJobType(), 3);
        addJobDetailToGrid(grid, "Location:", job.getLocation(), 4);
        addJobDetailToGrid(grid, "Salary:", job.getSalary(), 5);


        Label descriptionLabel = new Label("Description:");
        descriptionLabel.setStyle(JOB_DETAIL_STYLE_TITLE);
        TextArea descriptionArea = new TextArea(job.getDescription());
        descriptionArea.setWrapText(true);
        descriptionArea.setEditable(false);
        descriptionArea.setStyle("-fx-background-color: white; -fx-border-color: transparent; -fx-font-family: 'AppleGothic'; -fx-font-size: 12;");
        descriptionArea.setPrefHeight(100);

        grid.add(descriptionLabel, 0, 6);
        grid.add(descriptionArea, 1, 6);

        addJobDetailToGrid(grid, "Status:", job.isActive() ? "Active" : "Inactive", 7);
        addJobDetailToGrid(grid, "Recruiter:", job.getRecruiterUsername(), 8);

        return grid;
    }



    private void addJobDetailToGrid(GridPane grid, String label, String value, int row) {
        Label jobLabel = new Label(label);
        jobLabel.setStyle(JOB_DETAIL_STYLE_TITLE);
        Label jobValue = new Label(value);
        jobValue.setStyle(JOB_DETAIL_STYLE);

        grid.add(jobLabel, 0, row);
        grid.add(jobValue, 1, row);
    }


    private void openJobApplicationForm(JobAnnouncementBean job) {
        Dialog<JobApplicationBean> dialog = new Dialog<>();
        dialog.setTitle("Apply for: " + job.getJobTitle());
        dialog.setHeaderText("Submit your application for " + job.getCompanyName());

        TextArea coverLetterField = new TextArea();
        coverLetterField.setPromptText("Write your cover letter here...");
        coverLetterField.setPrefRowCount(6);
        coverLetterField.setStyle(COMMON_FONT_STYLE);

        VBox formBox = new VBox(15, new Label("Cover Letter:"), coverLetterField);
        formBox.setStyle("-fx-padding: 25; -fx-border-color: #2980b9;");

        dialog.getDialogPane().setContent(formBox);
        dialog.getDialogPane().getButtonTypes().add(new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE));

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
            JobApplicationBean applicationBean = createJobApplicationBean(job, coverLetter);

            boolean success = boundary.sendAJobApplication(applicationBean);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Application submitted successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to submit application.");
            }

            Platform.runLater(() -> {
                dialog.close();

                Stage detailsStage = (Stage) dialog.getOwner();
                detailsStage.close();

                loadJobAnnouncements();

                if (context != null) {
                    context.goNext("filtered_jobs");
                }
            });

        } catch (Exception e) {
            handleApplicationError(e, dialog);
        }
    }

    private JobApplicationBean createJobApplicationBean(JobAnnouncementBean job, String coverLetter) {
        JobApplicationBean applicationBean = new JobApplicationBean();
        applicationBean.setJobTitle(job.getJobTitle());
        applicationBean.setRecruiterUsername(job.getRecruiterUsername());
        applicationBean.setCoverLetter(coverLetter);
        applicationBean.setStudentUsername(SessionManager.getInstance().getStudentFromSession().obtainUsername());
        applicationBean.setStatus(Status.PENDING);
        return applicationBean;
    }

    private void handleApplicationError(Exception e, Dialog<JobApplicationBean> dialog) {
        String errorMessage = switch (e) {
            case RecruiterNotFoundException ignored -> "Error: Recruiter not found. Please try again later.";
            case JobAnnouncementNotFoundException ignored -> "Error: Job announcement not found. It may have been removed.";
            case JobAnnouncementNotActiveException ignored -> "Error: This job announcement is no longer active.";
            case JobApplicationAlreadySentException ignored -> "Warning: You have already applied for this job.";
            case UnauthorizedAccessException ignored -> "Error: You are not authorized to apply for this job.";
            case DatabaseException ignored -> "Error: A database error occurred. Please try again later.";
            default -> "Unexpected error: " + e.getMessage();
        };
        showAlert(Alert.AlertType.ERROR, errorMessage);
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


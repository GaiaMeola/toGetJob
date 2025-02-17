package org.example.togetjob.view.gui.controllergrafico;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.JobApplicationBean;
import org.example.togetjob.view.boundary.PublishAJobAnnouncementRecruiterBoundary;
import org.example.togetjob.view.boundary.SendAJobApplicationRecruiterBoundary;
import org.example.togetjob.state.GUIContext;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SendAJobApplicationRecruiterController {

    private static final String BUTTON_STYLE = "-fx-background-color: #b3d9ff; " +
            "-fx-text-fill: #2980b9; " +
            "-fx-border-radius: 10; " +
            "-fx-border-color: #2980b9; " +
            "-fx-border-width: 2; " +
            "-fx-cursor: hand; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 10; " +
            "-fx-font-size: 14px;" +
            "-fx-padding: 10 20;";

    @FXML
    private TableView<JobAnnouncementBean> jobAnnouncementsTable;
    @FXML
    private TableColumn<JobAnnouncementBean, String> jobTitleColumn;
    @FXML
    private TableColumn<JobAnnouncementBean, String> companyColumn;
    @FXML
    private TableColumn<JobAnnouncementBean, Void> actionsColumn;

    private GUIContext context;
    private final PublishAJobAnnouncementRecruiterBoundary recruiterBoundary = new PublishAJobAnnouncementRecruiterBoundary();
    private final SendAJobApplicationRecruiterBoundary sendAJobApplicationRecruiterBoundary = new SendAJobApplicationRecruiterBoundary();

    public void setContext(GUIContext context) {
        this.context = context;
        loadJobAnnouncements();
    }

    private void loadJobAnnouncements() {
        try {
            List<JobAnnouncementBean> jobAnnouncements = recruiterBoundary.getJobAnnouncements();
            ObservableList<JobAnnouncementBean> observableList = FXCollections.observableArrayList(jobAnnouncements);
            jobAnnouncementsTable.setItems(observableList);
            if (jobAnnouncements.isEmpty()) {
                jobAnnouncementsTable.setPlaceholder(new Label("No content available"));
            }
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "An error occurred while loading the job announcements. Please try again later.");
        }
    }

    @FXML
    private void initialize() {
        jobTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJobTitle()));
        companyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCompanyName()));

        actionsColumn.setCellFactory(c -> new TableCell<>() {
            private final Button viewApplicationsButton = new Button("View Job Applications");
            private final HBox buttonsBox = new HBox(viewApplicationsButton);

            { //NOSONAR
                viewApplicationsButton.setStyle(BUTTON_STYLE);
                viewApplicationsButton.setOnAction(event -> viewJobApplications(getTableView().getItems().get(getIndex())));
                buttonsBox.setStyle("-fx-alignment: center; -fx-font-size: 12px;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonsBox);
            }
        });
    }

    private void viewJobApplications(JobAnnouncementBean jobAnnouncement) {
        List<JobApplicationBean> jobApplications = sendAJobApplicationRecruiterBoundary.getAllJobApplications(jobAnnouncement)
                .stream()
                .filter(application -> "Pending".equalsIgnoreCase(String.valueOf(application.getStatus())))
                .toList();

        Stage applicationsStage = new Stage();
        applicationsStage.setTitle("Job Applications for: " + jobAnnouncement.getJobTitle());

        TableView<JobApplicationBean> applicationsTable = new TableView<>();

        TableColumn<JobApplicationBean, String> studentNameColumn = new TableColumn<>("Student Name");
        studentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudentUsername()));

        TableColumn<JobApplicationBean, String> coverLetterColumn = new TableColumn<>("Cover Letter");
        coverLetterColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCoverLetter()));

        TableColumn<JobApplicationBean, Void> actionsColumnForApplications = getJobApplicationBeanVoidTableColumn(applicationsTable, jobApplications);

        applicationsTable.getColumns().addAll(studentNameColumn, coverLetterColumn, actionsColumnForApplications);
        applicationsTable.setItems(FXCollections.observableArrayList(jobApplications));

        coverLetterColumn.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                } else {
                    TextArea textArea = new TextArea(item);
                    textArea.setWrapText(true);
                    textArea.setFont(Font.font(18));
                    textArea.setEditable(false);
                    textArea.setPrefRowCount(3);
                    textArea.setMaxWidth(450);
                    setGraphic(textArea);
                }
            }
        });

        Scene scene = new Scene(applicationsTable, 600, 400);
        applicationsStage.setScene(scene);
        applicationsStage.show();
    }

    @NotNull
    private TableColumn<JobApplicationBean, Void> getJobApplicationBeanVoidTableColumn(TableView<JobApplicationBean> applicationsTable, List<JobApplicationBean> jobApplications) {
        TableColumn<JobApplicationBean, Void> actionsColumnForApplications = new TableColumn<>("Actions");
        actionsColumnForApplications.setCellFactory(column -> new TableCell<>() {
            private final Button acceptButton = new Button("Accept");
            private final Button rejectButton = new Button("Reject");

            { //NOSONAR
                acceptButton.setStyle(BUTTON_STYLE);
                rejectButton.setStyle(BUTTON_STYLE);
                acceptButton.setTextFill(javafx.scene.paint.Color.WHITE);
                rejectButton.setTextFill(javafx.scene.paint.Color.WHITE);

                ObservableList<JobApplicationBean> observableJobApplications = FXCollections.observableArrayList(jobApplications);
                acceptButton.setOnAction(event -> handleAcceptApplication(getTableView().getItems().get(getIndex()), observableJobApplications, applicationsTable));
                rejectButton.setOnAction(event -> handleRejectApplication(getTableView().getItems().get(getIndex()), observableJobApplications, applicationsTable));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                HBox buttonBox = new HBox(15, acceptButton, rejectButton);
                buttonBox.setAlignment(Pos.CENTER);
                setGraphic(empty ? null : buttonBox);
            }
        });
        return actionsColumnForApplications;
    }

    private void handleAcceptApplication(JobApplicationBean jobApplication, ObservableList<JobApplicationBean> jobApplications, TableView<JobApplicationBean> applicationsTable) {
        boolean success = sendAJobApplicationRecruiterBoundary.acceptJobApplication(jobApplication);
        if (success) {
            Platform.runLater(() -> {
                jobApplications.remove(jobApplication);
                applicationsTable.setItems(FXCollections.observableArrayList(jobApplications));
                showAlert(Alert.AlertType.INFORMATION, "Application Accepted!");
            });
        } else {
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Failed to Accept Application."));
        }
    }

    private void handleRejectApplication(JobApplicationBean jobApplication, ObservableList<JobApplicationBean> jobApplications, TableView<JobApplicationBean> applicationsTable) {
        boolean success = sendAJobApplicationRecruiterBoundary.rejectJobApplication(jobApplication);
        if (success) {
            Platform.runLater(() -> {
                jobApplications.remove(jobApplication);
                applicationsTable.setItems(FXCollections.observableArrayList(jobApplications));
                showAlert(Alert.AlertType.INFORMATION, "Application Rejected!");
            });
        } else {
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "Failed to Reject Application."));
        }
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setTitle("Job Application");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    private void handleGoBack() {
        context.goNext("go_home");
    }
}

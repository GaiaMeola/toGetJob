package org.example.togetjob.view.gui.controllergrafico;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private static final String TEXT_STILE = "-fx-text-fill: #2980b9;";
    private static final Logger LOGGER = Logger.getLogger(SendAJobApplicationRecruiterController.class.getName());
    private static final String COMMON_FONT_STYLE = "-fx-font-family: 'Apple Gothic'; -fx-font-size: 16px; -fx-text-fill: #2980b9;";

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
        new Thread(() -> {
            try {
                List<JobAnnouncementBean> jobAnnouncements = recruiterBoundary.getJobAnnouncements();
                Platform.runLater(() -> updateJobAnnouncementsTable(jobAnnouncements));
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "An error occurred while loading job announcements", e);
                Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, "An error occurred while loading the job announcements. Please try again later."));
            }
        }).start();
    }

    private void updateJobAnnouncementsTable(List<JobAnnouncementBean> jobAnnouncements) {
        ObservableList<JobAnnouncementBean> observableList = FXCollections.observableArrayList(jobAnnouncements);
        jobAnnouncementsTable.setItems(observableList);
        if (jobAnnouncements.isEmpty()) {
            jobAnnouncementsTable.setPlaceholder(new Label("No content available"));
        }
    }

    @FXML
    private void initialize() {
        jobTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJobTitle()));
        companyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCompanyName()));

        jobAnnouncementsTable.setPrefSize(600, 400);
        jobAnnouncementsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); //NOSONAR
        jobAnnouncementsTable.setStyle("-fx-background-color: #f0f8ff; -fx-border-radius: 10; -fx-padding: 10px;");

        jobTitleColumn.setPrefWidth(250);
        companyColumn.setPrefWidth(180);
        actionsColumn.setPrefWidth(220);

        actionsColumn.setCellFactory(c -> new TableCell<>() {
            private final Button viewApplicationsButton = new Button("View Job Applications");
            private final HBox buttonsBox = new HBox(viewApplicationsButton);

            { //NOSONAR
                viewApplicationsButton.setStyle(BUTTON_STYLE);
                viewApplicationsButton.setOnAction(event -> viewJobApplications(getTableView().getItems().get(getIndex())));
                buttonsBox.setAlignment(Pos.CENTER);
                buttonsBox.setSpacing(10);
                buttonsBox.setPadding(new Insets(5));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonsBox);
            }
        });

        jobAnnouncementsTable.setStyle(
                "-fx-background-color: #f0f8ff;" +  // Light background for better readability
                        "-fx-border-color: #2980b9;" +
                        "-fx-border-radius: 10;" +
                        "-fx-border-width: 2;" +
                        COMMON_FONT_STYLE +
                        "-fx-font-family: 'Apple Gothic';" +
                        "-fx-selection-bar: #85C1E9;" +
                        "-fx-selection-bar-non-focused: #AED6F1;"
        );

        jobTitleColumn.setStyle(COMMON_FONT_STYLE);
        companyColumn.setStyle(COMMON_FONT_STYLE);
        actionsColumn.setStyle(COMMON_FONT_STYLE);
    }

    private void viewJobApplications(JobAnnouncementBean jobAnnouncement) {
        List<JobApplicationBean> jobApplications = sendAJobApplicationRecruiterBoundary.getAllJobApplications(jobAnnouncement)
                .stream()
                .filter(application -> "Pending".equalsIgnoreCase(String.valueOf(application.getStatus())))
                .toList();

        Stage applicationsStage = new Stage();
        applicationsStage.setTitle("Job Applications for: " + jobAnnouncement.getJobTitle());

        TableView<JobApplicationBean> applicationsTable = new TableView<>();
        applicationsTable.setStyle("-fx-background-color: #b3d9ff; "
                + "-fx-text-fill: #2980b9; "
                + "-fx-border-radius: 10; "
                + "-fx-border-color: #2980b9; "
                + "-fx-border-width: 2; "
                + "-fx-font-weight: bold; "
                + "-fx-background-radius: 10; "
                + "-fx-font-size: 14px;"
                + "-fx-font-family: 'Apple Gothic';"
        );

        // Student Name Column (with fixed width)
        TableColumn<JobApplicationBean, String> studentNameColumn = new TableColumn<>("Student Name");
        studentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudentUsername()));
        studentNameColumn.setPrefWidth(150);  // Set fixed width

        // Cover Letter Column (with fixed width)
        TableColumn<JobApplicationBean, String> coverLetterColumn = new TableColumn<>("Cover Letter");
        coverLetterColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCoverLetter()));
        coverLetterColumn.setPrefWidth(250);  // Set fixed width for cover letter

        // Actions column (buttons for acceptance and rejection)
        TableColumn<JobApplicationBean, Void> actionsColumnForApplications = getJobApplicationBeanVoidTableColumn(applicationsTable, jobApplications);

        applicationsTable.getColumns().addAll(studentNameColumn, coverLetterColumn, actionsColumnForApplications);
        applicationsTable.setItems(FXCollections.observableArrayList(jobApplications));

        studentNameColumn.setStyle(TEXT_STILE);
        coverLetterColumn.setStyle(TEXT_STILE);
        actionsColumnForApplications.setStyle(TEXT_STILE);

        // Set the Cover Letter cell factory to make it scrollable
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
                    textArea.setPrefRowCount(3);  // You can adjust the number of rows as needed
                    textArea.setMaxWidth(250);  // Set max width to match the column width
                    textArea.setStyle("-fx-background-color: #f5f5f5; "
                            + "-fx-border-color: #2980b9; "
                            + "-fx-border-radius: 10; "
                            + "-fx-border-width: 1; "
                            + "-fx-padding: 5 10;");
                    // Enable scrolling if content exceeds the visible area
                    textArea.setScrollTop(0);
                    setGraphic(textArea);
                }
            }
        });

        Scene scene = new Scene(applicationsTable, 500, 500);
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
                acceptButton.setOnAction(event -> handleJobApplicationAction(getTableView().getItems().get(getIndex()), observableJobApplications, applicationsTable, true));
                rejectButton.setOnAction(event -> handleJobApplicationAction(getTableView().getItems().get(getIndex()), observableJobApplications, applicationsTable, false));
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

    private void handleJobApplicationAction(JobApplicationBean jobApplication, ObservableList<JobApplicationBean> jobApplications, TableView<JobApplicationBean> applicationsTable, boolean isAccept) {
        boolean success = isAccept
                ? sendAJobApplicationRecruiterBoundary.acceptJobApplication(jobApplication)
                : sendAJobApplicationRecruiterBoundary.rejectJobApplication(jobApplication);

        if (success) {
            Platform.runLater(() -> {
                jobApplications.remove(jobApplication);
                applicationsTable.setItems(FXCollections.observableArrayList(jobApplications));
                showAlert(Alert.AlertType.INFORMATION, isAccept ? "Application Accepted!" : "Application Rejected!");
            });
        } else {
            Platform.runLater(() -> showAlert(Alert.AlertType.ERROR, isAccept ? "Failed to Accept Application." : "Failed to Reject Application."));
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
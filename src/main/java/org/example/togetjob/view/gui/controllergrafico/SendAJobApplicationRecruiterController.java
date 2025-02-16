package org.example.togetjob.view.gui.controllergrafico;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.JobApplicationBean;
import org.example.togetjob.view.boundary.PublishAJobAnnouncementRecruiterBoundary;
import org.example.togetjob.view.boundary.SendAJobApplicationRecruiterBoundary;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.view.gui.concretestate.HomeRecruiterState;
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
            "-fx-font-size: 14px;" + // Set font size for buttons
            "-fx-padding: 10 20;"; // Set padding for larger buttons

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

    // Set the context and load the job announcements into the table
    public void setContext(GUIContext context) {
        this.context = context;
        loadJobAnnouncements();
    }

    // Load job announcements from the boundary and display them in the table
    private void loadJobAnnouncements() {
        try {
            List<JobAnnouncementBean> jobAnnouncements = recruiterBoundary.getJobAnnouncements();
            ObservableList<JobAnnouncementBean> observableList = FXCollections.observableArrayList(jobAnnouncements);
            jobAnnouncementsTable.setItems(observableList);

            // Display placeholder text when no job announcements are available
            if (jobAnnouncements.isEmpty()) {
                jobAnnouncementsTable.setPlaceholder(new Label("No content available"));
            }
        } catch (Exception e) {
            // Show an error alert without logging
            showAlert(Alert.AlertType.ERROR, "An error occurred while loading the job announcements. Please try again later.");
        }
    }

    @FXML
    private void initialize() {
        // Set up the cell value factories for each column
        jobTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJobTitle()));
        companyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCompanyName()));

        // Set the font size for the columns
        setColumnFont(jobTitleColumn);
        setColumnFont(companyColumn);
        setColumnFont(actionsColumn);

        // Make the columns equally spaced
        setColumnAcquiesced(jobTitleColumn);
        setColumnAcquiesced(companyColumn);
        setColumnAcquiesced(actionsColumn);

        // Set up actions column with a "View Job Applications" button
        actionsColumn.setCellFactory(c -> new TableCell<>() {
            private final Button viewApplicationsButton = new Button("View Job Applications");
            private final HBox buttonsBox = new HBox(viewApplicationsButton);

            {
                viewApplicationsButton.setStyle(BUTTON_STYLE);
                viewApplicationsButton.setOnAction(event -> viewJobApplications(getTableView().getItems().get(getIndex())));
                buttonsBox.setStyle("-fx-alignment: center;" + "-fx-font-size: 12px;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonsBox);
            }
        });
    }

    // Set the font size for a column
    private void setColumnFont(TableColumn<?, ?> column) {
        column.setStyle("-fx-font-size: " + (double) 18 + "px;");
    }

    // Make the columns equally spaced
    private void setColumnAcquiesced(TableColumn<?, ?> column) {
        column.setResizable(true);
        column.prefWidthProperty().bind(jobAnnouncementsTable.widthProperty().divide(jobAnnouncementsTable.getColumns().size()));
    }

    // Display job applications for a selected job announcement
    private void viewJobApplications(JobAnnouncementBean jobAnnouncement) {

        // Fetch only "Pending" job applications
        List<JobApplicationBean> jobApplications = sendAJobApplicationRecruiterBoundary.getAllJobApplications(jobAnnouncement).stream()
                .filter(application -> "Pending".equalsIgnoreCase(String.valueOf(application.getStatus())))  // Filter for Pending applications
                .toList();

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Job Applications for: " + jobAnnouncement.getJobTitle());

        dialog.getDialogPane().setMinWidth(600);
        dialog.getDialogPane().setMinHeight(400);

        TableView<JobApplicationBean> applicationsTable = new TableView<>();

        // Create column for student name
        TableColumn<JobApplicationBean, String> studentNameColumn = new TableColumn<>("Student Name");
        studentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudentUsername()));

        // Create column for cover letter, with wrapping text
        TableColumn<JobApplicationBean, String> coverLetterColumn = new TableColumn<>("Cover Letter");
        coverLetterColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCoverLetter()));

        // Create the actions column for Accept/Reject buttons
        TableColumn<JobApplicationBean, Void> actionsColumnForApplications = getJobApplicationBeanVoidTableColumn(jobApplications);

        // Set font size for each column
        setColumnFont(studentNameColumn);
        setColumnFont(coverLetterColumn); // Increase font size for cover letter column
        setColumnFont(actionsColumnForApplications);

        // Add columns to the table
        applicationsTable.getColumns().clear();
        applicationsTable.getColumns().add(studentNameColumn);
        applicationsTable.getColumns().add(coverLetterColumn);
        applicationsTable.getColumns().add(actionsColumnForApplications);

        // Make the columns equally spaced
        setColumnAcquiesced(studentNameColumn);
        setColumnAcquiesced(coverLetterColumn);
        setColumnAcquiesced(actionsColumnForApplications);

        // Use TextArea for cover letter to allow for longer text wrapping
        coverLetterColumn.setCellFactory(column -> {
            return new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        TextArea textArea = new TextArea(item);
                        textArea.setWrapText(true); // Enable text wrapping
                        textArea.setFont(Font.font(18)); // Larger font size
                        textArea.setEditable(false); // Disable text editing
                        textArea.setPrefRowCount(3); // Set a default row count
                        textArea.setMaxWidth(450); // Set a maximum width for the text area
                        setGraphic(textArea);
                    }
                }
            };
        });

        // Set the items (job applications) in the table
        ObservableList<JobApplicationBean> applicationsList = FXCollections.observableArrayList(jobApplications);
        applicationsTable.setItems(applicationsList);

        // Style the table
        applicationsTable.setStyle("-fx-font-size: 18px; -fx-padding: 5px;");

        // Add the table to the dialog and display
        dialog.getDialogPane().setContent(applicationsTable);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }

    // Create the actions column with Accept/Reject buttons
    @NotNull
    private TableColumn<JobApplicationBean, Void> getJobApplicationBeanVoidTableColumn(List<JobApplicationBean> jobApplications) {
        TableColumn<JobApplicationBean, Void> actionsColumnForApplications = new TableColumn<>("Actions");
        actionsColumnForApplications.setCellFactory(column -> new TableCell<>() {
            private final Button acceptButton = new Button("Accept");
            private final Button rejectButton = new Button("Reject");

            { // NOSONAR
                // Set the Accept and Reject buttons style (same BUTTON_STYLE as you defined)
                acceptButton.setStyle(BUTTON_STYLE);
                rejectButton.setStyle(BUTTON_STYLE);

                // Ensure text is clearly visible and set proper font size
                acceptButton.setTextFill(javafx.scene.paint.Color.WHITE);  // White text color
                rejectButton.setTextFill(javafx.scene.paint.Color.WHITE);  // White text color

                // Set custom dimensions for the buttons (adjust width and height)
                acceptButton.setMinWidth(120); // Set a minimum width for the Accept button
                acceptButton.setMinHeight(40); // Set a minimum height for the Accept button

                rejectButton.setMinWidth(120); // Set a minimum width for the Reject button
                rejectButton.setMinHeight(40); // Set a minimum height for the Reject button

                // Handle button actions
                acceptButton.setOnAction(event -> handleAcceptApplication(getTableView().getItems().get(getIndex()), jobApplications));
                rejectButton.setOnAction(event -> handleRejectApplication(getTableView().getItems().get(getIndex()), jobApplications));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                // Create a HBox to hold the buttons with some alignment and spacing
                HBox buttonBox = new HBox(15, acceptButton, rejectButton); // Increased spacing between buttons
                buttonBox.setStyle("-fx-alignment: center; -fx-padding: 10;"); // Alignment centered and padding around buttons

                // Set the alignment of buttons to be centered in the HBox
                buttonBox.setAlignment(Pos.CENTER); // Ensure buttons are centered horizontally

                // Set the graphic of the cell, or clear it if empty
                setGraphic(empty ? null : buttonBox);
            }
        });
        return actionsColumnForApplications;
    }

    // Handle the acceptance of a job application
    private void handleAcceptApplication(JobApplicationBean jobApplication, List<JobApplicationBean> jobApplications) {
        boolean success = sendAJobApplicationRecruiterBoundary.acceptJobApplication(jobApplication);
        if (success) {
            jobApplications.remove(jobApplication); // Remove accepted application from the list
            showAlert(Alert.AlertType.INFORMATION, "Application Accepted!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to Accept Application.");
        }
    }

    // Handle the rejection of a job application
    private void handleRejectApplication(JobApplicationBean jobApplication, List<JobApplicationBean> jobApplications) {
        boolean success = sendAJobApplicationRecruiterBoundary.rejectJobApplication(jobApplication);
        if (success) {
            jobApplications.remove(jobApplication); // Remove rejected application from the list
            showAlert(Alert.AlertType.INFORMATION, "Application Rejected!");
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed to Reject Application.");
        }
    }

    // Display an alert with the result of accepting/rejecting the application
    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setTitle("Job Application");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    // Go back to the previous screen
    @FXML
    private void handleGoBack() {
        if (context != null) {
            context.setState(new HomeRecruiterState(context));
            context.showMenu();
        }
    }
}

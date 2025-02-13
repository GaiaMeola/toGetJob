package org.example.togetjob.view.gui.controllergrafico;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.JobAnnouncementSearchBean;
import org.example.togetjob.bean.JobApplicationBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.boundary.SendAJobApplicationStudentBoundary;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.concretestate.HomeStudentState;

import java.util.List;

public class SendAJobApplicationByStudentController {


    private static final String COMMON_FONT_STYLE = "-fx-font-family: AppleGothic Regular; -fx-font-size: 18px; -fx-text-fill: #2980b9;";

    private static final String BUTTON_STYLE = "-fx-background-color: #b3d9ff; " +
            "-fx-text-fill: #2980b9; " +
            "-fx-border-radius: 10; " +
            "-fx-border-color: #2980b9; " +
            "-fx-border-width: 2; " +
            "-fx-cursor: hand; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 10;";

    @FXML
    private TableView<JobAnnouncementBean> jobsTable;
    @FXML
    private TableColumn<JobAnnouncementBean, String> jobTitleColumn;
    @FXML
    private TableColumn<JobAnnouncementBean, String> companyColumn;
    @FXML
    private TableColumn<JobAnnouncementBean, Void> actionsColumn;

    private GUIContext guiContext;
    private JobAnnouncementSearchBean jobAnnouncementSearchBean;
    private final SendAJobApplicationStudentBoundary boundary = new SendAJobApplicationStudentBoundary();

    public void setContext(GUIContext context) {
        this.guiContext = context;
    }

    public void setJobAnnouncementSearchBean(JobAnnouncementSearchBean jobAnnouncementSearchBean) {
        this.jobAnnouncementSearchBean = jobAnnouncementSearchBean;
        if (jobsTable != null) {
            loadJobAnnouncements();
        }
    }

    @FXML
    private void initialize() {


        jobTitleColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJobTitle()));
        companyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCompanyName()));

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button viewDetailsButton = new Button("View Details");
            private final Button sendApplicationButton = new Button("Send a job application");
            private final VBox buttonsBox = new VBox(10, viewDetailsButton, sendApplicationButton);

            {
                viewDetailsButton.setStyle(BUTTON_STYLE);
                sendApplicationButton.setStyle(BUTTON_STYLE);

                viewDetailsButton.setOnAction(event -> viewJobDetails(getTableView().getItems().get(getIndex())));
                sendApplicationButton.setOnAction(event -> openJobApplicationForm(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : buttonsBox);
            }
        });

        if (jobAnnouncementSearchBean != null) {
            loadJobAnnouncements();
        }
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

    private void viewJobDetails(JobAnnouncementBean job) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Job Details");
        alert.setHeaderText("Position: " + job.getJobTitle() + " at " + job.getCompanyName());

        String details = """
                📌 Role: %s
                🏢 Job Type: %s
                📍 Location: %s
                ⏰ Working Hours: %s
                💰 Salary: %s
                """.formatted(
                job.getRole(),
                job.getJobType(),
                job.getLocation(),
                job.getWorkingHours(),
                job.getSalary()
        );


        alert.setContentText(details);
        alert.getDialogPane().setStyle(COMMON_FONT_STYLE);
        alert.showAndWait();
    }


    private void openJobApplicationForm(JobAnnouncementBean job) {
        JobApplicationBean applicationBean = boundary.fillJobApplicationForm(job);

        Dialog<JobApplicationBean> dialog = new Dialog<>();
        dialog.setTitle("Apply for: " + job.getJobTitle());

        TextArea coverLetterField = new TextArea();
        coverLetterField.setPromptText("Write your cover letter here...");
        coverLetterField.setPrefRowCount(5);
        coverLetterField.setStyle(COMMON_FONT_STYLE);

        VBox formBox = new VBox(10, new Label("Cover Letter:"), coverLetterField);
        formBox.setStyle("-fx-padding: 10;");

        dialog.getDialogPane().setContent(formBox);
        dialog.getDialogPane().getButtonTypes().addAll(
                new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE),
                new ButtonType("Go Back", ButtonBar.ButtonData.CANCEL_CLOSE)
        );

        dialog.setOnShown(event ->
                Platform.runLater(() -> {
                    dialog.getDialogPane().lookupButton(ButtonType.OK).setStyle(BUTTON_STYLE);  // Applica lo stile al bottone "Submit"
                    dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setStyle(BUTTON_STYLE);  // Applica lo stile al bottone "Go Back"
                })
        );

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                applicationBean.setCoverLetter(coverLetterField.getText());
                boolean success = boundary.sendAJobApplication(applicationBean);
                showAlert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                        success ? "Application submitted successfully!" : "Failed to submit application.");
                return applicationBean;
            }
            return null;
        });

        dialog.showAndWait();
    }


    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setTitle("Job Application");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    private void handleGoBack() {
        if (guiContext != null) {
            guiContext.setState(new HomeStudentState(guiContext));
            guiContext.showMenu();
        }
    }
}

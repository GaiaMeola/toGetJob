package org.example.togetjob.view.gui.controllergrafico;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.JobAnnouncementSearchBean;
import org.example.togetjob.bean.JobApplicationBean;
import org.example.togetjob.exceptions.*;
import org.example.togetjob.model.entity.Status;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.session.SessionManager;
import org.example.togetjob.view.boundary.SendAJobApplicationStudentBoundary;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.view.gui.concretestate.FilterJobAnnouncementStudentState;

import java.util.List;

public class SendAJobApplicationByStudentController {

    private static final String COMMON_FONT_STYLE = "-fx-font-family: 'Apple Gothic'; -fx-font-size: 18px; -fx-text-fill: #2980b9;";
    private static final String BUTTON_STYLE = "-fx-background-color: #b3d9ff; -fx-text-fill: #2980b9; " +
            "-fx-border-radius: 5; -fx-border-color: #2980b9; -fx-border-width: 2; " +
            "-fx-cursor: hand; -fx-font-weight: bold; -fx-background-radius: 5;" +
            "-fx-font-size: 14px; -fx-padding: 10 20;";
    private static final String BOLD_JOB_TITLE_STYLE = "-fx-font-family: 'Apple Gothic'; -fx-font-size: 18px; -fx-text-fill: #2980b9; -fx-font-weight: bold;";

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


        jobTitleColumn.setPrefWidth(300);
        companyColumn.setPrefWidth(250);
        actionsColumn.setPrefWidth(200);


        jobTitleColumn.setCellFactory(param -> new TableCell<JobAnnouncementBean, String>() {
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

        companyColumn.setCellFactory(param -> new TableCell<JobAnnouncementBean, String>() {
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

            {
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
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Job Details: " + job.getJobTitle());
        dialog.setHeaderText(job.getCompanyName());


        Label detailsLabel = new Label("Details:");
        detailsLabel.setStyle(COMMON_FONT_STYLE + "-fx-font-weight: bold; -fx-font-size: 20px;");


        Label detailsContent = new Label();
        detailsContent.setWrapText(true);
        detailsContent.setStyle(COMMON_FONT_STYLE);

        detailsContent.setText(
                "Role: " + job.getRole() + "\n" +
                        "Type: " + job.getJobType() + "\n" +
                        "Location: " + job.getLocation() + "\n" +
                        "Working Hours: " + job.getWorkingHours() + "\n" +
                        "Salary: " + job.getSalary()
        );


        Button applyButton = new Button("Apply");
        applyButton.setStyle(BUTTON_STYLE);
        applyButton.setOnAction(event -> openJobApplicationForm(job));

        VBox detailsBox = new VBox(10, detailsLabel, detailsContent, applyButton);
        detailsBox.setStyle("-fx-padding: 20; -fx-alignment: center;");

        dialog.getDialogPane().setMinWidth(400);
        dialog.getDialogPane().setMinHeight(300);
        dialog.getDialogPane().setContent(detailsBox);

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
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
        dialog.getDialogPane().getButtonTypes().addAll(
                new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE),
                new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE)
        );

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                try {
                    JobApplicationBean applicationBean = new JobApplicationBean();
                    applicationBean.setJobTitle(job.getJobTitle());
                    applicationBean.setRecruiterUsername(job.getRecruiterUsername());
                    applicationBean.setCoverLetter(coverLetterField.getText().trim());

                    String studentUsername = SessionManager.getInstance().getStudentFromSession().obtainUsername();
                    applicationBean.setStudentUsername(studentUsername);

                    applicationBean.setStatus(Status.PENDING);

                    boolean success = boundary.sendAJobApplication(applicationBean);
                    showAlert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR,
                            success ? "Application submitted successfully!" : "Failed to submit application.");

                } catch (RecruiterNotFoundException e) {
                    showAlert(Alert.AlertType.ERROR, "Error: Recruiter not found. Please try again later.");
                } catch (JobAnnouncementNotFoundException e) {
                    showAlert(Alert.AlertType.ERROR, "Error: Job announcement not found. It may have been removed.");
                } catch (JobAnnouncementNotActiveException e) {
                    showAlert(Alert.AlertType.ERROR, "Error: This job announcement is no longer active.");
                } catch (JobApplicationAlreadySentException e) {
                    showAlert(Alert.AlertType.WARNING, "Warning: You have already applied for this job.");
                } catch (UnauthorizedAccessException e) {
                    showAlert(Alert.AlertType.ERROR, "Error: You are not authorized to apply for this job.");
                } catch (DatabaseException e) {
                    showAlert(Alert.AlertType.ERROR, "Error: A database error occurred. Please try again later.");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Unexpected error: " + e.getMessage());
                }
            }
            return null;
        });

        dialog.showAndWait();
    }

    private void showAlert(Alert.AlertType type, String message) {
        Alert alert = new Alert(type, message);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    @FXML
    private void handleGoBack() {
        if (guiContext != null) {
            Printer.print("Going back to Filters...");
            guiContext.setState(new FilterJobAnnouncementStudentState(guiContext));
            guiContext.showMenu();
        } else {
            Printer.print("Context is NOT initialized in FilterJobAnnouncement!");
        }
    }

}

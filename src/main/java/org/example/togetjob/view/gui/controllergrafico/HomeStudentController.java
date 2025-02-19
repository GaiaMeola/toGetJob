package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.example.togetjob.bean.JobApplicationBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.boundary.LoginBoundary;
import org.example.togetjob.view.boundary.SendAJobApplicationStudentBoundary;
import org.example.togetjob.state.GUIContext;

import java.util.List;

public class HomeStudentController {

    @FXML
    private TitledPane pendingApplicationsPane;
    @FXML
    private TitledPane acceptedApplicationsPane;
    @FXML
    private TitledPane rejectedApplicationsPane;

    @FXML
    private ListView<JobApplicationBean> pendingApplicationsList;

    @FXML
    private ListView<JobApplicationBean> acceptedApplicationsList;

    @FXML
    private ListView<JobApplicationBean> rejectedApplicationsList;

    private static final String PENDING = "PENDING";
    private static final String ACCEPTED = "ACCEPTED";
    private static final String REJECTED = "REJECTED";

    private GUIContext context;
    private final LoginBoundary loginBoundary = new LoginBoundary();
    private final SendAJobApplicationStudentBoundary sendAJobApplicationStudentBoundary = new SendAJobApplicationStudentBoundary();

    public void setContext(GUIContext context) {
        this.context = context;
    }

    @FXML
    private void handleAcceptedApplications() {
        List<JobApplicationBean> allApplications = sendAJobApplicationStudentBoundary.getJobApplicationsByStudent();
        List<JobApplicationBean> acceptedApplications = allApplications.stream()
                .filter(app -> ACCEPTED.equalsIgnoreCase(String.valueOf(app.getStatus())))
                .toList();

        Printer.print("Accepted applications: " + acceptedApplications);
        populateApplications(acceptedApplications, acceptedApplicationsPane);
    }

    @FXML
    private void handleRejectedApplications() {
        List<JobApplicationBean> allApplications = sendAJobApplicationStudentBoundary.getJobApplicationsByStudent();
        List<JobApplicationBean> rejectedApplications = allApplications.stream()
                .filter(app -> REJECTED.equalsIgnoreCase(String.valueOf(app.getStatus())))
                .toList();

        Printer.print("Rejected applications: " + rejectedApplications);
        populateApplications(rejectedApplications, rejectedApplicationsPane);
    }

    @FXML
    private void handlePendingApplications() {
        List<JobApplicationBean> allApplications = sendAJobApplicationStudentBoundary.getJobApplicationsByStudent();
        List<JobApplicationBean> pendingApplications = allApplications.stream()
                .filter(app -> PENDING.equalsIgnoreCase(String.valueOf(app.getStatus())))
                .toList();

        Printer.print("Pending applications: " + pendingApplications);
        populateApplications(pendingApplications, pendingApplicationsPane);
    }

    @FXML
    private void sendAJobApplication() {
        if (context != null) {
            Printer.print("Context is initialized!");
            context.goNext("filter_jobs");
        } else {
            Printer.print("Context is not initialized!");
        }
    }

    @FXML
    private void handleLogout() {
        if (context != null) {
            Printer.print("Context is initialized!");
            loginBoundary.logout();
            context.goNext("logout");
        } else {
            Printer.print("Context is not initialized!");
        }
    }

    @FXML
    private void handleViewNotifications() {
        // * not implemented * //
    }

    private void populateApplications(List<JobApplicationBean> applications, TitledPane pane) {
        ListView<JobApplicationBean> listView = getListViewForPane(pane);
        if (listView != null) {
            listView.getItems().clear();
            listView.getItems().addAll(applications);
            listView.setCellFactory(param -> createJobApplicationCell());
        }
    }

    private ListView<JobApplicationBean> getListViewForPane(TitledPane pane) {
        if (pendingApplicationsPane.equals(pane)) {
            return pendingApplicationsList;
        } else if (acceptedApplicationsPane.equals(pane)) {
            return acceptedApplicationsList;
        } else if (rejectedApplicationsPane.equals(pane)) {
            return rejectedApplicationsList;
        }
        return null;
    }

    private ListCell<JobApplicationBean> createJobApplicationCell() {
        return new ListCell<>() {
            @Override
            protected void updateItem(JobApplicationBean app, boolean empty) {
                super.updateItem(app, empty);

                if (empty || app == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    hbox.setPadding(new Insets(5, 10, 5, 10));

                    // job title
                    Text jobTitleText = new Text(app.getJobTitle());
                    jobTitleText.setStyle("-fx-font-family: 'Apple Gothic'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-fill: #2980b9;");

                    Label statusLabel = createStatusLabel(String.valueOf(app.getStatus()));

                    hbox.getChildren().addAll(jobTitleText, statusLabel);

                    HBox buttonBox = createButtonsForApplication(app);
                    hbox.getChildren().add(buttonBox);

                    setGraphic(hbox);
                }
            }
        };
    }

    private Label createStatusLabel(String status) {
        Label statusLabel = new Label(status);
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-family: 'Apple Gothic'; " +
                getStatusColorStyle(status) +
                "-fx-padding: 5px 10px; -fx-border-radius: 5px;");
        return statusLabel;
    }

    private String getStatusColorStyle(String status) {
        switch (status.toUpperCase()) {
            case PENDING -> {
                return "-fx-background-color: #2980b9; -fx-text-fill: white;";
            }
            case ACCEPTED -> {
                return "-fx-background-color: #28A745; -fx-text-fill: white;";
            }
            case REJECTED -> {
                return "-fx-background-color: #DC3545; -fx-text-fill: white;";
            }
            default -> {
                return "-fx-background-color: #000000; -fx-text-fill: white;";
            }
        }
    }

    private HBox createButtonsForApplication(JobApplicationBean app) {
        HBox hbox = new HBox(10);

        if (PENDING.equalsIgnoreCase(String.valueOf(app.getStatus()))) {
            Button modifyButton = new Button("Modify");
            modifyButton.setOnAction(event -> modifyApplication(app));

            Button deleteButton = new Button("Delete");
            deleteButton.setOnAction(event -> deleteApplication(app));

            String buttonStyle = "-fx-background-color: #b3d9ff; -fx-text-fill: #2980b9; " +
                    "-fx-border-radius: 5; -fx-border-color: #2980b9; -fx-border-width: 2; " +
                    "-fx-cursor: hand; -fx-background-radius: 5;" +
                    "-fx-font-size: 10px;";

            modifyButton.setStyle(buttonStyle);
            deleteButton.setStyle(buttonStyle);

            hbox.getChildren().addAll(modifyButton, deleteButton);
        }

        return hbox;
    }

    private void modifyApplication(JobApplicationBean application) {
        Printer.print("Modify application: " + application.getJobTitle());
    }

    private void deleteApplication(JobApplicationBean application) {
        Printer.print("Delete application: " + application.getJobTitle());
        sendAJobApplicationStudentBoundary.deleteAJobApplication(application);
        handlePendingApplications();
        handleAcceptedApplications();
        handleRejectedApplications();
    }
}
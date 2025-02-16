package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.boundary.LoginBoundary;
import org.example.togetjob.view.boundary.PublishAJobAnnouncementRecruiterBoundary;
import org.example.togetjob.state.GUIContext;

import java.util.List;

public class HomeRecruiterController {

    @FXML
    private ListView<JobAnnouncementBean> jobAnnouncementBeanListView;

    private GUIContext context;
    private final LoginBoundary loginBoundary = new LoginBoundary();
    private final PublishAJobAnnouncementRecruiterBoundary publishBoundary = new PublishAJobAnnouncementRecruiterBoundary();

    public void setContext(GUIContext context) {
        this.context = context;
    }

    @FXML
    private void initialize() {
        List<JobAnnouncementBean> jobAnnouncements = fetchJobAnnouncements();
        populateJobAnnouncements(jobAnnouncements);
    }

    @FXML
    private void handlePublishJobAnnouncementButton() {
        if (context != null) {
            Printer.print("Going to CreateJobAnnouncement...");
            context.goNext("publishJobAnnouncement");
        } else {
            Printer.print("Context is NOT initialized in HomeRecruiter!");
        }
    }

    @FXML
    private void handleLogout() {
        if (context != null) {
            loginBoundary.logout();
            context.goNext("logout");
        } else {
            Printer.print("Context is not initialized!");
        }
    }

    private void populateJobAnnouncements(List<JobAnnouncementBean> jobAnnouncements) {
        if (jobAnnouncements != null && !jobAnnouncements.isEmpty()) {
            jobAnnouncementBeanListView.getItems().clear();
            jobAnnouncementBeanListView.getItems().addAll(jobAnnouncements);
            jobAnnouncementBeanListView.setCellFactory(param -> createJobAnnouncementCell());
        } else {
            Printer.print("No job announcements to display.");
        }
    }

    private ListCell<JobAnnouncementBean> createJobAnnouncementCell() {
        return new ListCell<JobAnnouncementBean>() {
            @Override
            protected void updateItem(JobAnnouncementBean jobAnnouncement, boolean empty) {
                super.updateItem(jobAnnouncement, empty);

                if (empty || jobAnnouncement == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(20);
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    hbox.setPadding(new Insets(5, 10, 10, 20));

                    Text jobTitleText = new Text(jobAnnouncement.getJobTitle());
                    jobTitleText.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-fill: #2980b9; -fx-font-family: 'Apple Gothic Regular';");

                    Label statusLabel = createStatusLabel(jobAnnouncement.isActive());

                    hbox.getChildren().addAll(jobTitleText, statusLabel);
                    hbox.getChildren().add(createButtonsForJobAnnouncement(jobAnnouncement));

                    setGraphic(hbox);
                }
            }
        };
    }

    private Label createStatusLabel(boolean isActive) {
        Label statusLabel = new Label(isActive ? "ACTIVE" : "INACTIVE");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-font-family: 'Apple Gothic Regular'; " +
                (isActive ? "-fx-background-color: #28A745; -fx-text-fill: white;" : "-fx-background-color: #DC3545; -fx-text-fill: white;") +
                "-fx-padding: 5px 10px; -fx-border-radius: 5px;");
        return statusLabel;
    }

    private HBox createButtonsForJobAnnouncement(JobAnnouncementBean jobAnnouncement) {
        HBox hbox = new HBox(10);

        Button manageButton = new Button("Manage");
        manageButton.setOnAction(event -> handleManageJobAnnouncement(jobAnnouncement));

        Button contactButton = new Button("Contact a Job Candidate");
        contactButton.setOnAction(event -> handleContactJobCandidate(jobAnnouncement));

        String buttonStyle = "-fx-background-color: #b3d9ff; -fx-text-fill: #2980b9; " +
                "-fx-border-radius: 5; -fx-border-color: #2980b9; -fx-border-width: 2; " +
                "-fx-cursor: hand; -fx-background-radius: 5;" +
                "-fx-font-size: 11px;";

        manageButton.setStyle(buttonStyle);
        contactButton.setStyle(buttonStyle);

        hbox.getChildren().addAll(manageButton, contactButton);
        return hbox;
    }

    private void handleManageJobAnnouncement(JobAnnouncementBean jobAnnouncement) {
        Printer.print("Managing Job Announcement: " + jobAnnouncement.getJobTitle());
    }

    private void handleContactJobCandidate(JobAnnouncementBean jobAnnouncement) {
        if (jobAnnouncement == null) {
            Printer.print("ERROR: jobAnnouncement is null!");
            return;
        }

        if (context == null) {
            Printer.print("ERROR: context is null in HomeRecruiterController!");
            return;
        }

        Printer.print("Contacting Job Candidate for: " + jobAnnouncement.getJobTitle());
        context.set("jobAnnouncement", jobAnnouncement);
        context.goNext("contactJobCandidate");
    }


    private List<JobAnnouncementBean> fetchJobAnnouncements() {
        return publishBoundary.getJobAnnouncements();
    }

    public void handleViewNotifications() {
        context.goNext("viewNotifications");
    }
}

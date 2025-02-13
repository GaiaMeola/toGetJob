package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.boundary.LoginBoundary;
import org.example.togetjob.view.boundary.PublishAJobAnnouncementRecruiterBoundary;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.concretestate.CreateJobAnnouncementState;
import org.example.togetjob.view.gui.concretestate.FilterJobCandidateState;
import org.example.togetjob.view.gui.concretestate.HomeState;
import org.example.togetjob.view.gui.concretestate.SendAJobApplicationRecruiterState;

import java.util.List;

public class HomeRecruiterController {

    @FXML
    private TitledPane jobAnnouncementField;

    @FXML
    private ListView<JobAnnouncementBean> jobAnnouncementBeanListView;

    private GUIContext context;
    private final LoginBoundary loginBoundary = new LoginBoundary();
    private final PublishAJobAnnouncementRecruiterBoundary publishAJobAnnouncementRecruiterBoundary = new PublishAJobAnnouncementRecruiterBoundary();

    public void setContext(GUIContext context){
        this.context = context;
    }

    @FXML
    private void initialize() {
        List<JobAnnouncementBean> jobAnnouncements = fetchJobAnnouncements();
        populateJobAnnouncements(jobAnnouncements);
    }

    @FXML
    private void handlePublishJobAnnouncementButton(){
        if (context != null) {
            Printer.print("Going to CreateJobAnnouncement...");
            context.setState(new CreateJobAnnouncementState(context));
            context.showMenu();
        } else {
            Printer.print("Context is NOT initialized in HomeRecruiter!");
        }
    }

    @FXML
    private void handleLogout() {
        if (context != null) {
            Printer.print("Context is initialized!");
            loginBoundary.logout();
            context.setState(new HomeState(context));
            context.showMenu();
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
                    HBox hbox = new HBox(10);
                    hbox.setAlignment(Pos.CENTER_LEFT);
                    hbox.setPadding(new Insets(5, 10, 5, 10));

                    Text jobTitleText = new Text(jobAnnouncement.getJobTitle());
                    jobTitleText.setStyle("-fx-font-family: 'Apple Gothic'; -fx-font-weight: bold; -fx-font-size: 14px; -fx-fill: #2980b9;");

                    Text statusLabelText = new Text(" - Status:");
                    statusLabelText.setStyle("-fx-font-family: 'Apple Gothic'; -fx-font-size: 12px; -fx-fill: #2980b9;");

                    Text statusText = new Text(jobAnnouncement.isActive() ? "ACTIVE" : "INACTIVE");
                    statusText.setStyle("-fx-font-family: 'Apple Gothic'; -fx-font-size: 12px; " + getStatusColorStyle(jobAnnouncement.isActive()));

                    hbox.getChildren().addAll(jobTitleText, statusLabelText, statusText);

                    // Create the button panel with the two buttons "Manage" and "Contact a Job Candidate"
                    HBox buttonBox = createButtonsForJobAnnouncement(jobAnnouncement);
                    hbox.getChildren().add(buttonBox);

                    setGraphic(hbox);
                }
            }
        };
    }

    private String getStatusColorStyle(boolean isActive) {
        return isActive ? "-fx-fill: #28A745;" : "-fx-fill: #DC3545;";
    }

    private HBox createButtonsForJobAnnouncement(JobAnnouncementBean jobAnnouncement) {
        HBox hbox = new HBox(10);

        // Button 1: "Manage"
        Button manageButton = new Button("Manage");
        manageButton.setOnAction(event -> handleManageJobAnnouncement(jobAnnouncement));

        // Button 2: "Contact a Job Candidate"
        Button contactButton = new Button("Contact a Job Candidate");
        contactButton.setOnAction(event -> handleContactJobCandidate(jobAnnouncement));

        String buttonStyle = "-fx-background-color: #b3d9ff; " +
                "-fx-text-fill: #2980b9; " +
                "-fx-border-radius: 5; " +
                "-fx-border-color: #2980b9; " +
                "-fx-border-width: 2; " +
                "-fx-cursor: hand; " +
                "-fx-font-weight: bold; " +
                "-fx-background-radius: 5;" +
                "-fx-font-size: 11px;";

        manageButton.setStyle(buttonStyle);
        contactButton.setStyle(buttonStyle);

        hbox.getChildren().addAll(manageButton, contactButton);
        return hbox;
    }

    private void handleManageJobAnnouncement(JobAnnouncementBean jobAnnouncement) {
        Printer.print("Managing Job Announcement: " + jobAnnouncement.getJobTitle());
        // Implement the logic for managing the job announcement (e.g., editing or reviewing it)
    }

    private void handleContactJobCandidate(JobAnnouncementBean jobAnnouncement) {
        Printer.print("Contacting Job Candidate for: " + jobAnnouncement.getJobTitle());
        context.setState(new FilterJobCandidateState(context, jobAnnouncement));
        context.showMenu();
    }

    private List<JobAnnouncementBean> fetchJobAnnouncements() {
        return publishAJobAnnouncementRecruiterBoundary.getJobAnnouncements();
    }

    public void handleViewNotifications() {
        Printer.print("View notifications ...");
        context.setState(new SendAJobApplicationRecruiterState(context));
        context.showMenu();
    }

}

package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.example.togetjob.bean.JobApplicationBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.boundary.LoginBoundary;
import org.example.togetjob.view.boundary.SendAJobApplicationStudentBoundary;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.concretestate.FilterJobAnnouncementStudentState;
import org.example.togetjob.view.gui.concretestate.HomeState;

import java.util.List;


public class HomeStudentController {

    @FXML
    private VBox applicationsVBox;

    @FXML
    private TitledPane pendingApplicationsPane;
    @FXML
    private TitledPane acceptedApplicationsPane;
    @FXML
    private TitledPane rejectedApplicationsPane;


    private GUIContext context;
    private final LoginBoundary loginBoundary = new LoginBoundary();
    private final SendAJobApplicationStudentBoundary sendAJobApplicationStudentBoundary = new SendAJobApplicationStudentBoundary();

    public void setContext(GUIContext context) {
        this.context = context;
    }

    @FXML
    private void handleAcceptedApplications(){

        List<JobApplicationBean> allApplications = sendAJobApplicationStudentBoundary.getJobApplicationsByStudent();
        List<JobApplicationBean> acceptedApplications = allApplications.stream()
                .filter(app -> "ACCEPTED".equalsIgnoreCase(String.valueOf(app.getStatus())))
                .toList();

        Printer.print("Accepted applications: " + acceptedApplications);

        populateApplications(acceptedApplications, acceptedApplicationsPane);

    }


    @FXML
    private void handleRejectedApplications(){

        List<JobApplicationBean> allApplications = sendAJobApplicationStudentBoundary.getJobApplicationsByStudent();
        List<JobApplicationBean> rejectedApplications = allApplications.stream()
                .filter(app -> "REJECTED".equalsIgnoreCase(String.valueOf(app.getStatus())))
                .toList();

        Printer.print("Rejected applications: " + rejectedApplications);

        populateApplications(rejectedApplications, rejectedApplicationsPane);


    }

    @FXML
    private void handlePendingApplications(){

        List<JobApplicationBean> allApplications = sendAJobApplicationStudentBoundary.getJobApplicationsByStudent();
        List<JobApplicationBean> pendingApplications = allApplications.stream()
                .filter(app -> "PENDING".equalsIgnoreCase(String.valueOf(app.getStatus())))
                .toList();

        Printer.print("Pending applications: " + pendingApplications);

        populateApplications(pendingApplications, pendingApplicationsPane);

    }


    @FXML
    private void sendAJobApplication() {
        if (context != null) {
            Printer.print("Context is initialized!");
            context.setState(new FilterJobAnnouncementStudentState(context));
            context.showMenu();
        } else {
            Printer.print("Context is not initialized!");
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


    @FXML
    private void handleViewNotifications(){
        /**/
    }

    private void populateApplications(List<JobApplicationBean> applications, TitledPane pane) {

        AnchorPane content = (AnchorPane) pane.getContent();
        content.getChildren().clear();

        for (JobApplicationBean app : applications) {

            Label applicationLabel = new Label("Job Application: " + app.getJobTitle() + " | Status: " + app.getStatus());


            content.getChildren().add(applicationLabel);


            if ("PENDING".equalsIgnoreCase(app.getStatus())) {
                Button modifyButton = new Button("Modify");
                modifyButton.setOnAction(event -> modifyApplication(app));

                Button deleteButton = new Button("Delete");
                deleteButton.setOnAction(event -> deleteApplication(app));

                content.getChildren().addAll(modifyButton, deleteButton);
            }
        }
    }




}

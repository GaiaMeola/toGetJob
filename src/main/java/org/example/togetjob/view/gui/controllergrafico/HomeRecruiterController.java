package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.boundary.LoginBoundary;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.concretestate.CreateJobAnnouncementState;
import org.example.togetjob.view.gui.concretestate.HomeState;
import org.example.togetjob.view.gui.concretestate.RecruiterDisplayFiltersState;

import java.util.List;

public class HomeRecruiterController {

    @FXML
    private TitledPane titledPaneField;
    @FXML
    private ListView<JobAnnouncementBean> listViewField;



    private GUIContext context;
    private final LoginBoundary loginBoundary = new LoginBoundary();

    public void setContext(GUIContext context){
        this.context = context ;
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

    @FXML
    private void handleContactAJobCandidate(){
        if (context != null) {
            Printer.print("Going to Contact a Job Candidate..");
            context.setState(new RecruiterDisplayFiltersState(context));
            context.showMenu();
        } else {
            Printer.print("Context is NOT initialized in HomeRecruiter!");
        }

    }


}

package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.concretestate.CreateJobAnnouncementState;

public class HomeRecruiterController {

    private GUIContext context;

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

    // DA FINIRE
    @FXML
    private void handleContactAJobCandidate(){

        if (context != null) {
            Printer.print("Going to Contact a Job Candidate..");
        } else {
            Printer.print("Context is NOT initialized in HomeRecruiter!");
        }

    }


}

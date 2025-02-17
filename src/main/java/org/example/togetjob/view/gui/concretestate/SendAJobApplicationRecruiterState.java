package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.state.State;
import org.example.togetjob.view.gui.controllergrafico.SendAJobApplicationRecruiterController;

public class SendAJobApplicationRecruiterState extends BaseState implements State {

    public SendAJobApplicationRecruiterState(GUIContext context) {
        super(context);
    }

    @Override
    protected String getFXMLFile() {
        return "/fxml/recruiterviewjobapplications.fxml";
    }

    @Override
    protected void setUpScene(FXMLLoader fxmlLoader) {
        SendAJobApplicationRecruiterController controller = fxmlLoader.getController();
        controller.setContext(context);
    }

    @Override
    public void showMenu() {
        show();
    }

    @Override
    public void goNext(Context context, String event) {
        GUIContext guiContext = (GUIContext) context;

        switch (event) {
            case "viewJobDetails":
                Printer.print("Navigating to Job Details...");
                // Possibly implement navigation to job details
                break;
            case "go_home":
                Printer.print("Going back to Recruiter Home...");
                guiContext.setState(new HomeRecruiterState(guiContext));
                guiContext.showMenu();
                break;
            default:
                Printer.print("Event not recognized: " + event);
        }
    }

    public GUIContext getContext() {
        return context;
    }
}

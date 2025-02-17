package org.example.togetjob.view.gui.concretestate;

import javafx.fxml.FXMLLoader;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.state.State;
import org.example.togetjob.view.gui.controllergrafico.CreateJobAnnouncementController;
import org.example.togetjob.state.Context;
import org.example.togetjob.printer.Printer;

public class CreateJobAnnouncementState extends BaseState implements State {

    public CreateJobAnnouncementState(GUIContext context) {
        super(context);
    }

    @Override
    protected String getFXMLFile() {
        return "/fxml/recruitercreatejobannouncement.fxml";
    }

    @Override
    protected void setUpScene(FXMLLoader fxmlLoader) {
        CreateJobAnnouncementController controller = fxmlLoader.getController();
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
            case "jobPublished":
                Printer.print("Job published successfully, returning to home...");
                guiContext.setState(new HomeRecruiterState(guiContext));
                guiContext.showMenu();
                break;

            case "homeRecruiter":
                guiContext.setState(new HomeRecruiterState(guiContext));
                guiContext.showMenu();
                break;

            default:
                Printer.print("Event not managed.");
        }
    }
}

package org.example.togetjob.view.gui.concretestate;

import javafx.fxml.FXMLLoader;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.state.State;
import org.example.togetjob.view.gui.controllergrafico.HomeRecruiterController;
import org.example.togetjob.state.Context;
import org.example.togetjob.printer.Printer;

public class HomeRecruiterState extends BaseState implements State {

    public HomeRecruiterState(GUIContext context) {
        super(context);
    }

    @Override
    protected String getFXMLFile() {
        return "/fxml/homerecruiter.fxml";
    }

    @Override
    protected void setUpScene(FXMLLoader fxmlLoader) {
        HomeRecruiterController controller = fxmlLoader.getController();
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
            case "publishJobAnnouncement":
                guiContext.setState(new CreateJobAnnouncementState(guiContext));
                break;
            case "logout":
                guiContext.setState(new HomeState(guiContext));
                break;
            case "contactJobCandidate":
                // not implemented
                break;
            case "viewNotifications":
                guiContext.setState(new SendAJobApplicationRecruiterState(guiContext));
                break;
            default:
                Printer.print("Event not managed.");
        }

        guiContext.showMenu();
    }
}

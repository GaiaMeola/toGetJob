package org.example.togetjob.view.gui.concretestate;

import javafx.fxml.FXMLLoader;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.state.State;
import org.example.togetjob.view.gui.controllergrafico.HomeStudentController;

public class HomeStudentState extends BaseState implements State {

    public HomeStudentState(GUIContext context) {
        super(context);
    }

    @Override
    protected String getFXMLFile() {
        return "/fxml/homestudent.fxml";
    }

    @Override
    protected void setUpScene(FXMLLoader fxmlLoader) {
        HomeStudentController controller = fxmlLoader.getController();
        controller.setContext(context);
    }

    @Override
    public void showMenu() {
        show();
    }

    @Override
    public void goNext(Context contextState, String event) {
        GUIContext guiContext = (GUIContext) contextState;

        switch (event) {
            case "logout":
                guiContext.setState(new HomeState(guiContext));
                guiContext.showMenu();
                break;

            case "filter_jobs":
                guiContext.setState(new FilterJobAnnouncementStudentState(guiContext));
                guiContext.showMenu();
                break;

            default:
                Printer.print("Event not managed: " + event);
                break;
        }
    }
}

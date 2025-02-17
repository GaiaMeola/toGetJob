package org.example.togetjob.view.gui.concretestate;

import javafx.fxml.FXMLLoader;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.state.State;
import org.example.togetjob.view.gui.controllergrafico.HomeController;

public class HomeState extends BaseState implements State {

    public HomeState(GUIContext context) {
        super(context);
    }

    @Override
    protected String getFXMLFile() {
        return "/fxml/home.fxml";
    }

    @Override
    protected void setUpScene(FXMLLoader fxmlLoader) {
        HomeController controller = fxmlLoader.getController();
        controller.initialize(context);
    }

    @Override
    public void showMenu() {
        show();
    }

    @Override
    public void goNext(Context contextState, String event) {
        GUIContext guiContext = (GUIContext) contextState;

        switch (event) {
            case "student_home":
                guiContext.setState(new HomeStudentState(guiContext));
                break;
            case "recruiter_home":
                guiContext.setState(new HomeRecruiterState(guiContext));
                break;
            case "register":
                guiContext.setState(new RegisterUserState(guiContext));
                break;
            default:
                Printer.print("Event not managed: " + event);
                break;
        }

        guiContext.showMenu();
    }
}

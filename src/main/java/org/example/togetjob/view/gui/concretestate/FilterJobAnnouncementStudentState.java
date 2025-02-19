package org.example.togetjob.view.gui.concretestate;

import javafx.fxml.FXMLLoader;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.state.State;
import org.example.togetjob.view.gui.controllergrafico.FilterJobAnnouncementStudentController;

public class FilterJobAnnouncementStudentState extends BaseState implements State {

    public FilterJobAnnouncementStudentState(GUIContext context) {
        super(context);
    }

    @Override
    protected String getFXMLFile() {
        return "/fxml/studentfiltersjobannouncement.fxml";
    }

    @Override
    protected void setUpScene(FXMLLoader fxmlLoader) {
        FilterJobAnnouncementStudentController controller = fxmlLoader.getController();
        controller.setContext(context);
    }

    @Override
    public void showMenu() {
        show();
    }

    @Override
    public void goNext(Context contextState, String event) {
        GUIContext guiContext = (GUIContext) contextState;

        if (event.equals("student_home")) {
            guiContext.setState(new HomeStudentState(guiContext));
        } else {
            Printer.print("Event not handled for this state: " + event);
        }

        guiContext.showMenu();
    }
}

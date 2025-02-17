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
    public void goNext(Context context, String event) {
        Printer.print("Event not handled for this state: " + event);
    }
}

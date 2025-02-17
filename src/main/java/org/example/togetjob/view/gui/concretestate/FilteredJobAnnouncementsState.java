package org.example.togetjob.view.gui.concretestate;

import javafx.fxml.FXMLLoader;
import org.example.togetjob.bean.JobAnnouncementSearchBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.state.State;
import org.example.togetjob.view.gui.controllergrafico.SendAJobApplicationByStudentController;

public class FilteredJobAnnouncementsState extends BaseState implements State {

    private final JobAnnouncementSearchBean jobAnnouncementSearchBean;

    public FilteredJobAnnouncementsState(GUIContext context, JobAnnouncementSearchBean jobAnnouncementSearchBean) {
        super(context);
        this.jobAnnouncementSearchBean = jobAnnouncementSearchBean;
    }

    @Override
    protected String getFXMLFile() {
        return "/fxml/filteredjobannouncements.fxml";
    }

    @Override
    protected void setUpScene(FXMLLoader fxmlLoader) {
        SendAJobApplicationByStudentController controller = fxmlLoader.getController();
        controller.setContext(context);
        controller.setJobAnnouncementSearchBean(jobAnnouncementSearchBean);
    }

    @Override
    public void showMenu() {
        show();
    }

    @Override
    public void goNext(Context context, String event) {
        GUIContext guiContext = (GUIContext) context;

        if ("go_home".equals(event)) {
            Printer.print("Returning to Student Home...");
            guiContext.setState(new HomeStudentState(guiContext));
        } else {
            Printer.print("Warning: Unrecognized event -> " + event);
        }

        guiContext.showMenu();
    }
}

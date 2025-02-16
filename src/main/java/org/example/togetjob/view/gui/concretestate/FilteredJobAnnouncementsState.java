package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.togetjob.bean.JobAnnouncementSearchBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.State;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.view.gui.controllergrafico.SendAJobApplicationByStudentController;

import java.io.IOException;

public class FilteredJobAnnouncementsState implements State {

    private final GUIContext context;
    private final JobAnnouncementSearchBean jobAnnouncementSearchBean;

    public FilteredJobAnnouncementsState(GUIContext context, JobAnnouncementSearchBean jobAnnouncementSearchBean) {
        this.context = context;
        this.jobAnnouncementSearchBean = jobAnnouncementSearchBean;
    }

    @Override
    public void showMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/filteredjobannouncements.fxml"));

            VBox root = fxmlLoader.load();
            Scene scene = new Scene(root, 600, 500);

            SendAJobApplicationByStudentController controller = fxmlLoader.getController();
            controller.setContext(context);
            controller.setJobAnnouncementSearchBean(jobAnnouncementSearchBean);

            Stage stage = context.getStage();
            if (stage == null) {
                stage = new Stage();
                context.setStage(stage);
            }

            stage.setTitle("Filtered Job Announcements by Student");
            stage.setScene(scene);

            stage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });

            stage.show();

        } catch (IOException e) {
            Printer.print("Error loading Filtered Job Announcement View: " + e.getMessage());
        }
    }

    @Override
    public void goNext(Context context, String event) {
        GUIContext guiContext = (GUIContext) context;

        switch (event) {
            case "viewJobDetails":
                Printer.print("Navigating to Job Details...");
                break;
            case "goBack":
                Printer.print("Going back to Student Home...");
                guiContext.setState(new HomeStudentState(guiContext));
                break;
            default:
                Printer.print("Event not recognized: " + event);
        }

        guiContext.showMenu();
    }

    public GUIContext getContext() {
        return context;
    }
}

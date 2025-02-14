package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.Context;
import org.example.togetjob.view.State;
import org.example.togetjob.view.GUIContext;
import org.example.togetjob.view.gui.controllergrafico.HomeRecruiterController;

import java.io.IOException;

public class HomeRecruiterState implements State {

    private final GUIContext context;

    public HomeRecruiterState(GUIContext context) {
        this.context = context;
    }

    @Override
    public void showMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/togetjob/fxml/homerecruiter.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);

            HomeRecruiterController controller = fxmlLoader.getController();
            controller.setContext(context);

            Stage stage = context.getStage();
            if (stage == null) {
                stage = new Stage();
                context.setStage(stage);
            }

            stage.setTitle("Home Recruiter");
            stage.setScene(scene);

            stage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
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
                JobAnnouncementBean jobAnnouncement = (JobAnnouncementBean) guiContext.get("jobAnnouncement");
                if (jobAnnouncement != null) {
                    guiContext.setState(new FilterJobCandidateState(guiContext, jobAnnouncement));
                } else {
                    Printer.print("Error: No JobAnnouncement found in context!");
                }
                break;
            case "viewNotifications":
                guiContext.setState(new SendAJobApplicationRecruiterState(guiContext));
                break;
            default:
                Printer.print("Event not managed.");
        }

        guiContext.showMenu();
    }

    public GUIContext getContext() {
        return context;
    }
}

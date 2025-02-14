package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.StudentInfoSearchBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.Context;
import org.example.togetjob.view.State;
import org.example.togetjob.view.GUIContext;
import org.example.togetjob.view.gui.controllergrafico.FilterJobCandidateRecruiterController;

import java.io.IOException;

public class FilterJobCandidateState implements State {

    private final GUIContext context;

    public FilterJobCandidateState(GUIContext context, JobAnnouncementBean jobAnnouncementBean) {
        this.context = context;
        this.context.set("jobAnnouncement", jobAnnouncementBean); // Memorizza la bean nel context
    }

    @Override
    public void showMenu() {
        Printer.print("Showing Filter Job Candidate by Recruiter...");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/togetjob/fxml/filterjobcandidatesrecruiter.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);

            FilterJobCandidateRecruiterController controller = fxmlLoader.getController();
            controller.setContext(context);

            Stage stage = context.getStage();
            if (stage == null) {
                stage = new Stage();
                context.setStage(stage);
            }

            stage.setTitle("Filter Job Announcements");
            stage.setScene(scene);

            stage.setOnCloseRequest(event -> {
                Platform.exit();
                System.exit(0);
            });

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Printer.print("Error loading FXML: " + e.getMessage());
        }
    }

    @Override
    public void goNext(Context context, String event) {
        GUIContext guiContext = (GUIContext) context;

        switch (event) {
            case "contactJobCandidate":
                StudentInfoSearchBean studentInfoSearchBean = (StudentInfoSearchBean) guiContext.get("studentInfoSearch");
                JobAnnouncementBean jobAnnouncementBean = (JobAnnouncementBean) guiContext.get("jobAnnouncement");

                guiContext.setState(new ContactAJobCandidateState(guiContext, studentInfoSearchBean, jobAnnouncementBean));
                break;

            case "homeRecruiter":
                guiContext.setState(new HomeRecruiterState(guiContext));
                break;

            default:
                Printer.print("Event not managed.");
        }
    }

    public GUIContext getContext() {
        return context;
    }
}

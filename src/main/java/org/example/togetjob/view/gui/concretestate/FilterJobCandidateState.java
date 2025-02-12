package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.GUIState;
import org.example.togetjob.view.gui.controllergrafico.FilterJobCandidateRecruiterController;

import java.io.IOException;

public class FilterJobCandidateState implements GUIState {

    GUIContext context;
    JobAnnouncementBean jobAnnouncementBean;

    public FilterJobCandidateState(GUIContext context, JobAnnouncementBean jobAnnouncement) {
        this.context = context;
        this.jobAnnouncementBean = jobAnnouncement;
    }

    @Override
    public void showMenu() {

        Printer.print("Showing Filter Job Candidate by Recruiter...");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/togetjob/fxml/filterjobcandidatesrecruiter.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);

            FilterJobCandidateRecruiterController filterJobCandidateRecruiterController = fxmlLoader.getController();
            filterJobCandidateRecruiterController.setContext(context);
            filterJobCandidateRecruiterController.setJobAnnouncement(jobAnnouncementBean);

            Stage stage = context.getStage();

            if (stage == null) {
                stage = new Stage();
                context.setStage(stage);
            }

            stage.setTitle("Filter Job Announcements");
            stage.setScene(scene);

            stage.setOnCloseRequest(event -> {
                Platform.exit();  // JavaFX
                System.exit(0);
            });

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Printer.print("Error loading FXML: " + e.getMessage());
        }

    }

    @Override
    public GUIContext getContext() {
        return this.context;
    }
}

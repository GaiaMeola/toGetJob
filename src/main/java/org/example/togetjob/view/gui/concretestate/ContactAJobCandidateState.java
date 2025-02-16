package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.StudentInfoSearchBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.State;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.view.gui.controllergrafico.ContactAJobCandidateController;

import java.io.IOException;

public class ContactAJobCandidateState implements State {

    GUIContext context;

    public ContactAJobCandidateState(GUIContext context) {
        this.context = context;
    }

    @Override
    public void showMenu() {

        StudentInfoSearchBean studentInfoSearchBean = (StudentInfoSearchBean) context.get("studentInfoSearch");
        JobAnnouncementBean jobAnnouncementBean = (JobAnnouncementBean) context.get("jobAnnouncement");

        if (studentInfoSearchBean == null || jobAnnouncementBean == null) {
            Printer.print("ERROR: Missing data in context!");
            return;
        }

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/filteredjobcandidates.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            ContactAJobCandidateController controller = fxmlLoader.getController();
            controller.setContext(context);
            controller.setStudentInfoSearchBean(studentInfoSearchBean);
            controller.setJobAnnouncementBean(jobAnnouncementBean);

            Stage stage = context.getStage();
            if (stage == null) {
                stage = createNewStage();
            }

            stage.setTitle("Contact a Job Candidate");
            stage.setScene(scene);
            setCloseRequestListener(stage);
            stage.show();
        } catch (IOException e) {
            Printer.print("Error loading Contact a Job Candidate View: " + e.getMessage());
        }
    }

    private Stage createNewStage() {
        Stage stage = new Stage();
        context.setStage(stage);
        return stage;
    }

    private void setCloseRequestListener(Stage stage) {
        stage.setOnCloseRequest(e -> {
            Platform.exit();  // Exits JavaFX
            System.exit(0);   // Terminates the process
        });
    }

    @Override
    public void goNext(Context context, String event) {
        //**//
    }

    public GUIContext getContext() {
        return context;
    }
}

package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.StudentInfoSearchBean;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.GUIState;
import org.example.togetjob.view.gui.controllergrafico.ContactAJobCandidateController;

import java.io.IOException;

public class ContactAJobCandidateState implements GUIState {

    GUIContext context;
    StudentInfoSearchBean studentInfoSearchBean;
    JobAnnouncementBean jobAnnouncementBean;

    public ContactAJobCandidateState(GUIContext context, StudentInfoSearchBean studentInfoSearchBean, JobAnnouncementBean jobAnnouncementBean) {
        this.context = context;
        this.studentInfoSearchBean = studentInfoSearchBean;
        this.jobAnnouncementBean = jobAnnouncementBean;
    }

    @Override
    public void showMenu() {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/togetjob/fxml/filteredjobcandidates.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);

            ContactAJobCandidateController controller = fxmlLoader.getController();
            controller.setContext(context);
            controller.setStudentInfoSearchBean(studentInfoSearchBean);
            controller.setJobAnnouncementBean(jobAnnouncementBean);

            Stage stage = context.getStage();

            if(stage == null){
                stage = new Stage();
                context.setStage(stage);
            }

            stage.setTitle("Contact a Job Candidate");
            stage.setScene(scene);

            //listener to close the application
            stage.setOnCloseRequest(event -> {
                Platform.exit();  // JavaFX
                System.exit(0);   // process
            });

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public GUIContext getContext() {
        return this.context;
    }
}

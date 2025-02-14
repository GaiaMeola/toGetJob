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
    private final GUIContext context;
    private final StudentInfoSearchBean studentInfoSearchBean;
    private final JobAnnouncementBean jobAnnouncementBean;

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
            if (stage == null) {
                stage = createNewStage();
            }

            stage.setTitle("Contact a Job Candidate");
            stage.setScene(scene);
            setCloseRequestListener(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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
    public GUIContext getContext() {
        return this.context;
    }
}

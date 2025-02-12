package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.bean.JobAnnouncementSearchBean;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.GUIState;
import org.example.togetjob.view.gui.controllergrafico.FilteredJobAnnouncementsController;

import java.io.IOException;

public class FilteredJobAnnouncementsState implements GUIState {

    private final GUIContext context;
    private final JobAnnouncementSearchBean jobAnnouncementSearchBean;

    public FilteredJobAnnouncementsState(GUIContext context, JobAnnouncementSearchBean jobAnnouncementSearchBean) {
        this.context = context;
        this.jobAnnouncementSearchBean = jobAnnouncementSearchBean;
    }

    @Override
    public void showMenu() {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/togetjob/fxml/filteredjobannouncements.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);

            FilteredJobAnnouncementsController controller = fxmlLoader.getController();
            controller.setContext(context);
            controller.setJobAnnouncementSearchBean(jobAnnouncementSearchBean);

            Stage stage = context.getStage();

            if(stage == null){
                stage = new Stage();
                context.setStage(stage);
            }

            stage.setTitle("Home Student");
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

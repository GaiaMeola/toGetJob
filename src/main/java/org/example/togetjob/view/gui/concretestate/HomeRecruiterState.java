package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.GUIState;
import org.example.togetjob.view.gui.controllergrafico.HomeRecruiterController;

import java.io.IOException;

public class HomeRecruiterState implements GUIState {

    GUIContext context;

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

            if(stage == null){
                stage = new Stage();
                context.setStage(stage);
            }

            stage.setTitle("Home Recruiter");
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

package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.GUIState;

import java.io.IOException;

public class HomeStudentState implements GUIState {

    GUIContext context;

    public HomeStudentState(GUIContext context) {
        this.context = context;
    }

    @Override
    public void showMenu() {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/togetjob/fxml/homestudent.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 492.0, 427.0);


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

package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.State;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.view.gui.controllergrafico.SendAJobApplicationRecruiterController;

import java.io.IOException;

public class SendAJobApplicationRecruiterState implements State {

    GUIContext context;

    public SendAJobApplicationRecruiterState(GUIContext context) {
        this.context = context;
    }

    @Override
    public void showMenu() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/togetjob/fxml/recruiterviewjobapplications.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);


            SendAJobApplicationRecruiterController controller = fxmlLoader.getController();
            controller.setContext(context);


            Stage stage = context.getStage();

            if (stage == null) {
                stage = new Stage();
                context.setStage(stage);
            }

            stage.setTitle("Filtered Job Announcements by Student");
            stage.setScene(scene);


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
    public void goNext(Context context, String event) {
        //**//
    }
    public GUIContext getContext() {
        return context;
    }
}

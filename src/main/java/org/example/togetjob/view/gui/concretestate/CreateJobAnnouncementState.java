package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.GUIState;
import org.example.togetjob.view.gui.controllergrafico.CreateJobAnnouncementController;

import java.io.IOException;

public class CreateJobAnnouncementState implements GUIState {
    private final GUIContext context;

    public CreateJobAnnouncementState(GUIContext context) {
        this.context = context;
    }

    @Override
    public void showMenu() {
        try {
            Printer.print("Showing CreateJobAnnouncementState...");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/togetjob/fxml/recruitercreatejobannouncement.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);

            CreateJobAnnouncementController controller = fxmlLoader.getController();
            controller.setContext(context);

            Stage stage = getStage();
            stage.setTitle("Create Job Announcement");
            stage.setScene(scene);
            setCloseRequestListener(stage);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GUIContext getContext() {
        return this.context;
    }

    private Stage getStage() {
        Stage stage = context.getStage();
        if (stage == null) {
            stage = new Stage();
            context.setStage(stage);
        }
        return stage;
    }

    private void setCloseRequestListener(Stage stage) {
        stage.setOnCloseRequest(e -> {
            Platform.exit();  // Exits JavaFX
            System.exit(0);   // Terminates the process
        });
    }
}

package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.State;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.view.gui.controllergrafico.CreateJobAnnouncementController;

import java.io.IOException;

public class CreateJobAnnouncementState implements State {
    private final GUIContext context;

    public CreateJobAnnouncementState(GUIContext context) {
        this.context = context;
    }

    @Override
    public void showMenu() {
        try {
            Printer.print("Showing Create Job Announcement...");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/recruitercreatejobannouncement.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);

            CreateJobAnnouncementController controller = fxmlLoader.getController();
            controller.setContext(context);

            Stage stage = getStage();
            stage.setTitle("Create Job Announcement");
            stage.setScene(scene);
            setCloseRequestListener(stage);
            stage.show();
        } catch (IOException e) {
            Printer.print("Error loading Create Job Announcement View: " + e.getMessage());
        }
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
            Platform.exit();
            System.exit(0);
        });
    }

    @Override
    public void goNext(Context context, String event) {
        GUIContext guiContext = (GUIContext) context;

        switch (event) {
            case "jobPublished":
                Printer.print("Job published successfully, returning to home...");
                guiContext.setState(new HomeRecruiterState(guiContext));
                break;

            case "homeRecruiter":
                guiContext.setState(new HomeRecruiterState(guiContext));
                break;

            default:
                Printer.print("Event not managed.");
        }
    }

    public GUIContext getContext() {
        return this.context;
    }
}

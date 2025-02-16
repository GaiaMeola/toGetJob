package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.State;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.view.gui.controllergrafico.FilterJobCandidateRecruiterController;

import java.io.IOException;

public class FilterJobCandidateState implements State {

    private final GUIContext context;

    public FilterJobCandidateState(GUIContext context) {
        this.context = context;
    }

    @Override
    public void showMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/filterjobcandidatesrecruiter.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);

            FilterJobCandidateRecruiterController controller = fxmlLoader.getController();
            controller.setContext(context);

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
            Printer.print("Error loading Filter Job Candidate View: " + e.getMessage());
        }
    }

    @Override
    public void goNext(Context context, String event) {
        GUIContext guiContext = (GUIContext) context;

        switch (event) {
            case "filterJobCandidates":
                guiContext.setState(new ContactAJobCandidateState(guiContext));
                break;

            case "homeRecruiter":
                guiContext.setState(new HomeRecruiterState(guiContext));
                break;

            default:
                Printer.print("Event not managed.");
        }
    }

    public GUIContext getContext() {
        return context;
    }
}

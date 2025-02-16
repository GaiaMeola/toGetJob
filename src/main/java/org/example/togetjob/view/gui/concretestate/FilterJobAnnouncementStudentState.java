package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.State;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.view.gui.controllergrafico.FilterJobAnnouncementStudentController;

import java.io.IOException;

public class FilterJobAnnouncementStudentState implements State {

    private final GUIContext context;

    public FilterJobAnnouncementStudentState(GUIContext context) {
        this.context = context;
    }

    @Override
    public void showMenu() {
        Printer.print("Showing Filter Job Announcement by Student...");

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/togetjob/fxml/studentfiltersjobannouncement.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);

            FilterJobAnnouncementStudentController filterJobAnnouncementStudentController = fxmlLoader.getController();
            filterJobAnnouncementStudentController.setContext(context);

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
            e.printStackTrace();
            Printer.print("Error loading FXML: " + e.getMessage());
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

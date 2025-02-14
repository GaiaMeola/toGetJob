package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.Context;
import org.example.togetjob.view.State;
import org.example.togetjob.view.GUIContext;
import org.example.togetjob.view.gui.controllergrafico.CreateJobAnnouncementController;

import java.io.IOException;

public class RecruiterDisplayFiltersState implements State {

    GUIContext context;

    public RecruiterDisplayFiltersState(GUIContext context ){

        this.context = context ;

    }

    @Override
    public void showMenu() {

        try {
            Printer.print("Showing RecruiterDisplayFiltersState...");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/togetjob/fxml/filterjobcandidatesrecruiter.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);

            CreateJobAnnouncementController controller = fxmlLoader.getController();
            controller.setContext(context);

            Stage stage = context.getStage();

            if (stage == null) {
                stage = new Stage();
                context.setStage(stage);
            }

            stage.setTitle("Display Filters");
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

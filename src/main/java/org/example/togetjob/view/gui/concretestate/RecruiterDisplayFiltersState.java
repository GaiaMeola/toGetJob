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

public class RecruiterDisplayFiltersState implements GUIState {

    GUIContext context;

    public RecruiterDisplayFiltersState(GUIContext context ){

        this.context = context ;

    }

    @Override
    public void showMenu() {

        try {
            Printer.print("Showing RecruiterDisplayFiltersState...");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/togetjob/fxml/RecruiterDisplayFilters.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 492.0, 427.0);

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
    public GUIContext getContext() {
        return this.context;
    }

}

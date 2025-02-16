package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.State;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.view.gui.controllergrafico.HomeStudentController;

import java.io.IOException;

public class HomeStudentState implements State {

    private final GUIContext context;

    public HomeStudentState(GUIContext context) {
        this.context = context;
    }

    @Override
    public void showMenu() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/homestudent.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);

            HomeStudentController controller = fxmlLoader.getController();
            controller.setContext(context);

            Stage stage = context.getStage();

            if (stage == null) {
                stage = new Stage();
                context.setStage(stage);
            }

            stage.setTitle("Home Student");
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
    public void goNext(Context contextState, String event) {
        // Cast GUIContext
        GUIContext contextGUI = (GUIContext) contextState;

        switch (event) {
            case "logout":
                contextGUI.setState(new HomeState(contextGUI));
                contextGUI.showMenu();
                break;

            case "filter_jobs":

                contextGUI.setState(new FilterJobAnnouncementStudentState(contextGUI));
                contextGUI.showMenu();
                break;

            default:

                Printer.print("Event not managed: " + event);
                break;
        }
    }

    public GUIContext getContext() {
        return context;
    }
}

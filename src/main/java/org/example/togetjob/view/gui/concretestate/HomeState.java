package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.Context;
import org.example.togetjob.view.State;
import org.example.togetjob.view.GUIContext;
import org.example.togetjob.view.gui.controllergrafico.HomeController;


import java.io.IOException;

public class HomeState implements State {

    private final GUIContext context;

    public HomeState(GUIContext context) {
        this.context = context;
    }

    @Override
    public void showMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/togetjob/fxml/home.fxml"));
            Parent root = loader.load();

            HomeController controller = loader.getController();
            controller.initialize(context);

            Scene scene = new Scene(root);
            Stage stage = context.getStage();

            if (stage == null) {
                stage = new Stage();
                context.setStage(stage);
            }

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

        GUIContext contextGUI = (GUIContext) contextState;

        switch (event) {
            case "student_home":
                contextGUI.setState(new HomeStudentState(contextGUI));
                break;
            case "recruiter_home":
                contextGUI.setState(new HomeRecruiterState(contextGUI));
                break;
            case "register":
                contextGUI.setState(new RegisterUserState(contextGUI));
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

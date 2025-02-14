package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.Context;
import org.example.togetjob.view.State;
import org.example.togetjob.view.GUIContext;
import org.example.togetjob.view.gui.controllergrafico.RegisterRecruiterController;

import java.io.IOException;

public class RegisterRecruiterState implements State {

    RegisterUserBean registerUserBean;
    GUIContext context;

    public RegisterRecruiterState(RegisterUserBean registerUserBean, GUIContext context) {
        this.registerUserBean = registerUserBean;
        this.context = context;
    }

    @Override
    public void showMenu() {
        try {
            Printer.print("Showing RegisterRecruiterState...");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/togetjob/fxml/registerrecruiter.fxml"));
            Parent root = fxmlLoader.load();

            RegisterRecruiterController registerRecruiterController = fxmlLoader.getController();
            registerRecruiterController.setContext(context);
            registerRecruiterController.setUserBean(registerUserBean);

            Scene scene = new Scene(root);
            Stage stage = context.getStage();

            if (stage == null) {
                stage = new Stage();
                context.setStage(stage);
            }

            stage.setTitle("Register Recruiter");
            stage.setScene(scene);

            // Listener to close the application
            stage.setOnCloseRequest(event -> {
                Platform.exit();  // JavaFX exit
                System.exit(0);   // process exit
            });

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void goNext(Context context, String event) {

        GUIContext contextGUI = (GUIContext) context;

        switch (event) {
            case "go_home_recruiter":

                contextGUI.setState(new HomeRecruiterState(contextGUI));
                break;
            case "go_home":

                contextGUI.setState(new HomeState(contextGUI));
                break;
            case "register_recruiter":

                contextGUI.setState(new RegisterRecruiterState(registerUserBean, contextGUI));
                break;
            default:
                Printer.print("Event not handled: " + event);
                break;
        }
    }

    public GUIContext getContext() {
        return context;
    }
}

package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.boundary.RegisterBoundary;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.GUIState;
import org.example.togetjob.view.gui.controllergrafico.RegisterStudentController;

import java.io.IOException;

public class RegisterStudentState implements GUIState {

    RegisterUserBean userBean;
    GUIContext context;

    public RegisterStudentState(RegisterUserBean userBean, GUIContext context) {
        this.userBean = userBean;
        this.context = context;
    }

    @Override
    public void showMenu() {
        try {
            Printer.print("Showing RegisterStudentState...");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/togetjob/fxml/registerstudent.fxml"));
            Parent root = fxmlLoader.load();

            RegisterStudentController registerStudentController = fxmlLoader.getController();
            registerStudentController.setContext(context);
            registerStudentController.setUserBean(userBean);

            Scene scene = new Scene(root);
            Stage stage = context.getStage();

            if(stage == null){
                stage = new Stage();
                context.setStage(stage);

            }

            stage.setTitle("Register Student");
            stage.setScene(scene);

            //listener to close the application
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
        return context;
    }
}

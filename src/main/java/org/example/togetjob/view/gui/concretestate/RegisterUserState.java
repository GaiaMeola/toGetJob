package org.example.togetjob.view.gui.concretestate;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.GUIState;
import org.example.togetjob.view.gui.controllergrafico.RegisterController;

import java.io.IOException;

public class RegisterUserState implements GUIState {

    private final GUIContext context;

    public RegisterUserState(GUIContext context) {
        this.context = context;
    }

    @Override
    public void showMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/togetjob/fxml/registeruser.fxml"));
            Parent root = loader.load();

            RegisterController controller = loader.getController();
            controller.setContext(context);


            Stage stage = context.getStage();
            if (stage == null) {
                throw new IllegalStateException("Stage is null! Ensure GUIContext is properly initialized.");
            }


            stage.setScene(new Scene(root));
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

package org.example.togetjob.view.gui.concretestate;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.GUIContext;

import java.io.IOException;

public abstract class BaseState {

    protected final GUIContext context;

    protected BaseState(GUIContext context) {
        this.context = context;
    }

    public void show() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(getFXMLFile()));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            setUpScene(fxmlLoader);
            configureStage(scene);
        } catch (IOException e) {
            Printer.print("Error loading FXML: " + e.getMessage());
        }
    }

    protected abstract String getFXMLFile();


    protected abstract void setUpScene(FXMLLoader fxmlLoader);

    private void configureStage(Scene scene) {
        Stage stage = context.getStage();
        if (stage == null) {
            stage = new Stage();
            context.setStage(stage);
        }
        stage.setScene(scene);
        stage.show();
    }
}


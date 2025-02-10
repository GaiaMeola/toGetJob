package org.example.togetjob.view.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.togetjob.view.gui.concretestate.HomeState;

public class JavaFXApplication extends Application {

    public void start(Stage primaryStage) {
        GUIContext guiContext = new GUIContext(primaryStage);
        HomeState homeState = new HomeState(guiContext);
        guiContext.setState(homeState); //initial State
        guiContext.showMenu();  // (HomeState)
    }

    public static void main(String[] args) {
        launch();
    }
}

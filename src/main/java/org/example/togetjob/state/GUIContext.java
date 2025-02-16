package org.example.togetjob.state;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.togetjob.view.gui.concretestate.HomeState;

import java.util.HashMap;
import java.util.Map;

public class GUIContext extends Application implements Context {

    private State currentState;
    private Stage currentStage;

    private static final Map<String, Object> contextData = new HashMap<>();

    @Override
    public void start(Stage primaryStage) {
        this.currentStage = primaryStage;
        initialize();
        primaryStage.setTitle("GUI Application");
        primaryStage.show();
    }

    @Override
    public void initialize() {
        this.currentState = new HomeState(this);
        showMenu();
    }

    @Override
    public void goNext(String event) {
        if (currentState != null) {
            currentState.goNext(this, event);
        }
    }

    public void showMenu() {
        if (currentState != null) {
            currentState.showMenu();
        }
    }

    public void setStage(Stage stage) {
        this.currentStage = stage;
    }

    public Stage getStage() {
        return currentStage;
    }

    @Override
    public void setState(State state) {
        this.currentState = state;
        showMenu();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void set(String key, Object value) {
        contextData.put(key, value);
    }

    public Object get(String key) {
        return contextData.get(key);
    }

}

package org.example.togetjob.view.gui;

import javafx.stage.Stage;

public class GUIContext {

    private GUIState currentState;
    private Stage currentStage;

    public GUIContext(Stage currentStage) {
        this.currentStage = currentStage;
    }

    public void setState(GUIState state) {
        this.currentState = state;
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
}

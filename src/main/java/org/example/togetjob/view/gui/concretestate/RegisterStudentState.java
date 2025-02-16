package org.example.togetjob.view.gui.concretestate;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.State;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.view.gui.controllergrafico.RegisterStudentController;

import java.io.IOException;

public class RegisterStudentState implements State {

    private final RegisterUserBean userBean;
    private final GUIContext context;

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

            if (stage == null) {
                stage = new Stage();
                context.setStage(stage);
            }

            stage.setTitle("Register Student");
            stage.setScene(scene);

            // listener to close the application
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
    public void goNext(Context context, String event) {

        GUIContext guiContext = (GUIContext) context;

        switch (event) {
            case "register_student_complete":
                Printer.print("Student registration complete, showing HomeStudentState.");
                guiContext.setState(new HomeStudentState(guiContext));
                guiContext.showMenu();
                break;

            case "go_home":
                Printer.print("Going back to HomeState.");
                guiContext.setState(new HomeState(guiContext));
                guiContext.showMenu();
                break;

            default:
                Printer.print("Unknown event: " + event);
                break;
        }
    }

    public GUIContext getContext() {
        return context;
    }
}

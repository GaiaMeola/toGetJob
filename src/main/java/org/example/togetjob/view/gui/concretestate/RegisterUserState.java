package org.example.togetjob.view.gui.concretestate;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.State;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.view.gui.controllergrafico.RegisterController;

import java.io.IOException;

public class RegisterUserState implements State {

    private final GUIContext context;

    public RegisterUserState(GUIContext context) {
        this.context = context;
    }

    @Override
    public void showMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/registeruser.fxml"));
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
            Printer.print("Error loading FXML for Register User View: " + e.getMessage());
        }
    }

    @Override
    public void goNext(Context context, String event) {
        // Cast a GUIContext
        GUIContext guiContext = (GUIContext) context;
        RegisterUserBean user = (RegisterUserBean) guiContext.get("user");

        switch (event) {
            case "register_student":
                guiContext.setState(new RegisterStudentState(user, guiContext));
                break;
            case "register_recruiter":
                guiContext.setState(new RegisterRecruiterState(user, guiContext));
                break;
            case "go_home":
                guiContext.setState(new HomeState(guiContext));
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

package org.example.togetjob.view.gui.concretestate;

import javafx.fxml.FXMLLoader;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.state.State;
import org.example.togetjob.view.gui.controllergrafico.RegisterController;

public class RegisterUserState extends BaseState implements State {

    public RegisterUserState(GUIContext context) {
        super(context);
    }

    @Override
    protected String getFXMLFile() {
        return "/fxml/registeruser.fxml";
    }

    @Override
    protected void setUpScene(FXMLLoader fxmlLoader) {
        RegisterController controller = fxmlLoader.getController();
        controller.setContext(context);
    }

    @Override
    public void showMenu() {
        show();
    }

    @Override
    public void goNext(Context context, String event) {
        GUIContext guiContext = (GUIContext) context;

        switch (event) {
            case "go_home" -> guiContext.setState(new HomeState(guiContext));
            case null, default -> Printer.print("Warning: Unrecognized event -> " + event);
        }

        guiContext.showMenu();
    }
}

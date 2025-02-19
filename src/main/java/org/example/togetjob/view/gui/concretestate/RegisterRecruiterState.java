package org.example.togetjob.view.gui.concretestate;

import javafx.fxml.FXMLLoader;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.state.State;
import org.example.togetjob.view.gui.controllergrafico.RegisterRecruiterController;

public class RegisterRecruiterState extends BaseState implements State {

    private final RegisterUserBean userBean;

    public RegisterRecruiterState(RegisterUserBean userBean, GUIContext context) {
        super(context);
        this.userBean = userBean;
    }

    @Override
    protected String getFXMLFile() {
        return "/fxml/registerrecruiter.fxml";
    }

    @Override
    protected void setUpScene(FXMLLoader fxmlLoader) {
        RegisterRecruiterController controller = fxmlLoader.getController();
        controller.setContext(context);
        controller.setUserBean(userBean);
    }

    @Override
    public void showMenu() {
        show();
    }

    @Override
    public void goNext(Context contextState, String event) {
        GUIContext guiContext = (GUIContext) contextState;

        switch (event) {
            case "go_home_recruiter":
                guiContext.setState(new HomeRecruiterState(guiContext));
                guiContext.showMenu();
                break;
            case "go_home":
                guiContext.setState(new HomeState(guiContext));
                guiContext.showMenu();
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

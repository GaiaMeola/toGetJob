package org.example.togetjob.view.gui.concretestate;

import javafx.fxml.FXMLLoader;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.state.GUIContext;
import org.example.togetjob.state.State;
import org.example.togetjob.view.gui.controllergrafico.RegisterStudentController;
import org.example.togetjob.state.Context;
import org.example.togetjob.printer.Printer;

public class RegisterStudentState extends BaseState implements State {

    private final RegisterUserBean userBean;

    public RegisterStudentState(RegisterUserBean userBean, GUIContext context) {
        super(context);
        this.userBean = userBean;
    }

    @Override
    protected String getFXMLFile() {
        return "/fxml/registerstudent.fxml";
    }

    @Override
    protected void setUpScene(FXMLLoader fxmlLoader) {
        RegisterStudentController controller = fxmlLoader.getController();
        controller.setContext(context);
        controller.setUserBean(userBean);
    }

    @Override
    public void showMenu() {
        show();
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

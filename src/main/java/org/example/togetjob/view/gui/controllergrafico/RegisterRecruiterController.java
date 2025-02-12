package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.example.togetjob.bean.RecruiterInfoBean;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.boundary.RegisterBoundary;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.concretestate.HomeRecruiterState;
import org.example.togetjob.view.gui.concretestate.HomeState;

import java.util.List;

public class RegisterRecruiterController {

    @FXML private TextField companiesField;

    private GUIContext context;
    private RegisterUserBean userBean;
    private final RegisterBoundary registerBoundary = new RegisterBoundary();

    public void setContext(GUIContext context) {
        this.context = context;
    }

    public void setUserBean(RegisterUserBean userBean) {
        this.userBean = userBean;
    }

    @FXML
    private void handleContinueButton() {

        List<String> companies = List.of(companiesField.getText().split(","));

        RecruiterInfoBean recruiterInfoBean = new RecruiterInfoBean();
        recruiterInfoBean.setCompanies(companies);

        if (companies.isEmpty()) {
            Printer.print("Companies field cannot be empty!");
            return;
        }

        boolean registrationSuccess = registerBoundary.registerUser(userBean, recruiterInfoBean);

        if (registrationSuccess) {
            Printer.print("Recruiter successfully registered: " + recruiterInfoBean);
            context.setState(new HomeRecruiterState(context));
            context.showMenu();
        } else {
            Printer.print("Registration failed!");
        }
    }

    @FXML
    private void handleBackButton() {
        context.setState(new HomeState(context));
        context.showMenu();
    }

}


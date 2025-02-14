package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.togetjob.bean.RecruiterInfoBean;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.boundary.RegisterBoundary;
import org.example.togetjob.view.GUIContext;

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
            context.set("recruiterInfo", recruiterInfoBean);
            context.goNext("go_home_recruiter");
        } else {
            Printer.print("Registration failed!");
        }
    }

    @FXML
    private void handleBackButton() {
        if (context != null) {
            Printer.print("Going back to Home...");
            context.goNext("go_home");
        } else {
            Printer.print("Context is NOT initialized in RegisterRecruiterController!");
        }
    }
}

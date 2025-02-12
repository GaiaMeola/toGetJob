package org.example.togetjob.view.gui.controllergrafico;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.togetjob.bean.JobAnnouncementSearchBean;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.gui.GUIContext;
import org.example.togetjob.view.gui.concretestate.HomeStudentState;

public class FilterJobAnnouncementStudentController {

    @FXML
    private TextField jobTitleField ;
    @FXML
    private TextField jobTypeField ;
    @FXML
    private TextField roleField ;
    @FXML
    private TextField locationField ;
    @FXML
    private TextField workingHoursField ;
    @FXML
    private TextField companyNameField ;
    @FXML
    private TextField salaryField ;

    GUIContext context;

    public void setContext(GUIContext context){
        this.context = context;
    }

    @FXML
    private void handleProceedButton(){

        String jobTitle = jobTitleField.getText();
        String jobType = jobTypeField.getText();
        String role = roleField.getText();
        String location = locationField.getText();
        String workingHours = workingHoursField.getText();
        String companyName = companyNameField.getText();
        String salary = salaryField.getText();

        JobAnnouncementSearchBean jobAnnouncementSearchBean = new JobAnnouncementSearchBean() ;

        jobAnnouncementSearchBean.setJobTitle(jobTitle);
        jobAnnouncementSearchBean.setJobType(jobType);
        jobAnnouncementSearchBean.setCompanyName(companyName);
        jobAnnouncementSearchBean.setLocation(location);
        jobAnnouncementSearchBean.setWorkingHours(workingHours);
        jobAnnouncementSearchBean.setSalary(salary);
        jobAnnouncementSearchBean.setRole(role);
    }

    @FXML
    private void handleGoBack(){

        if (context != null) {
            Printer.print("Going back to StudentHome...");
            context.setState(new HomeStudentState(context));
            context.showMenu();
        } else {
            Printer.print("Context is NOT initialized in FilterJobAnnouncement!");
        }

    }
}

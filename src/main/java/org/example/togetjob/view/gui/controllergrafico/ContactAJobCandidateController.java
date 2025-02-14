package org.example.togetjob.view.gui.controllergrafico;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import org.example.togetjob.bean.*;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.boundary.ContactAJobCandidateRecruiterBoundary;
import org.example.togetjob.view.GUIContext;
import org.example.togetjob.view.gui.concretestate.FilterJobCandidateState;

import java.util.List;

public class ContactAJobCandidateController {

    private GUIContext context;
    private JobAnnouncementBean jobAnnouncementBean;
    private StudentInfoSearchBean studentInfoSearchBean;
    private final ContactAJobCandidateRecruiterBoundary contactAJobCandidateRecruiterBoundary = new ContactAJobCandidateRecruiterBoundary();

    @FXML
    private TableView<StudentInfoBean> candidatesTable;
    @FXML
    private TableColumn<StudentInfoBean, String> nameColumn;
    @FXML
    private TableColumn<StudentInfoBean, String> degreeColumn;
    @FXML
    private TableColumn<StudentInfoBean, Void> actionColumn;

    public void setContext(GUIContext context) {
        this.context = context;
    }

    public void setJobAnnouncementBean(JobAnnouncementBean jobAnnouncementBean) {
        this.jobAnnouncementBean = jobAnnouncementBean;
    }

    public void setStudentInfoSearchBean(StudentInfoSearchBean studentInfoSearchBean) {
        this.studentInfoSearchBean = studentInfoSearchBean;
        populateCandidatesTable();
    }

    private void populateCandidatesTable() {
        List<StudentInfoBean> candidates = getFilteredCandidates(studentInfoSearchBean, jobAnnouncementBean);

        ObservableList<StudentInfoBean> observableCandidates = FXCollections.observableArrayList(candidates);
        candidatesTable.setItems(observableCandidates);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        degreeColumn.setCellValueFactory(new PropertyValueFactory<>("degreesAsString"));

        actionColumn.setCellFactory(param -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    StudentInfoBean candidate = getTableView().getItems().get(getIndex());

                    Button viewButton = new Button("View Details");
                    Button contactButton = new Button("Contact");

                    viewButton.setOnAction(event -> showCandidateDetails(candidate));
                    contactButton.setOnAction(event -> contactCandidate(candidate));

                    HBox buttons = new HBox(10, viewButton, contactButton);
                    setGraphic(buttons);
                }
            }
        });
    }

    public List<StudentInfoBean> getFilteredCandidates(StudentInfoSearchBean searchCriteria, JobAnnouncementBean jobAnnouncement) {
        return contactAJobCandidateRecruiterBoundary.getFilteredCandidates(searchCriteria, jobAnnouncement);
    }

    private void showCandidateDetails(StudentInfoBean candidate) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Job Candidates");
        alert.setHeaderText(candidate.getUsername());
        alert.setContentText("Degrees: " + formatList(candidate.getDegrees()) +
                "\nCourses Attended: " + formatList(candidate.getCoursesAttended()) +
                "\nCertifications: " + formatList(candidate.getCertifications()) +
                "\nWork Experiences: " + formatList(candidate.getWorkExperiences()) +
                "\nSkills: " + formatList(candidate.getSkills()) +
                "\nAvailability: " + candidate.getAvailability());
        alert.showAndWait();
    }

    private void contactCandidate(StudentInfoBean candidate) {
        InterviewSchedulingBean form = getInterviewSchedulingForm(candidate, jobAnnouncementBean);
        boolean success = inviteCandidateToInterview(form);

        Alert alert = new Alert(success ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle("Interview Invitation");
        alert.setHeaderText(success ? "Invitation Sent!" : "Error Sending Invitation");
        alert.setContentText(success ? "The candidate has been successfully invited to an interview."
                : "An error occurred while sending the invitation.");
        alert.showAndWait();
    }

    public InterviewSchedulingBean getInterviewSchedulingForm(StudentInfoBean candidate, JobAnnouncementBean jobAnnouncement) {
        return contactAJobCandidateRecruiterBoundary.getInterviewSchedulingForm(candidate, jobAnnouncement);
    }

    public boolean inviteCandidateToInterview(InterviewSchedulingBean interviewDetails) {
        return contactAJobCandidateRecruiterBoundary.inviteCandidateToInterview(interviewDetails);
    }

    @FXML
    private void handleBackButton() {
        if (context != null) {
            Printer.print("Going back to filters...");
            context.setState(new FilterJobCandidateState(context, jobAnnouncementBean));
            context.showMenu();
        } else {
            Printer.print("Context is NOT initialized in RegisterController!");
        }
    }

    private String formatList(List<String> list) {
        return (list == null || list.isEmpty()) ? "N/A" : String.join(", ", list);
    }
}

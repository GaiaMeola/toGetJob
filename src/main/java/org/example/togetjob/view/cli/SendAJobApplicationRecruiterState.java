package org.example.togetjob.view.cli;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.JobApplicationBean;
import org.example.togetjob.exceptions.*;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.State;
import org.example.togetjob.view.boundary.SendAJobApplicationRecruiterBoundary;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.CliContext;

import java.util.List;
import java.util.Scanner;

public class SendAJobApplicationRecruiterState implements State{

    private final SendAJobApplicationRecruiterBoundary sendAJobApplicationRecruiterBoundary = new SendAJobApplicationRecruiterBoundary();
    private final JobAnnouncementBean jobAnnouncementBean;
    private static final String GO_BACK = "Go Back \n";
    private static final String CHOOSE_AN_OPTION = "Choose an option";

    public SendAJobApplicationRecruiterState(JobAnnouncementBean jobAnnouncementBean) {
        this.jobAnnouncementBean = jobAnnouncementBean;
    }

    @Override
    public void showMenu() {
        Printer.print("\n --- Job Applications and Interview Schedules for: " + jobAnnouncementBean.getJobTitle() + " ---");
        Printer.print("Welcome, Recruiter! You can do the following:");
        Printer.print("1. View all job applications for this job announcement");
        Printer.print("2. View all interview schedules for this job announcement");
        Printer.print("3." + GO_BACK);
        Printer.print(CHOOSE_AN_OPTION);
    }

    @Override
    public void goNext(Context contextState, String input) {

        CliContext context = (CliContext) contextState;
        Scanner scanner = context.getScanner();

        try {
            switch (input.toLowerCase()) {
                case "1": // View job applications for a specific job announcement
                    viewJobApplicationsForJobAnnouncement(scanner);
                    break;

                case "2": // View and manage interview schedules for the job announcement
                    //** not implemented *//
                    break;

                case "3": // Go back to previous state
                    Printer.print("Returning to job announcement management...");
                    context.setState(new PublishAJobAnnouncementRecruiterState());
                    break;

                default:
                    Printer.print("Please try again." + CHOOSE_AN_OPTION);
                    break;
            }
        } catch (JobAnnouncementNotFoundException e) {
            Printer.print(e.getMessage());
        }
    }

    private void viewJobApplicationsForJobAnnouncement(Scanner scanner) {
        try {
            // Call the boundary method to fetch job applications for a specific job announcement
            List<JobApplicationBean> jobApplications = sendAJobApplicationRecruiterBoundary.getAllJobApplications(jobAnnouncementBean);

            if (jobApplications.isEmpty()) {
                Printer.print("No job applications found for this job announcement.");
            } else {
                // Display the list of job applications
                Printer.print("Here are the job applications for the job announcement: " + jobAnnouncementBean.getJobTitle());
            }

            // Print the details of each job application
            for (int i = 0; i < jobApplications.size(); i++) {
                JobApplicationBean jobApplication = jobApplications.get(i);
                Printer.print((i + 1) + ". Applicant: " + jobApplication.getStudentUsername() +
                        " | Status: " + jobApplication.getStatus());
            }

            // Allow the user to select an application to manage
            Printer.print("Enter the number of the application you want to manage, or 0 to go back: ");
            int selection = scanner.nextInt();
            scanner.nextLine();

            if (selection > 0 && selection <= jobApplications.size()) {
                JobApplicationBean selectedApplication = jobApplications.get(selection - 1);
                manageApplication(scanner, selectedApplication);
            } else if (selection == 0) {
                Printer.print("Returning to the job announcement menu...");
            } else {
                Printer.print("Invalid selection. Please try again.");
            }

        } catch (DatabaseException e) {
            Printer.print("Error: A database error occurred while fetching the job applications. Please try again later.");
        } catch (JobAnnouncementNotFoundException e) {
            Printer.print("Error: The job announcement could not be found. It may have been removed or is no longer available.");
        } catch (UnauthorizedAccessException e) {
            Printer.print("Error: You do not have permission to view the job applications for this job announcement.");
        } catch (Exception e) {
            Printer.print("Unexpected error: " + e.getMessage());
        }
    }

    private void manageApplication(Scanner scanner, JobApplicationBean selectedApplication){
        Printer.print("Managing application from " + selectedApplication.getStudentUsername());
        Printer.print("1. Accept this application");
        Printer.print("2. Reject this application");
        Printer.print("3." + GO_BACK);
        Printer.print(CHOOSE_AN_OPTION);
        String action = scanner.nextLine();

        boolean success;
        try {
            switch (action) {
                case "1": // Accept the application
                    success = sendAJobApplicationRecruiterBoundary.acceptJobApplication(selectedApplication);
                    if (success) {
                        Printer.print("Application accepted successfully.");
                    } else {
                        Printer.print("Failed to accept the application.");
                    }
                    break;

                case "2": // Reject the application
                    success = sendAJobApplicationRecruiterBoundary.rejectJobApplication(selectedApplication);
                    if (success) {
                        Printer.print("Application rejected successfully.");
                    } else {
                        Printer.print("Failed to reject the application.");
                    }
                    break;

                case "3": // Go back to the applications list
                    Printer.print("Returning to job applications...");
                    break;

                default:
                    Printer.print("Invalid option. Please try again.");
                    break;
            }
        } catch (JobApplicationNotFoundException | DatabaseException e) {
            Printer.print(e.getMessage());
        }
    }
}

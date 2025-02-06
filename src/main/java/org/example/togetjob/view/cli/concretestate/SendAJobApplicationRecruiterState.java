package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.JobApplicationBean;
import org.example.togetjob.boundary.PublishAJobAnnouncementRecruiterBoundary;
import org.example.togetjob.boundary.SendAJobApplicationRecruiterBoundary;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.util.List;
import java.util.Scanner;

public class SendAJobApplicationRecruiterState implements CliState {

    private final SendAJobApplicationRecruiterBoundary sendAJobApplicationRecruiterBoundary;
    private final JobAnnouncementBean jobAnnouncementBean;

    public SendAJobApplicationRecruiterState(JobAnnouncementBean jobAnnouncementBean) {
        this.jobAnnouncementBean = jobAnnouncementBean;
        this.sendAJobApplicationRecruiterBoundary = new SendAJobApplicationRecruiterBoundary();
    }


    @Override
    public void showMenu() {
        Printer.print("\n --- Job Applications for: " + jobAnnouncementBean.getJobTitle() + " ---");
        Printer.print("Welcome, Recruiter! You can do the following:");
        Printer.print("1. View all job applications for this job announcement");
        Printer.print("2. Go back");
        Printer.print("Choose an option: ");
    }

    @Override
    public void goNext(CliContext context, String input) {

        Scanner scanner = context.getScanner();

        switch (input.toLowerCase()) {
            case "1": // View job applications for a specific job announcement
                viewJobApplicationsForJobAnnouncement(scanner);
                break;

            case "2": // Go back to previous state
                Printer.print("Returning to job announcement management...");
                context.setState(new PublishAJobAnnouncementRecruiterState());
                break;

            default:
                Printer.print("Invalid option. Please try again.");
                break;
        }
    }


    private void viewJobApplicationsForJobAnnouncement(Scanner scanner) {

        // Call the boundary method to fetch job announcements
        List<JobApplicationBean> jobApplications = sendAJobApplicationRecruiterBoundary.getAllJobApplications(jobAnnouncementBean);

        if (jobApplications.isEmpty()) {
            Printer.print("No job applications found for this job announcement.");
        } else {
            // Display the list of job applications
            Printer.print("Here are the job applications for the job announcement: " + jobAnnouncementBean.getJobTitle());

        }

        for (int i = 0; i < jobApplications.size(); i++) {
            JobApplicationBean jobApplication = jobApplications.get(i);
            Printer.print((i + 1) + ". Applicant: " + jobApplication.getStudentUsername() +
                    " | Status: " + jobApplication.getStatus());
        }

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

    }


    private void manageApplication(Scanner scanner, JobApplicationBean selectedApplication) {
        Printer.print("Managing application from " + selectedApplication.getStudentUsername());
        Printer.print("1. Accept this application");
        Printer.print("2. Reject this application");
        Printer.print("3. Go back");
        Printer.print("Choose an option: ");
        String action = scanner.nextLine();

        boolean success;
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
    }


}

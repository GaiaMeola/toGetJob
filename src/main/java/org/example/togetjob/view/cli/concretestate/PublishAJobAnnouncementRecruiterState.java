package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.exceptions.*;
import org.example.togetjob.view.Context;
import org.example.togetjob.view.State;
import org.example.togetjob.view.boundary.PublishAJobAnnouncementRecruiterBoundary;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.CliContext;

import java.util.Scanner;

public class PublishAJobAnnouncementRecruiterState implements State{

    private final PublishAJobAnnouncementRecruiterBoundary publishAJobAnnouncementRecruiterBoundary = new PublishAJobAnnouncementRecruiterBoundary();

    @Override
    public void showMenu() {

        Printer.print("\n --- Job Announcements - Recruiter ---");
        Printer.print("Welcome, Recruiter! You can do the following:");
        Printer.print("1. View all your job announcements");
        Printer.print("2. Create a new job announcement");
        Printer.print("3. Manage a job announcement");
        Printer.print("4. Go back");
        Printer.print("5. Exit");
        Printer.print("Choose an option: ");

    }

    @Override
    public void goNext(Context contextState, String input) {

        CliContext context = (CliContext) contextState;
        Scanner scanner = context.getScanner();

        switch (input.toLowerCase()) {
            case "1": // View job announcements
                viewJobAnnouncements(scanner, context);
                break;

            case "2": // Create a new job announcement
                createJobAnnouncement(scanner);
                context.setState(new PublishAJobAnnouncementRecruiterState());
                break;

            case "3": // Manage job announcement
                manageJobAnnouncement(scanner);
                context.setState(new PublishAJobAnnouncementRecruiterState());
                break;

            case "4": // Go back to previous state
                Printer.print("Returning to recruiter home...");
                context.setState(new HomeRecruiterState());
                break;

            case "5": // Exit application
                Printer.print("Exiting application...");
                context.setState(new ExitState());
                break;

            default:
                Printer.print("Invalid option. Please try again.");
                break;
        }

    }


    private void viewJobAnnouncements(Scanner scanner, CliContext context) {
        // Call the boundary method to fetch job announcements
        var jobAnnouncements = publishAJobAnnouncementRecruiterBoundary.getJobAnnouncements();

        if (jobAnnouncements.isEmpty()) {
            Printer.print("No job announcements found.");
        } else {
            // Display the announcements
            Printer.print("Here are the job announcements you have created or are collaborating on:");

            for (int i = 0; i < jobAnnouncements.size(); i++) {
                var job = jobAnnouncements.get(i);
                Printer.print((i + 1) + ". Title: " + job.getJobTitle() + " | Active: " + job.isActive());
            }

            Printer.print("Enter the number of the job announcement you want to manage: ");
            int jobSelection = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character after the number input

            // Validate the input
            if (jobSelection < 1 || jobSelection > jobAnnouncements.size()) {
                Printer.print("Invalid selection. Please try again.");
                return; // Exit the method to let the user try again
            }

            var selectedJob = jobAnnouncements.get(jobSelection - 1);

            Printer.print("Do you want to view the details of this job announcement? (yes/no): ");
            String viewResponse = scanner.nextLine().trim().toLowerCase();

            if ("yes".equals(viewResponse)) {
                // Display the details of the selected job announcement
                Printer.print("Job Title: " + selectedJob.getJobTitle());
                Printer.print("Job Type: " + selectedJob.getJobType());
                Printer.print("Location: " + selectedJob.getLocation());
                Printer.print("Working hours: " + selectedJob.getWorkingHours());
                Printer.print("Company name: " + selectedJob.getCompanyName());
                Printer.print("Salary: " + selectedJob.getSalary());
                Printer.print("Description: " + selectedJob.getDescription());
                Printer.print("Active: " + selectedJob.isActive());

            }

            // Ask if the recruiter wants to view job applications
            Printer.print("Do you want to view job applications or scheduling interviews sent for this job announcement? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if ("yes".equals(response)) {
                // Pass the selected job title to the next state for managing job applications
                context.setState(new SendAJobApplicationRecruiterState(selectedJob));
            } else {
                Printer.print("Returning to job announcements...");
            }

        }

    }


    private void createJobAnnouncement(Scanner scanner) {
        try {
            Printer.print("Enter the details for the new job announcement:");

            JobAnnouncementBean jobAnnouncementBean = new JobAnnouncementBean();

            jobAnnouncementBean.setJobTitle(getValidInput(scanner, "Enter Job Title: "));
            jobAnnouncementBean.setJobType(getValidInput(scanner, "Enter Job Type: "));
            jobAnnouncementBean.setRole(getValidInput(scanner, "Enter Role: "));
            jobAnnouncementBean.setLocation(getValidInput(scanner, "Enter Location: "));
            jobAnnouncementBean.setWorkingHours(getValidInput(scanner, "Enter Working Hours: "));
            jobAnnouncementBean.setCompanyName(getValidInput(scanner, "Enter Company Name: "));
            jobAnnouncementBean.setSalary(getValidInput(scanner, "Enter Salary: "));
            jobAnnouncementBean.setDescription(getValidInput(scanner, "Enter Description: "));

            // Call the boundary method to publish the job announcement
            boolean success = publishAJobAnnouncementRecruiterBoundary.publishJobAnnouncement(jobAnnouncementBean);

            if (success) {
                Printer.print("Job announcement created successfully.");
            } else {
                Printer.print("Failed to create job announcement.");
            }
        } catch (JobAnnouncementAlreadyExists | InvalidWorkingHourException | InvalidSalaryException | UserNotLoggedException e) {
            Printer.print(e.getMessage());
        }
    }

    private String getValidInput(Scanner scanner, String prompt) {
        String input;
        do {
            Printer.print(prompt);
            input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                Printer.print("This field cannot be empty. Please enter a valid value.");
            }
        } while (input.isEmpty());
        return input;
    }

    private void manageJobAnnouncement(Scanner scanner) {
        // This method will allow the recruiter to manage (deactivate, activate, or delete) a job announcement

        var jobAnnouncements = publishAJobAnnouncementRecruiterBoundary.getJobAnnouncements();

        if (jobAnnouncements.isEmpty()) {
            Printer.print("No job announcements found.");
        } else {
            // Display the announcements
            Printer.print("Here are the job announcements you have created or are collaborating on:");

            for (int i = 0; i < jobAnnouncements.size(); i++) {
                var job = jobAnnouncements.get(i);
                Printer.print((i + 1) + ". Title: " + job.getJobTitle() + " | Active: " + job.isActive());
            }
        }

        Printer.print("Enter the title of the job announcement to manage: ");
        String jobTitle = scanner.nextLine();

        Printer.print("Choose an action: ");
        Printer.print("0. Go back");
        Printer.print("1. Deactivate");
        Printer.print("2. Activate");
        Printer.print("3. Delete");
        Printer.print("Choose an option: ");
        String action = scanner.nextLine();

        if ("0".equals(action)) {
            Printer.print("Returning to the previous menu...");
            return; // Exit the method and return to the previous menu
        }

        JobAnnouncementBean jobAnnouncementBean = new JobAnnouncementBean();
        jobAnnouncementBean.setJobTitle(jobTitle);

        boolean success;

        switch (action) {
            case "1": // Deactivate
                try {
                    success = publishAJobAnnouncementRecruiterBoundary.deactivateJobAnnouncement(jobAnnouncementBean);
                    if (success) {
                        Printer.print("Job announcement deactivated successfully.");
                    } else {
                        Printer.print("Failed to deactivate job announcement.");
                    }
                } catch (DatabaseException e) {
                    Printer.print(e.getMessage());
                }
                break;

            case "2": // Activate
                success = publishAJobAnnouncementRecruiterBoundary.activateJobAnnouncement(jobAnnouncementBean);
                if (success) {
                    Printer.print("Job announcement activated successfully.");
                } else {
                    Printer.print("Failed to activate job announcement.");
                }
                break;

            case "3": // Delete
                success = publishAJobAnnouncementRecruiterBoundary.deleteJobAnnouncement(jobAnnouncementBean);
                if (success) {
                    Printer.print("Job announcement deleted successfully.");
                } else {
                    Printer.print("Failed to delete job announcement.");
                }
                break;

            default:
                Printer.print("Invalid option. Returning to the previous menu.");
                break;
        }
    }

}


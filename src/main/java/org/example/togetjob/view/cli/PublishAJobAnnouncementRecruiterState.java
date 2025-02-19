package org.example.togetjob.view.cli;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.exceptions.*;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.State;
import org.example.togetjob.view.boundary.PublishAJobAnnouncementRecruiterBoundary;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.CliContext;

import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class PublishAJobAnnouncementRecruiterState implements State {

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

            // Using getValidInputWithValidation to handle user input with validation
            getValidInputWithValidation(scanner, "Enter Job Title: ", jobAnnouncementBean::setJobTitle);
            getValidInputWithValidation(scanner, "Enter Job Type: ", jobAnnouncementBean::setJobType);
            getValidInputWithValidation(scanner, "Enter Role: ", jobAnnouncementBean::setRole);
            getValidInputWithValidation(scanner, "Enter Location: ", jobAnnouncementBean::setLocation);
            getValidInputWithValidation(scanner, "Enter Working Hours: ", jobAnnouncementBean::setWorkingHours);
            getValidInputWithValidation(scanner, "Enter Company Name: ", jobAnnouncementBean::setCompanyName);
            getValidInputWithValidation(scanner, "Enter Salary: ", jobAnnouncementBean::setSalary);
            getValidInputWithValidation(scanner, "Enter Description: ", jobAnnouncementBean::setDescription);

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

    // New method to handle validation and error handling
    private void getValidInputWithValidation(Scanner scanner, String prompt, Consumer<String> setter) {
        while (true) {
            try {
                String input = getValidInput(scanner, prompt);
                setter.accept(input);  // Set the value through the setter
                return;  // Exit if input is valid
            } catch (Exception e) {
                Printer.print("Error: " + e.getMessage() + ". Please try again.");
            }
        }
    }

    private String getValidInput(Scanner scanner, String prompt) {
        Printer.print(prompt);
        return scanner.nextLine().trim();
    }

    private void manageJobAnnouncement(Scanner scanner) {
        // Retrieve the list of job announcements
        var jobAnnouncements = publishAJobAnnouncementRecruiterBoundary.getJobAnnouncements();

        if (jobAnnouncements.isEmpty()) {
            Printer.print("No job announcements found.");
            return;
        }

        // Display the announcements
        displayJobAnnouncements(jobAnnouncements);

        // Prompt for the job title to manage
        String jobTitle = getJobTitle(scanner);
        if (jobTitle == null) return;  // If the input is invalid, exit the method

        // Display available actions
        Printer.print("Choose an action: ");
        Printer.print("0. Go back");
        Printer.print("1. Deactivate");
        Printer.print("2. Activate");
        Printer.print("3. Delete");

        String action = scanner.nextLine();

        if ("0".equals(action)) {
            Printer.print("Returning to the previous menu...");
            return;
        }

        // Perform the selected action
        handleJobAction(action, jobTitle);
    }

    private void displayJobAnnouncements(List<JobAnnouncementBean> jobAnnouncements) {
        Printer.print("Here are the job announcements you have created or are collaborating on:");
        for (int i = 0; i < jobAnnouncements.size(); i++) {
            var job = jobAnnouncements.get(i);
            Printer.print((i + 1) + ". Title: " + job.getJobTitle() + " | Active: " + job.isActive());
        }
    }

    private String getJobTitle(Scanner scanner) {
        Printer.print("Enter the title of the job announcement to manage: ");
        String jobTitle = scanner.nextLine();

        if (jobTitle.isEmpty()) {
            Printer.print("Job title cannot be empty. Returning to previous menu.");
            return null;
        }

        return jobTitle;
    }

    private void handleJobAction(String action, String jobTitle) {
        JobAnnouncementBean jobAnnouncementBean = new JobAnnouncementBean();
        jobAnnouncementBean.setJobTitle(jobTitle);

        boolean success;

        switch (action) {
            case "1":
                success = deactivateJobAnnouncement(jobAnnouncementBean);
                break;
            case "2":
                success = activateJobAnnouncement(jobAnnouncementBean);
                break;
            case "3":
                success = deleteJobAnnouncement(jobAnnouncementBean);
                break;
            default:
                Printer.print("Invalid option. Returning to the previous menu.");
                return;
        }

        if (success) {
            Printer.print("Job announcement action performed successfully.");
        } else {
            Printer.print("Failed to perform the action.");
        }
    }

    private boolean deactivateJobAnnouncement(JobAnnouncementBean jobAnnouncementBean) {
        try {
            return publishAJobAnnouncementRecruiterBoundary.deactivateJobAnnouncement(jobAnnouncementBean);
        } catch (DatabaseException e) {
            Printer.print(e.getMessage());
            return false;
        }
    }

    private boolean activateJobAnnouncement(JobAnnouncementBean jobAnnouncementBean) {
        try {
            return publishAJobAnnouncementRecruiterBoundary.activateJobAnnouncement(jobAnnouncementBean);
        } catch (DatabaseException e) {
            Printer.print(e.getMessage());
            return false;
        }
    }

    private boolean deleteJobAnnouncement(JobAnnouncementBean jobAnnouncementBean) {
        try {
            return publishAJobAnnouncementRecruiterBoundary.deleteJobAnnouncement(jobAnnouncementBean);
        } catch (DatabaseException e) {
            Printer.print(e.getMessage());
            return false;
        }
    }
}

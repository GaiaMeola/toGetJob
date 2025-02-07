package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.boundary.PublishAJobAnnouncementRecruiterBoundary;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.util.Scanner;

public class PublishAJobAnnouncementRecruiterState implements CliState {

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
    public void goNext(CliContext context, String input) {

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
                Printer.print((i + 1) + ". Title: " + job.getJobTitle() + " | Location: " + job.getLocation() +
                        " | Salary: " + job.getSalary() + " | Active: " + job.isActive());
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
                Printer.print("Location: " + selectedJob.getLocation());
                Printer.print("Salary: " + selectedJob.getSalary());
                Printer.print("Active: " + selectedJob.isActive());
                Printer.print("Description: " + selectedJob.getDescription());

                // Ask if the recruiter wants to view job applications
                Printer.print("Do you want to view job applications for this job announcement? (yes/no): ");
                String response = scanner.nextLine().trim().toLowerCase();

                if ("yes".equals(response)) {
                    // Pass the selected job title to the next state for managing job applications
                    context.setState(new SendAJobApplicationRecruiterState(selectedJob));
                } else {
                    Printer.print("Returning to job announcements...");
                }
            } else {
                Printer.print("Returning to job announcements...");
            }
        }
    }

    private void createJobAnnouncement(Scanner scanner) {
        // Collect job details from the user to create a new job announcement
        Printer.print("Enter the details for the new job announcement:");

        JobAnnouncementBean jobAnnouncementBean = new JobAnnouncementBean();

        Printer.print("Enter Job Title: ");
        jobAnnouncementBean.setJobTitle(scanner.nextLine());

        Printer.print("Enter Job Type: ");
        jobAnnouncementBean.setJobType(scanner.nextLine());

        Printer.print("Enter Role: ");
        jobAnnouncementBean.setRole(scanner.nextLine());

        Printer.print("Enter Location: ");
        jobAnnouncementBean.setLocation(scanner.nextLine());

        Printer.print("Enter Working Hours: ");
        jobAnnouncementBean.setWorkingHours(scanner.nextLine());

        Printer.print("Enter Company Name: ");
        jobAnnouncementBean.setCompanyName(scanner.nextLine());

        Printer.print("Enter Salary: ");
        jobAnnouncementBean.setSalary(scanner.nextLine());

        Printer.print("Enter Description: ");
        jobAnnouncementBean.setDescription(scanner.nextLine());

        // Call the boundary method to publish the job announcement
        boolean success = publishAJobAnnouncementRecruiterBoundary.publishJobAnnouncement(jobAnnouncementBean);

        if (success) {
            Printer.print("Job announcement created successfully.");
        } else {
            Printer.print("Failed to create job announcement.");
        }
    }


    private void manageJobAnnouncement(Scanner scanner) {
        // This method will allow the recruiter to manage (deactivate, activate, or delete) a job announcement
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
                success = publishAJobAnnouncementRecruiterBoundary.deactivateJobAnnouncement(jobAnnouncementBean);
                if (success) {
                    Printer.print("Job announcement deactivated successfully.");
                } else {
                    Printer.print("Failed to deactivate job announcement.");
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

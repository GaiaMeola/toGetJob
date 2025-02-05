package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.boundary.PublishAJobAnnouncementRecruiterBoundary;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.util.Scanner;

public class JobAnnouncementRecruiterState implements CliState {

    private final PublishAJobAnnouncementRecruiterBoundary jobAnnouncementRecruiterBoundary = new PublishAJobAnnouncementRecruiterBoundary();

    @Override
    public void showMenu() {
        Printer.print("\n --- Job Announcements - Recruiter ---");
        Printer.print("Welcome, Recruiter! You can do the following:");
        Printer.print("1. View all your job announcements");
        Printer.print("2. Create a new job announcement");
        Printer.print("3. Deactivate a job announcement");
        Printer.print("4. Delete a job announcement");
        Printer.print("5. Go back");
        Printer.print("6. Exit");
        Printer.print("Choose an option: ");
    }

    @Override
    public void goNext() {

    }

    @Override
    public void goNext(CliContext context, String input) {

        Scanner scanner = context.getScanner();

        switch (input.toLowerCase()) {
            case "1": // View job announcements
                viewJobAnnouncements();
                context.setState(new JobAnnouncementRecruiterState());
                break;

            case "2": // Create a new job announcement
                createJobAnnouncement(scanner);
                context.setState(new JobAnnouncementRecruiterState());
                break;

            case "3": // Deactivate a job announcement
                deactivateJobAnnouncement(scanner);
                context.setState(new JobAnnouncementRecruiterState());
                break;

            case "4": // Delete a job announcement
                deleteJobAnnouncement(scanner);
                context.setState(new JobAnnouncementRecruiterState());
                break;

            case "5": // Go back to previous state
                Printer.print("Returning to recruiter home...");
                context.setState(new HomeRecruiterState());
                break;

            case "6": // Exit application
                Printer.print("Exiting application...");
                context.setState(new ExitState());
                break;

            default:
                Printer.print("Invalid option. Please try again.");
                break;
        }

    }


    private void viewJobAnnouncements() {
        // Call the boundary method to fetch job announcements
        var jobAnnouncements = jobAnnouncementRecruiterBoundary.getJobAnnouncements();

        if (jobAnnouncements.isEmpty()) {
            Printer.print("No job announcements found.");
        } else {
            // Display the announcements
            for (var job : jobAnnouncements) {
                Printer.print("Title: " + job.getJobTitle());
                Printer.print("Location: " + job.getLocation());
                Printer.print("Salary: " + job.getSalary());
                Printer.print("Active: " + job.isActive());
                Printer.print("-------------------------");
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
        boolean success = jobAnnouncementRecruiterBoundary.publishJobAnnouncement(jobAnnouncementBean);

        if (success) {
            Printer.print("Job announcement created successfully.");
        } else {
            Printer.print("Failed to create job announcement.");
        }
    }

    private void deactivateJobAnnouncement(Scanner scanner) {
        Printer.print("Enter the title of the job announcement to deactivate: ");
        String jobTitle = scanner.nextLine();

        JobAnnouncementBean jobAnnouncementBean = new JobAnnouncementBean();
        jobAnnouncementBean.setJobTitle(jobTitle);

        // Call the boundary method to deactivate the job announcement
        boolean success = jobAnnouncementRecruiterBoundary.deactivateJobAnnouncement(jobAnnouncementBean);

        if (success) {
            Printer.print("Job announcement deactivated successfully.");
        } else {
            Printer.print("Failed to deactivate job announcement.");
        }
    }

    private void deleteJobAnnouncement(Scanner scanner) {
        // Collect job title from the user to delete the job announcement
        Printer.print("Enter the title of the job announcement to delete: ");
        String jobTitle = scanner.nextLine();

        JobAnnouncementBean jobAnnouncementBean = new JobAnnouncementBean();
        jobAnnouncementBean.setJobTitle(jobTitle);

        // Call the boundary method to delete the job announcement
        boolean success = jobAnnouncementRecruiterBoundary.deleteJobAnnouncement(jobAnnouncementBean);

        if (success) {
            Printer.print("Job announcement deleted successfully.");
        } else {
            Printer.print("Failed to delete job announcement.");
        }
    }

}

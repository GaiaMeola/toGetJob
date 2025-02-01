package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.boundary.PublishAJobAnnouncementRecruiterBoundary;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.util.Scanner;

public class JobAnnouncementRecruiterState implements CliState {

    private final PublishAJobAnnouncementRecruiterBoundary jobAnnouncementRecruiterBoundary = new PublishAJobAnnouncementRecruiterBoundary();

    @Override
    public void showMenu() {
        System.out.println("\n --- Job Announcements - Recruiter ---");
        System.out.println("Welcome, Recruiter! You can do the following:");
        System.out.println("1. View all your job announcements");
        System.out.println("2. Create a new job announcement");
        System.out.println("3. Deactivate a job announcement");
        System.out.println("4. Delete a job announcement");
        System.out.println("5. Go back");
        System.out.println("6. Exit");
        System.out.print("Choose an option: ");
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
                System.out.println("Returning to recruiter home...");
                context.setState(new HomeRecruiterState());
                break;

            case "6": // Exit application
                System.out.println("Exiting application...");
                context.setState(new ExitState());
                break;

            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }

    }


    private void viewJobAnnouncements() {
        // Call the boundary method to fetch job announcements
        var jobAnnouncements = jobAnnouncementRecruiterBoundary.getJobAnnouncements();

        if (jobAnnouncements.isEmpty()) {
            System.out.println("No job announcements found.");
        } else {
            // Display the announcements
            for (var job : jobAnnouncements) {
                System.out.println("Title: " + job.getJobTitle());
                System.out.println("Location: " + job.getLocation());
                System.out.println("Salary: " + job.getSalary());
                System.out.println("Active: " + job.isActive());
                System.out.println("-------------------------");
            }
        }
    }

    private void createJobAnnouncement(Scanner scanner) {
        // Collect job details from the user to create a new job announcement
        System.out.println("Enter the details for the new job announcement:");

        JobAnnouncementBean jobAnnouncementBean = new JobAnnouncementBean();

        System.out.print("Enter Job Title: ");
        jobAnnouncementBean.setJobTitle(scanner.nextLine());

        System.out.print("Enter Job Type: ");
        jobAnnouncementBean.setJobType(scanner.nextLine());

        System.out.print("Enter Role: ");
        jobAnnouncementBean.setRole(scanner.nextLine());

        System.out.print("Enter Location: ");
        jobAnnouncementBean.setLocation(scanner.nextLine());

        System.out.print("Enter Working Hours: ");
        jobAnnouncementBean.setWorkingHours(scanner.nextLine());

        System.out.print("Enter Company Name: ");
        jobAnnouncementBean.setCompanyName(scanner.nextLine());

        System.out.print("Enter Salary: ");
        jobAnnouncementBean.setSalary(scanner.nextLine());

        System.out.print("Enter Description: ");
        jobAnnouncementBean.setDescription(scanner.nextLine());

        // Call the boundary method to publish the job announcement
        boolean success = jobAnnouncementRecruiterBoundary.publishJobAnnouncement(jobAnnouncementBean);

        if (success) {
            System.out.println("Job announcement created successfully.");
        } else {
            System.out.println("Failed to create job announcement.");
        }
    }

    private void deactivateJobAnnouncement(Scanner scanner) {
        System.out.print("Enter the title of the job announcement to deactivate: ");
        String jobTitle = scanner.nextLine();

        JobAnnouncementBean jobAnnouncementBean = new JobAnnouncementBean();
        jobAnnouncementBean.setJobTitle(jobTitle);

        // Call the boundary method to deactivate the job announcement
        boolean success = jobAnnouncementRecruiterBoundary.deactivateJobAnnouncement(jobAnnouncementBean);

        if (success) {
            System.out.println("Job announcement deactivated successfully.");
        } else {
            System.out.println("Failed to deactivate job announcement.");
        }
    }

    private void deleteJobAnnouncement(Scanner scanner) {
        // Collect job title from the user to delete the job announcement
        System.out.print("Enter the title of the job announcement to delete: ");
        String jobTitle = scanner.nextLine();

        JobAnnouncementBean jobAnnouncementBean = new JobAnnouncementBean();
        jobAnnouncementBean.setJobTitle(jobTitle);

        // Call the boundary method to delete the job announcement
        boolean success = jobAnnouncementRecruiterBoundary.deleteJobAnnouncement(jobAnnouncementBean);

        if (success) {
            System.out.println("Job announcement deleted successfully.");
        } else {
            System.out.println("Failed to delete job announcement.");
        }
    }

}

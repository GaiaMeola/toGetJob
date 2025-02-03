package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.boundary.PublishAJobAnnouncementRecruiterBoundary;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.util.Scanner;
import java.util.logging.Logger;

public class JobAnnouncementRecruiterState implements CliState {

    private static final Logger logger = Logger.getLogger(JobAnnouncementRecruiterState.class.getName());
    private final PublishAJobAnnouncementRecruiterBoundary jobAnnouncementRecruiterBoundary = new PublishAJobAnnouncementRecruiterBoundary();

    @Override
    public void showMenu() {
        logger.info("\n --- Job Announcements - Recruiter ---");
        logger.info("Welcome, Recruiter! You can do the following:");
        logger.info("1. View all your job announcements");
        logger.info("2. Create a new job announcement");
        logger.info("3. Deactivate a job announcement");
        logger.info("4. Delete a job announcement");
        logger.info("5. Go back");
        logger.info("6. Exit");
        logger.info("Choose an option: ");
    }

    @Override
    public void goNext() {
        // Non implementato
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
                logger.info("Returning to recruiter home...");
                context.setState(new HomeRecruiterState());
                break;

            case "6": // Exit application
                logger.info("Exiting application...");
                context.setState(new ExitState());
                break;

            default:
                logger.warning("Invalid option. Please try again.");
                break;
        }

    }

    private void viewJobAnnouncements() {
        // Call the boundary method to fetch job announcements
        var jobAnnouncements = jobAnnouncementRecruiterBoundary.getJobAnnouncements();

        if (jobAnnouncements.isEmpty()) {
            logger.info("No job announcements found.");
        } else {
            // Display the announcements
            for (var job : jobAnnouncements) {
                logger.info("Title: " + job.getJobTitle());
                logger.info("Location: " + job.getLocation());
                logger.info("Salary: " + job.getSalary());
                logger.info("Active: " + job.isActive());
                logger.info("-------------------------");
            }
        }
    }

    private void createJobAnnouncement(Scanner scanner) {
        // Collect job details from the user to create a new job announcement
        logger.info("Enter the details for the new job announcement:");

        JobAnnouncementBean jobAnnouncementBean = new JobAnnouncementBean();

        logger.info("Enter Job Title: ");
        jobAnnouncementBean.setJobTitle(scanner.nextLine());

        logger.info("Enter Job Type: ");
        jobAnnouncementBean.setJobType(scanner.nextLine());

        logger.info("Enter Role: ");
        jobAnnouncementBean.setRole(scanner.nextLine());

        logger.info("Enter Location: ");
        jobAnnouncementBean.setLocation(scanner.nextLine());

        logger.info("Enter Working Hours: ");
        jobAnnouncementBean.setWorkingHours(scanner.nextLine());

        logger.info("Enter Company Name: ");
        jobAnnouncementBean.setCompanyName(scanner.nextLine());

        logger.info("Enter Salary: ");
        jobAnnouncementBean.setSalary(scanner.nextLine());

        logger.info("Enter Description: ");
        jobAnnouncementBean.setDescription(scanner.nextLine());

        // Call the boundary method to publish the job announcement
        boolean success = jobAnnouncementRecruiterBoundary.publishJobAnnouncement(jobAnnouncementBean);

        if (success) {
            logger.info("Job announcement created successfully.");
        } else {
            logger.warning("Failed to create job announcement.");
        }
    }

    private void deactivateJobAnnouncement(Scanner scanner) {
        logger.info("Enter the title of the job announcement to deactivate: ");
        String jobTitle = scanner.nextLine();

        JobAnnouncementBean jobAnnouncementBean = new JobAnnouncementBean();
        jobAnnouncementBean.setJobTitle(jobTitle);

        // Call the boundary method to deactivate the job announcement
        boolean success = jobAnnouncementRecruiterBoundary.deactivateJobAnnouncement(jobAnnouncementBean);

        if (success) {
            logger.info("Job announcement deactivated successfully.");
        } else {
            logger.warning("Failed to deactivate job announcement.");
        }
    }

    private void deleteJobAnnouncement(Scanner scanner) {
        // Collect job title from the user to delete the job announcement
        logger.info("Enter the title of the job announcement to delete: ");
        String jobTitle = scanner.nextLine();

        JobAnnouncementBean jobAnnouncementBean = new JobAnnouncementBean();
        jobAnnouncementBean.setJobTitle(jobTitle);

        // Call the boundary method to delete the job announcement
        boolean success = jobAnnouncementRecruiterBoundary.deleteJobAnnouncement(jobAnnouncementBean);

        if (success) {
            logger.info("Job announcement deleted successfully.");
        } else {
            logger.warning("Failed to delete job announcement.");
        }
    }

}

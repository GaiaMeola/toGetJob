package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.boundary.SendAJobApplicationStudentBoundary;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.util.List;
import java.util.Scanner;

public class SendAJobApplicationState implements CliState {

    private final SendAJobApplicationStudentBoundary sendAJobApplicationStudentBoundary = new SendAJobApplicationStudentBoundary();

    @Override
    public void showMenu() {

        System.out.println("\n --- Show Job Announcements - Student ---");
        System.out.println("Welcome, Student! You can do the following:");
        System.out.println("1. View available job announcements");
        System.out.println("2. Go back");
        System.out.println("3. Exit");
        System.out.print("Choose an option: ");
    }

    @Override
    public void goNext() {

    }

    @Override
    public void goNext(CliContext context, String input) {

        /* da rivedere

        Scanner scanner = context.getScanner();


        switch (input.toLowerCase()) {
            case "1": // View job announcements
                viewJobAnnouncements(scanner);
                context.setState(new SendAJobApplicationState()); // Stay in the same state after viewing
                break;

            case "2": // Go back to previous state
                System.out.println("Returning to student home...");
                context.setState(new HomeStudentState());
                break;

            case "3": // Exit application
                System.out.println("Exiting application...");
                context.setState(new ExitState());
                break;

            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }


    private void viewJobAnnouncements(Scanner scanner) {
        // Call the boundary method to fetch job announcements
        List<JobAnnouncementBean> jobAnnouncements = sendAJobApplicationStudentBoundary.getJobAnnouncements();

        if (jobAnnouncements.isEmpty()) {
            System.out.println("No job announcements found.");
        } else {
            // Display the announcements
            for (int i = 0; i < jobAnnouncements.size(); i++) {
                JobAnnouncementBean job = jobAnnouncements.get(i);
                System.out.println((i + 1) + ". " + job.getJobTitle() + " - Location: " + job.getLocation() + " - Salary: " + job.getSalary());
            }

            // Allow the student to apply to a selected job announcement
            applyToJob(scanner, jobAnnouncements);
        }
    }

    private void applyToJob(Scanner scanner, List<JobAnnouncementBean> jobAnnouncements) {
        // Ask the student to choose a job to apply to
        System.out.print("Please enter the number of the job you want to apply to: ");
        int jobIndex = scanner.nextInt() - 1;
        scanner.nextLine();  // Consume the newline

        if (jobIndex < 0 || jobIndex >= jobAnnouncements.size()) {
            System.out.println("Invalid choice. Please try again.");
            return;
        }

        // Get the selected job announcement
        JobAnnouncementBean selectedJob = jobAnnouncements.get(jobIndex);

        // Collect the student's application details
        JobApplicationBean jobApplicationBean = new JobApplicationBean();
        jobApplicationBean.setJobTitle(selectedJob.getJobTitle());
        jobApplicationBean.setStudentName("StudentName"); // Replace with actual student's name or reference

        System.out.println("Enter your motivation letter: ");
        jobApplicationBean.setMotivationLetter(scanner.nextLine());

        System.out.println("Enter your resume reference (e.g., file path or link): ");
        jobApplicationBean.setResumeReference(scanner.nextLine());

        // Send the application
        boolean success = sendAJobApplicationStudentBoundary.sendJobApplication(jobApplicationBean);

        if (success) {
            System.out.println("Your job application has been successfully submitted.");
        } else {
            System.out.println("Failed to submit your job application. Please try again later.");
        }
    }






}

package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.JobAnnouncementSearchBean;
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

        Scanner scanner = context.getScanner();


        switch (input.toLowerCase()) {
            case "1": // View job announcements
                applyFiltersAndShowJobAnnouncements(scanner);
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


    private void applyFiltersAndShowJobAnnouncements(Scanner scanner) {

        System.out.println("\nEnter your search filters:");

        System.out.print("Enter job title (or leave blank to skip): ");
        String jobTitle = scanner.nextLine();

        System.out.print("Enter job type (or leave blank to skip): ");
        String jobType = scanner.nextLine();

        System.out.print("Enter role (or leave blank to skip): ");
        String role = scanner.nextLine();

        System.out.print("Enter location (or leave blank to skip): ");
        String location = scanner.nextLine();

        System.out.print("Enter working hours (or leave blank to skip): ");
        String workingHours = scanner.nextLine();

        System.out.print("Enter company name (or leave blank to skip): ");
        String companyName = scanner.nextLine();

        System.out.print("Enter salary (or leave blank to skip): ");
        String salary = scanner.nextLine();

        JobAnnouncementSearchBean searchBean = new JobAnnouncementSearchBean(jobTitle, jobType, role, location, workingHours, companyName, salary);

        System.out.println("\n --- Filters you have selected ---");
        System.out.println("Job Title: " + (jobTitle.isEmpty() ? "Not specified" : jobTitle));
        System.out.println("Location: " + (location.isEmpty() ? "Not specified" : location));
        System.out.println("Salary: " + (salary.isEmpty() ? "Not specified" : salary));
        System.out.println("Working Hours: " + (workingHours.isEmpty() ? "Not specified" : workingHours));
        System.out.println("Company: " + (companyName.isEmpty() ? "Not specified" : companyName));
        System.out.println("\nDo you want to proceed with these filters?");
        System.out.println("1. Proceed");
        System.out.println("2. Go back and change filters");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            System.out.println("\nProceeding with the selected filters...");
            proceedWithFilters(scanner,searchBean);
        } else if (choice.equals("2")) {
            System.out.println("Returning to filter selection...");
            applyFiltersAndShowJobAnnouncements(scanner);
        } else {
            System.out.println("Invalid choice. Please try again.");
            applyFiltersAndShowJobAnnouncements(scanner);
        }
    }


    private void proceedWithFilters(Scanner scanner, JobAnnouncementSearchBean searchBean) {

        List<JobAnnouncementBean> jobApplications = sendAJobApplicationStudentBoundary.getJobAnnouncements(searchBean);

        if (jobApplications.isEmpty()) {
            System.out.println("No job applications found with the specified filters.");
        } else {
            for (int i = 0; i < jobApplications.size(); i++) {
                JobAnnouncementBean job = jobApplications.get(i);
                System.out.println((i + 1) + ". Job Title: " + job.getJobTitle() + " - Location: " + job.getLocation() + " - Salary: " + job.getSalary());
            }

            showJobAnnouncementDetails(scanner, jobApplications);
        }
    }

    private void showJobAnnouncementDetails(Scanner scanner, List<JobAnnouncementBean> jobAnnouncements) {
        // Ask the student to choose a job
        System.out.print("Enter the number of the job to see more details: ");
        int jobIndex = scanner.nextInt() - 1;
        scanner.nextLine();  // Consume the newline

        if (jobIndex < 0 || jobIndex >= jobAnnouncements.size()) {
            System.out.println("Invalid choice. Please try again.");
            return;
        }

        JobAnnouncementBean selectedJob = jobAnnouncements.get(jobIndex);
        JobAnnouncementBean jobDetails = sendAJobApplicationStudentBoundary.getJobAnnouncementDetail(selectedJob);

        System.out.println("\n --- Job Details ---");
        System.out.println("Job Title: " + jobDetails.getJobTitle());
        System.out.println("Company: " + jobDetails.getCompanyName());
        System.out.println("Location: " + jobDetails.getLocation());
        System.out.println("Salary: " + jobDetails.getSalary());
        System.out.println("Working Hours: " + jobDetails.getWorkingHours());
        System.out.println("Job Type: " + jobDetails.getJobType());
        System.out.println("Role: " + jobDetails.getRole());
        System.out.println("Job Description: " + jobDetails.getDescription());


        // Allow user to go back or apply for the job
        System.out.println("\nDo you want to apply for this job?");
        System.out.println("1. Send your job application");
        System.out.println("2. Go back");
        System.out.print("Choose an option: ");
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            // Proceed with job application process (not yet implemented)
            System.out.println("Applying for the job...");
            // Implement the application logic here
        } else if (choice.equals("2")) {
            // Go back to job announcement details
            showJobAnnouncementDetails(scanner, jobAnnouncements);
        } else {
            System.out.println("Invalid choice. Please try again.");
            showJobAnnouncementDetails(scanner, jobAnnouncements);
        }

    }

}
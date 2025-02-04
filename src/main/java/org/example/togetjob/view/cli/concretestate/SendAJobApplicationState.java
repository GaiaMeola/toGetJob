package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.JobAnnouncementSearchBean;
import org.example.togetjob.bean.JobApplicationBean;
import org.example.togetjob.boundary.SendAJobApplicationStudentBoundary;
import org.example.togetjob.exceptions.JobAnnouncementNotActiveException;
import org.example.togetjob.exceptions.JobApplicationAlreadySentException;
import org.example.togetjob.model.entity.Status;
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
        System.out.println("2. View sent job applications");
        System.out.println("3. Go back");
        System.out.println("4. Exit");
        System.out.print("Choose an option: ");;
    }

    @Override
    public void goNext() {

    }

    @Override
    public void goNext(CliContext context, String input) {

        Scanner scanner = context.getScanner();


        switch (input.toLowerCase()) {
            case "1":
                applyFiltersAndShowJobAnnouncements(scanner);
                break;

            case "2":
                viewAndManageJobApplications(scanner);
                break;

            case "3":
                System.out.println("Returning to student home...");
                context.setState(new HomeStudentState());
                break;

            case "4":
                System.out.println("Exiting application...");
                context.setState(new ExitState());
                break;

            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }

    private void viewAndManageJobApplications(Scanner scanner){

        List<JobApplicationBean> jobApplications = sendAJobApplicationStudentBoundary.getJobApplicationsByStudent();

        if (jobApplications.isEmpty()) {
            System.out.println("You haven't sent any job applications yet.");
            return;
        }

        System.out.println("\n --- Your Job Applications ---");
        int index = 1;
        for (JobApplicationBean application : jobApplications) {
            System.out.println(index + ". Job Title: " + application.getJobTitle() +
                    " | Status: " + application.getStatus());
            index++;
        }

        System.out.println("\nSelect an application to manage, or enter 0 to go back:");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice == 0) {
            return; // Go back
        }

        if (choice < 1 || choice > jobApplications.size()) {
            System.out.println("Invalid selection. Please try again.");
            return;
        }

        JobApplicationBean selectedApplication = jobApplications.get(choice - 1);

        if (selectedApplication.getStatus() == Status.PENDING) {
            System.out.println("\nWhat would you like to do?");
            System.out.println("1. Modify this application");
            System.out.println("2. Delete this application");
            System.out.println("3. Go back");
            System.out.print("Choose an option: ");

            String action = scanner.nextLine();
            switch (action) {
                case "1":
                    modifyJobApplication(scanner, selectedApplication);
                    break;
                case "2":
                    deleteJobApplication(scanner, selectedApplication);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid option. Returning to main menu.");
            }
        } else {
            System.out.println("This application has already been processed and cannot be modified or deleted.");
        }

    }

    private void modifyJobApplication(Scanner scanner, JobApplicationBean jobApplicationBean){

        System.out.println("Enter a new cover letter:");
        String newCoverLetter = scanner.nextLine();
        jobApplicationBean.setCoverLetter(newCoverLetter);

        boolean success = sendAJobApplicationStudentBoundary.modifyAJobApplication(jobApplicationBean);
        if (success) {
            System.out.println("Job application successfully modified!");
        } else {
            System.out.println("Error modifying the job application. It may have already been processed.");
        }

    }

    private void deleteJobApplication(Scanner scanner, JobApplicationBean jobApplicationBean){

        System.out.println("Are you sure you want to delete this application? (yes/no)");
        String confirmation = scanner.nextLine().toLowerCase();

        if (!confirmation.equals("yes")) {
            System.out.println("Deletion canceled.");
            return;
        }

        boolean success = sendAJobApplicationStudentBoundary.deleteAJobApplication(jobApplicationBean);
        if (success) {
            System.out.println("Job application successfully deleted!");
        } else {
            System.out.println("Error deleting the job application. It may have already been processed.");
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
            // Proceed to the job application process
            applyForJob(scanner, selectedJob, jobAnnouncements);
        } else if (choice.equals("2")) {
            // Go back to job announcements list
            showJobAnnouncementDetails(scanner, jobAnnouncements);
        } else {
            System.out.println("Invalid choice. Please try again.");
            showJobAnnouncementDetails(scanner, jobAnnouncements);
        }
    }

    private void applyForJob(Scanner scanner, JobAnnouncementBean selectedJob, List<JobAnnouncementBean> jobAnnouncements) {
        try {
            System.out.println("Filling out the application form...");

            JobApplicationBean jobApplicationBean = sendAJobApplicationStudentBoundary.fillJobApplicationForm(selectedJob);

            if (jobApplicationBean != null) {
                System.out.println("\n --- Review your Application ---");
                System.out.println("Job Title: " + jobApplicationBean.getJobTitle());
                System.out.println("Applicant Name: " + jobApplicationBean.getStudentUsername());

                // Ask the user for their cover letter
                System.out.print("Enter your cover letter: ");
                String coverLetter = scanner.nextLine();
                jobApplicationBean.setCoverLetter(coverLetter);

                System.out.println("Cover Letter: " + jobApplicationBean.getCoverLetter());

                // Confirm submission
                System.out.println("\nDo you want to submit your application?");
                System.out.println("1. Submit");
                System.out.println("2. Go back");
                System.out.print("Choose an option: ");
                String submitChoice = scanner.nextLine();

                if (submitChoice.equals("1")) {
                    // Attempt to submit the application
                    boolean isApplicationSent = sendAJobApplicationStudentBoundary.sendAJobApplication(jobApplicationBean);
                    if (isApplicationSent) {
                        System.out.println("Your application has been successfully submitted!");
                    } else {
                        System.out.println("There was an error submitting your application. Please try again.");
                    }
                } else if (submitChoice.equals("2")) {
                    // Go back to job details
                    showJobAnnouncementDetails(scanner, jobAnnouncements);
                } else {
                    System.out.println("Invalid choice. Please try again.");
                    applyForJob(scanner, selectedJob, jobAnnouncements);
                }
            } else {
                System.out.println("Failed to fill out the application form. Please try again.");
            }
        } catch (JobAnnouncementNotActiveException e) {
            // Handle case where the job announcement is no longer active
            System.out.println("Error: " + e.getMessage());
        } catch (JobApplicationAlreadySentException e) {
            // Handle case where the user has already submitted an application
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            // Handle any other unexpected exceptions
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }

}
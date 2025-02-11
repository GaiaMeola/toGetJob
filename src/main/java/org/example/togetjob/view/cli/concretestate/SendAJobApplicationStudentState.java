package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.JobAnnouncementSearchBean;
import org.example.togetjob.bean.JobApplicationBean;
import org.example.togetjob.view.boundary.SendAJobApplicationStudentBoundary;
import org.example.togetjob.exceptions.JobAnnouncementNotActiveException;
import org.example.togetjob.exceptions.JobApplicationAlreadySentException;
import org.example.togetjob.model.entity.Status;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.util.List;
import java.util.Scanner;

public class SendAJobApplicationStudentState implements CliState {

    private final SendAJobApplicationStudentBoundary sendAJobApplicationStudentBoundary = new SendAJobApplicationStudentBoundary();
    private static final String CHOSE_AN_OPTION = "Choose an option: ";
    private static final String NOT_SPECIFIED = "Not specified";
    private static final String JOB_TITLE = "Job title: ";
    private static final String LOCATION = "Location: ";
    private static final String SALARY = "Salary: ";
    private static final String WORKING_HOURS = "Working Hours: ";
    private static final String COMPANY = "Company: ";
    private static final String JOB_TYPE = "Job Type: ";
    private static final String JOB_ROLE = "Job Role: ";

    @Override
    public void showMenu() {

        Printer.print("\n --- Show Job Announcements - Student ---");
        Printer.print("Welcome, Student! You can do the following:");
        Printer.print("1. View available job announcements");
        Printer.print("2. View sent job applications");
        Printer.print("3. Go back");
        Printer.print("4. Exit");
        Printer.print(CHOSE_AN_OPTION);
    }

    @Override
    public void goNext(CliContext context, String input) {

        Scanner scanner = context.getScanner();

        switch (input.toLowerCase()) {
            case "1":
                applyFiltersAndShowJobAnnouncements(scanner); //show filters and job announcements
                break;

            case "2":
                viewAndManageJobApplications(scanner); // view past job applications
                break;

            case "3":
                Printer.print("Returning to student home...");
                context.setState(new HomeStudentState());
                break;

            case "4":
                Printer.print("Exiting application...");
                context.setState(new ExitState());
                break;

            default:
                Printer.print("Invalid option. Please try again.");
                break;
        }
    }

    private void viewAndManageJobApplications(Scanner scanner){

        List<JobApplicationBean> jobApplications = sendAJobApplicationStudentBoundary.getJobApplicationsByStudent();

        if (jobApplications.isEmpty()) {
            Printer.print("You haven't sent any job applications yet.");
            return;
        }

        Printer.print("\n --- Your Job Applications ---");
        int index = 1;
        for (JobApplicationBean application : jobApplications) {
            Printer.print(index + ". Your Job Application for the Job Announcement << " + application.getJobTitle() +
                    " >> is " + application.getStatus());
            index++;
        }

        Printer.print("\nSelect an application to manage, or enter 0 to go back:");
        int choice = Integer.parseInt(scanner.nextLine());

        if (choice == 0) {
            return; // Go back
        }

        if (choice < 1 || choice > jobApplications.size()) {
            Printer.print("Invalid selection. Please try again.");
            return;
        }

        JobApplicationBean selectedApplication = jobApplications.get(choice - 1);

        if (selectedApplication.getStatus() == Status.PENDING) {
            Printer.print("\nWhat would you like to do?");
            Printer.print("1. Modify this application");
            Printer.print("2. Delete this application");
            Printer.print("3. Go back");
            Printer.print(CHOSE_AN_OPTION);

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
                    Printer.print("Invalid option. Returning to main menu.");
            }
        } else {
            Printer.print("This application has already been processed and cannot be modified or deleted." +
                    "\nPlease visit the ‘View sent job applications’ section to view the current status of your application.");
        }

    }

    private void modifyJobApplication(Scanner scanner, JobApplicationBean jobApplicationBean){

        Printer.print("Enter a new cover letter:");
        String newCoverLetter = scanner.nextLine();
        jobApplicationBean.setCoverLetter(newCoverLetter);

        boolean success = sendAJobApplicationStudentBoundary.modifyAJobApplication(jobApplicationBean);
        if (success) {
            Printer.print("Job application successfully modified!");
        } else {
            Printer.print("Error modifying the job application. It may have already been processed.");
        }

    }

    private void deleteJobApplication(Scanner scanner, JobApplicationBean jobApplicationBean){

        Printer.print("Are you sure you want to delete this application? (yes/no)");
        String confirmation = scanner.nextLine().toLowerCase();

        if (!confirmation.equals("yes")) {
            Printer.print("Deletion canceled.");
            return;
        }

        boolean success = sendAJobApplicationStudentBoundary.deleteAJobApplication(jobApplicationBean);
        if (success) {
            Printer.print("Job application successfully deleted!");
        } else {
            Printer.print("Error deleting the job application. It may have already been processed.");
        }

    }


    private void applyFiltersAndShowJobAnnouncements(Scanner scanner) {

        Printer.print("\nEnter your search filters:");

        Printer.print("Enter job title (or leave blank to skip): ");
        String jobTitle = scanner.nextLine();

        Printer.print("Enter job type (or leave blank to skip): ");
        String jobType = scanner.nextLine();

        Printer.print("Enter role (or leave blank to skip): ");
        String role = scanner.nextLine();

        Printer.print("Enter location (or leave blank to skip): ");
        String location = scanner.nextLine();

        Printer.print("Enter working hours (or leave blank to skip): ");
        String workingHours = scanner.nextLine();

        Printer.print("Enter company name (or leave blank to skip): ");
        String companyName = scanner.nextLine();

        Printer.print("Enter salary (or leave blank to skip): ");
        String salary = scanner.nextLine();

        JobAnnouncementSearchBean searchBean = new JobAnnouncementSearchBean(); // Empty

        searchBean.setJobTitle(jobTitle);
        searchBean.setJobType(jobType);
        searchBean.setRole(role);
        searchBean.setLocation(location);
        searchBean.setWorkingHours(workingHours);
        searchBean.setCompanyName(companyName);
        searchBean.setSalary(salary);


        Printer.print("\n --- Filters you have selected ---");
        Printer.print(JOB_TITLE + (jobTitle.isEmpty() ? NOT_SPECIFIED : jobTitle));
        Printer.print(JOB_TYPE + (jobType.isEmpty() ? NOT_SPECIFIED : jobType));
        Printer.print(JOB_ROLE + (role.isEmpty() ? NOT_SPECIFIED : role));
        Printer.print(LOCATION + (location.isEmpty() ? NOT_SPECIFIED : location));
        Printer.print(SALARY + (salary.isEmpty() ? NOT_SPECIFIED : salary));
        Printer.print(WORKING_HOURS  + (workingHours.isEmpty() ? NOT_SPECIFIED : workingHours));
        Printer.print(COMPANY + (companyName.isEmpty() ? NOT_SPECIFIED : companyName));
        Printer.print("\nDo you want to proceed with these filters?");
        Printer.print("1. Proceed");
        Printer.print("2. Go back and change filters");
        Printer.print(CHOSE_AN_OPTION);
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            Printer.print("\nProceeding with the selected filters...");
            proceedWithFilters(scanner,searchBean);
        } else if (choice.equals("2")) {
            Printer.print("Returning to filter selection...");
            applyFiltersAndShowJobAnnouncements(scanner);
        } else {
            Printer.print("Please try again." + CHOSE_AN_OPTION);
            applyFiltersAndShowJobAnnouncements(scanner);
        }
    }


    private void proceedWithFilters(Scanner scanner, JobAnnouncementSearchBean searchBean) {

        List<JobAnnouncementBean> jobAnnouncements = sendAJobApplicationStudentBoundary.getJobAnnouncements(searchBean);

        if (jobAnnouncements.isEmpty()) {
            Printer.print("No job announcements found with the specified filters.");
        } else {
            for (int i = 0; i < jobAnnouncements.size(); i++) {
                JobAnnouncementBean job = jobAnnouncements.get(i);
                Printer.print((i + 1) + ". Job Title: " + job.getJobTitle() + " - Location: " + job.getLocation() + " - Salary: " + job.getSalary());
            }

            showJobAnnouncementDetails(scanner, jobAnnouncements);
        }
    }

    private void showJobAnnouncementDetails(Scanner scanner, List<JobAnnouncementBean> jobAnnouncements) {
        // Ask the student to choose a job
        Printer.print("Enter the number of the job to see more details: ");
        int jobIndex = scanner.nextInt() - 1;
        scanner.nextLine();  // Consume the newline

        if (jobIndex < 0 || jobIndex >= jobAnnouncements.size()) {
            Printer.print("Please try again." + CHOSE_AN_OPTION);
            return;
        }

        JobAnnouncementBean selectedJob = jobAnnouncements.get(jobIndex);

        Printer.print("\n --- Job Details ---");
        Printer.print(JOB_TITLE + selectedJob.getJobTitle());
        Printer.print(JOB_TYPE+ selectedJob.getJobType());
        Printer.print(JOB_ROLE  + selectedJob.getRole());
        Printer.print(LOCATION + selectedJob.getLocation());
        Printer.print(SALARY + selectedJob.getSalary());
        Printer.print(WORKING_HOURS + selectedJob.getWorkingHours());
        Printer.print(COMPANY+ selectedJob.getCompanyName());
        Printer.print("Job Description: " + selectedJob.getDescription());


        // Allow user to go back or apply for the job
        Printer.print("\nDo you want to apply for this job?");
        Printer.print("1. Send your job application");
        Printer.print("2. Go back");
        Printer.print(CHOSE_AN_OPTION);
        String choice = scanner.nextLine();

        if (choice.equals("1")) {
            // Proceed to the job application process
            applyForJob(scanner, selectedJob, jobAnnouncements);
        } else if (choice.equals("2")) {
            // Go back to job announcements list
            showJobAnnouncementDetails(scanner, jobAnnouncements);
        } else {
            Printer.print("Invalid choice. Please try again.");
            showJobAnnouncementDetails(scanner, jobAnnouncements);
        }
    }

    private void applyForJob(Scanner scanner, JobAnnouncementBean selectedJob, List<JobAnnouncementBean> jobAnnouncements) {
        try {
            Printer.print("Filling out the application form...");

            JobApplicationBean jobApplicationBean = sendAJobApplicationStudentBoundary.fillJobApplicationForm(selectedJob);

            if (jobApplicationBean != null) {
                Printer.print("\n --- Review your Application ---");
                Printer.print("Job Title: " + jobApplicationBean.getJobTitle());
                Printer.print("Applicant Name: " + jobApplicationBean.getStudentUsername());

                // Ask the user for their cover letter
                Printer.print("Enter your cover letter: ");
                String coverLetter = scanner.nextLine();
                jobApplicationBean.setCoverLetter(coverLetter);

                Printer.print("Cover Letter: " + jobApplicationBean.getCoverLetter());

                // Confirm submission
                Printer.print("\nDo you want to submit your application?");
                Printer.print("1. Submit");
                Printer.print("2. Go back");
                Printer.print(CHOSE_AN_OPTION);
                String submitChoice = scanner.nextLine();

                if (submitChoice.equals("1")) {
                    // Attempt to submit the application
                    boolean isApplicationSent = sendAJobApplicationStudentBoundary.sendAJobApplication(jobApplicationBean);
                    if (isApplicationSent) {
                        Printer.print("Your application has been successfully submitted!");
                    } else {
                        Printer.print("There was an error submitting your application. Please try again.");
                    }
                } else if (submitChoice.equals("2")) {
                    // Go back to job details
                    showJobAnnouncementDetails(scanner, jobAnnouncements);
                } else {
                    Printer.print("Invalid choice. Please try again.");
                    applyForJob(scanner, selectedJob, jobAnnouncements);
                }
            } else {
                Printer.print("Failed to fill out the application form. Please try again.");
            }
        } catch (JobAnnouncementNotActiveException | JobApplicationAlreadySentException e) {
            // Handle case where the job announcement is no longer active
            Printer.print("Error: " + e.getMessage());
        }  catch (Exception e) {
            // Handle any other unexpected exceptions
            Printer.print("An unexpected error occurred: " + e.getMessage());
        }
    }

}
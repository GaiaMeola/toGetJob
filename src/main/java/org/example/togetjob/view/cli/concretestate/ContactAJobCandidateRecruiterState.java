package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.bean.InterviewSchedulingBean;
import org.example.togetjob.bean.JobAnnouncementBean;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.bean.StudentInfoSearchBean;
import org.example.togetjob.exceptions.*;
import org.example.togetjob.view.boundary.ContactAJobCandidateRecruiterBoundary;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.util.List;
import java.util.Scanner;

public class ContactAJobCandidateRecruiterState implements CliState {

    private final ContactAJobCandidateRecruiterBoundary boundary = new ContactAJobCandidateRecruiterBoundary();
    private static final String CHOSE_AN_OPTION = "Choose an option: ";
    private static final String NOT_SPECIFIED = "Not specified";

    @Override
    public void showMenu() {
        Printer.print("\n ---Contact a Job Candidate---");
        Printer.print("Select an option:");
        Printer.print("1. View all candidates");
        Printer.print("2. Go back");
        Printer.print(CHOSE_AN_OPTION);
    }

    @Override
    public void goNext(CliContext context, String input) {

        Scanner scanner = context.getScanner();

        switch (input) {
            case "1":
                // all candidates
                Printer.print("Fetching candidates...");
                viewJobAnnouncements(scanner);
                break;
            case "2":
                context.setState(new HomeRecruiterState());
                break;
            default:
                Printer.print("Invalid option. Please try again.");
                showMenu();
                break;
        }

    }

    private void viewJobAnnouncements(Scanner scanner) {
        // Call the boundary method to fetch job announcements
        var jobAnnouncements = boundary.getJobAnnouncementsByRecruiter();

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
                Printer.print("Location: " + selectedJob.getLocation());
                Printer.print("Salary: " + selectedJob.getSalary());
                Printer.print("Active: " + selectedJob.isActive());
                Printer.print("Description: " + selectedJob.getDescription());
            }

            Printer.print("Do you want to contact a job candidate? (yes/no): ");
            viewResponse = scanner.nextLine().trim().toLowerCase();

            if ("yes".equals(viewResponse)) {
                applyFiltersAndShowCandidates(scanner, selectedJob); // Pass selected job to filter candidates
            }
        }
    }


    private void applyFiltersAndShowCandidates(Scanner scanner, JobAnnouncementBean selectedJob) {
        Printer.print("\nEnter the filters you'd like to apply:");

        // filters to the recruiter
        Printer.print("Enter degree (or leave blank to skip): ");
        String degree = scanner.nextLine();

        Printer.print("Enter course attended (or leave blank to skip): ");
        String course = scanner.nextLine();

        Printer.print("Enter certifications (or leave blank to skip): ");
        String certification = scanner.nextLine();

        Printer.print("Enter work experience (or leave blank to skip): ");
        String workExperience = scanner.nextLine();

        Printer.print("Enter skills (or leave blank to skip): ");
        String skills = scanner.nextLine();

        Printer.print("Enter availability (or leave blank to skip): ");
        String availability = scanner.nextLine();


        StudentInfoSearchBean filters = new StudentInfoSearchBean();
        filters.setDegrees(List.of(degree.isEmpty() ? "" : degree));
        filters.setCoursesAttended(List.of(course.isEmpty() ? "" : course));
        filters.setCertifications(List.of(certification.isEmpty() ? "" : certification));
        filters.setWorkExperiences(List.of(workExperience.isEmpty() ? "" : workExperience));
        filters.setSkills(List.of(skills.isEmpty() ? "" : skills));
        filters.setAvailability(availability.isEmpty() ? "" : availability);

        Printer.print("\n --- Filters you have selected ---");
        Printer.print("Degree: " + (degree.isEmpty() ? NOT_SPECIFIED : degree));
        Printer.print("Course: " + (course.isEmpty() ?  NOT_SPECIFIED : course));
        Printer.print("Certification: " + (certification.isEmpty() ?  NOT_SPECIFIED : certification));
        Printer.print("Work Experience: " + (workExperience.isEmpty() ?  NOT_SPECIFIED : workExperience));
        Printer.print("Skills: " + (skills.isEmpty() ?  NOT_SPECIFIED : skills));
        Printer.print("Availability: " + (availability.isEmpty() ?  NOT_SPECIFIED : availability));

        Printer.print("\nDo you want to proceed with these filters?");
        Printer.print("1. Proceed");
        Printer.print("2. Go back and change filters");
        Printer.print(CHOSE_AN_OPTION);
        String choice = scanner.nextLine();

        if ("1".equals(choice)) {
            Printer.print("\nProceeding with the selected filters...");
            List<StudentInfoBean> filteredCandidates = boundary.getFilteredCandidates(filters, selectedJob); // Candidates filtered
            displayCandidates(scanner, filteredCandidates, selectedJob);
        } else if ("2".equals(choice)) {
            Printer.print("Returning to filter selection...");
            applyFiltersAndShowCandidates(scanner, selectedJob);
        } else {
            Printer.print("Invalid choice. Please try again.");
            applyFiltersAndShowCandidates(scanner, selectedJob);
        }

    }


    private void displayCandidates(Scanner scanner, List<StudentInfoBean> candidatesList, JobAnnouncementBean selectedJob) {
        if (candidatesList.isEmpty()) {
            Printer.print("No candidates found.");
        } else {
            for (int i = 0; i < candidatesList.size(); i++) {
                StudentInfoBean candidate = candidatesList.get(i);
                Printer.print((i + 1) + ". " + candidate.getUsername());
            }

            Printer.print("\nDo you want to view the details of any candidate?");
            Printer.print("1. View candidate details");
            Printer.print("2. Go back");
            Printer.print(CHOSE_AN_OPTION);
            String choice = scanner.nextLine();

            if ("1".equals(choice)) {
                viewCandidateDetails(scanner, candidatesList, selectedJob);
            } else if ("2".equals(choice)) {
                showMenu();
            } else {
                Printer.print("Invalid option. Please try again.");
                displayCandidates(scanner, candidatesList, selectedJob);
            }
        }
    }


    private void viewCandidateDetails(Scanner scanner, List<StudentInfoBean> candidatesList, JobAnnouncementBean selectedJob) {
        Printer.print("Enter the number of the candidate to view details: ");
        int candidateIndex = scanner.nextInt() - 1;
        scanner.nextLine(); // Consume the newline

        if (candidateIndex < 0 || candidateIndex >= candidatesList.size()) {
            Printer.print("Invalid choice. Please try again.");
            return;
        }

        StudentInfoBean selectedCandidate = candidatesList.get(candidateIndex);
        Printer.print("\n --- Candidate Details ---");
        Printer.print("Username: " + selectedCandidate.getUsername());
        Printer.print("Degree: " + selectedCandidate.getDegrees());
        Printer.print("Courses: " + selectedCandidate.getCoursesAttended());
        Printer.print("Certifications: " + selectedCandidate.getCertifications());
        Printer.print("Work Experience: " + selectedCandidate.getWorkExperiences());
        Printer.print("Skills: " + selectedCandidate.getSkills());
        Printer.print("Availability: " + selectedCandidate.getAvailability());
        Printer.print("Do you want to schedule an interview with this candidate? (yes/no): ");
        String response = scanner.nextLine().trim().toLowerCase();

        if ("yes".equals(response)) {
            scheduleInterview(scanner, selectedCandidate, selectedJob);
        } else {
            Printer.print("Returning to candidate list...");
            displayCandidates(scanner, candidatesList, selectedJob); // Go back to the list of candidates
        }
    }


    private void scheduleInterview(Scanner scanner, StudentInfoBean selectedCandidate, JobAnnouncementBean selectedJob) {
        boolean validDate = false;

        while (!validDate) {
            try {
                Printer.print("Enter the interview date and time (e.g., 2025-02-10T10:00): ");
                String interviewDateTime = scanner.nextLine();

                Printer.print("Enter the location for the interview: ");
                String location = scanner.nextLine();

                InterviewSchedulingBean interviewDetails = boundary.getInterviewSchedulingForm(selectedCandidate, selectedJob);
                interviewDetails.setInterviewDateTime(interviewDateTime);
                interviewDetails.setLocation(location);

                boolean success = boundary.inviteCandidateToInterview(interviewDetails);
                if (success) {
                    Printer.print("The interview invitation has been sent successfully.");
                    validDate = true;
                } else {
                    Printer.print("There was an issue sending the interview invitation. Please try again.");
                }
            } catch (DateNotValidException e) {
                Printer.print( e.getMessage() + ". Please enter a valid date.");
            }catch (StudentNotFoundException | JobAnnouncementNotFoundException | JobApplicationNotFoundException |
                    InterviewSchedulingAlreadyExistsException | NotificationException e) {
                Printer.print(e.getMessage());
                return; // Esce dal metodo
            }
        }
    }

}


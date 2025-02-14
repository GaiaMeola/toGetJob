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
        try {
            var jobAnnouncements = boundary.getJobAnnouncementsByRecruiter();
            var selectedJob = selectJobAnnouncement(scanner, jobAnnouncements);
            if (selectedJob == null) return;

            Printer.print("Do you want to view the details of this job announcement? (yes/no): ");
            String viewResponse = scanner.nextLine().trim().toLowerCase();
            if ("yes".equals(viewResponse)) {
                Printer.print("Job Title: " + selectedJob.getJobTitle());
                Printer.print("Location: " + selectedJob.getLocation());
                Printer.print("Salary: " + selectedJob.getSalary());
                Printer.print("Active: " + selectedJob.isActive());
                Printer.print("Description: " + selectedJob.getDescription());
            }

            Printer.print("Do you want to contact a job candidate? (yes/no): ");
            viewResponse = scanner.nextLine().trim().toLowerCase();
            if ("yes".equals(viewResponse)) {
                applyFiltersAndShowCandidates(scanner, selectedJob);
            }

        } catch (DatabaseException e) {
            Printer.print(e.getMessage());
        }
    }

    private JobAnnouncementBean selectJobAnnouncement(Scanner scanner, List<JobAnnouncementBean> jobAnnouncements) {
        if (jobAnnouncements.isEmpty()) {
            Printer.print("No job announcements found.");
            return null;
        }

        Printer.print("Here are the job announcements you have created or are collaborating on:");
        for (int i = 0; i < jobAnnouncements.size(); i++) {
            var job = jobAnnouncements.get(i);
            Printer.print((i + 1) + ". Title: " + job.getJobTitle() + " | Active: " + job.isActive());
        }

        Printer.print("Enter the number of the job announcement you want to manage: ");
        int jobSelection;
        try {
            jobSelection = scanner.nextInt();
            scanner.nextLine();
            if (jobSelection < 1 || jobSelection > jobAnnouncements.size()) {
                Printer.print("Invalid selection. Please try again.");
                return null;
            }
        } catch (Exception e) {
            Printer.print("Invalid input. Please enter a valid number.");
            scanner.nextLine();
            return null;
        }

        return jobAnnouncements.get(jobSelection - 1);
    }

    private void applyFiltersAndShowCandidates(Scanner scanner, JobAnnouncementBean selectedJob) {
        try {
            Printer.print("\nEnter the filters you'd like to apply:");
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

            Printer.print("\nProceeding with the selected filters...");
            List<StudentInfoBean> filteredCandidates = boundary.getFilteredCandidates(filters, selectedJob);
            displayCandidates(scanner, filteredCandidates, selectedJob);

        } catch (DatabaseException e) {
            Printer.print(e.getMessage());
        }
    }

    private void displayCandidates(Scanner scanner, List<StudentInfoBean> candidatesList, JobAnnouncementBean selectedJob) {
        if (candidatesList.isEmpty()) {
            Printer.print("No candidates found.");
        } else {
            for (int i = 0; i < candidatesList.size(); i++) {
                Printer.print((i + 1) + ". " + candidatesList.get(i).getUsername());
            }

            Printer.print("\nDo you want to view the details of any candidate?");
            Printer.print("1. View candidate details");
            Printer.print("2. Go back");
            Printer.print(CHOSE_AN_OPTION);

            String choice = scanner.nextLine();
            if ("1".equals(choice)) {
                viewCandidateDetails(scanner, candidatesList, selectedJob);
            }
        }
    }

    private void viewCandidateDetails(Scanner scanner, List<StudentInfoBean> candidatesList, JobAnnouncementBean selectedJob) {
        Printer.print("Enter the number of the candidate to view details: ");
        int candidateIndex = scanner.nextInt() - 1;
        scanner.nextLine();

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
        String viewResponse = scanner.nextLine().trim();
        if ("yes".equalsIgnoreCase(viewResponse)) {
            scheduleInterview(scanner, selectedCandidate, selectedJob);
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

                if (boundary.inviteCandidateToInterview(interviewDetails)) {
                    Printer.print("The interview invitation has been sent successfully.");
                    validDate = true;
                }

            } catch (DateNotValidException e) {
                Printer.print(e.getMessage());
            }
        }
    }
}

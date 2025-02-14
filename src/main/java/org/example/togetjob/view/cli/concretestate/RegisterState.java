package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.bean.RecruiterInfoBean;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.exceptions.DatabaseException;
import org.example.togetjob.exceptions.UsernameTakeException;
import org.example.togetjob.view.Context;
import org.example.togetjob.view.State;
import org.example.togetjob.view.boundary.RegisterBoundary;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.CliContext;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class RegisterState implements State {

    private static final String STUDENT = "student";

    @Override
    public void showMenu() {

        Printer.print("\n ---Register---");

    }

    @Override
    public void goNext(Context contextState, String input) {

        CliContext context = (CliContext) contextState;

        Scanner scanner = context.getScanner();

        Printer.print("Welcome to toGetJob! Fill the following fields: ");

        String username = getValidInput(scanner, "Enter username: ");
        String password = getValidInput(scanner, "Enter password: ");
        String checkPassword = getValidInput(scanner, "Confirm password: ");

        while (!password.equals(checkPassword)) {
            Printer.print("Passwords do not match. Please try again.");
            password = getValidInput(scanner, "Enter password: ");
            checkPassword = getValidInput(scanner, "Confirm password: ");
        }

        String name = getValidInput(scanner, "Enter name: ");
        String surname = getValidInput(scanner, "Enter surname: ");
        String email = getValidInput(scanner, "Enter email: ");


        String roleInput;
        do {
            roleInput = getValidInput(scanner, "Enter role (student/recruiter): ").trim().toLowerCase();
            if (!roleInput.equalsIgnoreCase(STUDENT) && !roleInput.equalsIgnoreCase("recruiter")) {
                Printer.print("Invalid role. Please enter 'student' or 'recruiter'.");
            }
        } while (!roleInput.equals(STUDENT) && !roleInput.equals("recruiter"));

        RegisterUserBean userBean = new RegisterUserBean();
        userBean.setUsername(username);
        userBean.setPassword(password);
        userBean.setName(name);
        userBean.setSurname(surname);
        userBean.setEmail(email);
        userBean.setRoleInput(roleInput);

        // Polymorphism
        Object infoBean = STUDENT.equalsIgnoreCase(roleInput) ? getStudentInfo(scanner) : getRecruiterInfo(scanner);

        RegisterBoundary registerBoundary = new RegisterBoundary();
        try {
            boolean registrationSuccess = registerBoundary.registerUser(userBean, infoBean);

            if (registrationSuccess) {
                Printer.print("Registration successful!");
                context.setState(new MainMenuState());
            } else {
                Printer.print("Username already exists. Please try again.");
                context.setState(new RegisterState());
            }
        } catch (UsernameTakeException e) {
            Printer.print("Error: The username is already taken. Please choose a different username.");
            Printer.print("Would you like to try again with a different username? (yes/no)");
            if (scanner.nextLine().trim().equalsIgnoreCase("yes")) {
                context.setState(new RegisterState());
            } else {
                context.setState(new MainMenuState());
            }
        } catch (DatabaseException e) {
            Printer.print(e.getMessage());
            context.setState(new ExitState());
        } catch (Exception e) {
            Printer.print("An unexpected error occurred: " + e.getMessage());
            context.setState(new MainMenuState());
        }
    }

    private String getValidInput(Scanner scanner, String prompt) {
        String input;
        do {
            Printer.print(prompt);
            input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                Printer.print("This field cannot be empty. Please enter a valid value.");
                continue;
            }

            if (prompt.toLowerCase().contains("email") && !input.contains("@")) {
                Printer.print("Invalid email format. Please enter a valid email containing '@'.");
                continue;
            }

            break;
        } while (true);

        return input;
    }


    private StudentInfoBean getStudentInfo(Scanner scanner) {
        Printer.print("Please, complete your student profile:");

        LocalDate dateOfBirth = getValidDate(scanner);
        Printer.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        Printer.print("Enter degrees (comma-separated): ");
        List<String> degrees = List.of(scanner.nextLine().split(","));
        Printer.print("Enter courses attended (comma-separated): ");
        List<String> courses = List.of(scanner.nextLine().split(","));
        Printer.print("Enter certifications (comma-separated): ");
        List<String> certifications = List.of(scanner.nextLine().split(","));
        Printer.print("Enter work experiences (comma-separated): ");
        List<String> workExperiences = List.of(scanner.nextLine().split(","));
        Printer.print("Enter skills (comma-separated): ");
        List<String> skills = List.of(scanner.nextLine().split(","));
        Printer.print("Enter availability: ");
        String availability = scanner.nextLine();

        StudentInfoBean studentInfoBean = new StudentInfoBean();

        studentInfoBean.setDateOfBirth(dateOfBirth);
        studentInfoBean.setPhoneNumber(phoneNumber);
        studentInfoBean.setDegrees(degrees);
        studentInfoBean.setCoursesAttended(courses);
        studentInfoBean.setCertifications(certifications);
        studentInfoBean.setWorkExperiences(workExperiences);
        studentInfoBean.setSkills(skills);
        studentInfoBean.setAvailability(availability);
        return studentInfoBean;
    }

    private LocalDate getValidDate(Scanner scanner) {
        LocalDate date = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        boolean valid = false;

        do {
            try {
                Printer.print("Enter date of birth (yyyy-mm-dd): ");
                String input = scanner.nextLine().trim();
                date = LocalDate.parse(input, formatter);
                valid = true;
            } catch (DateTimeParseException e) {
                Printer.print("Invalid date format. Please enter a valid date (yyyy-MM-dd).");
            }
        } while (!valid);

        return date;
    }


    private RecruiterInfoBean getRecruiterInfo(Scanner scanner) {
        Printer.print("Please, complete your recruiter profile:");

        Printer.print("Enter the companies you work for (comma-separated): ");
        List<String> companies = List.of(scanner.nextLine().split(","));

        RecruiterInfoBean recruiterInfoBean = new RecruiterInfoBean();
        recruiterInfoBean.setCompanies(companies);

        return recruiterInfoBean;
    }

}

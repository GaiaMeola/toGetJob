package org.example.togetjob.view.cli;

import org.example.togetjob.bean.RecruiterInfoBean;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.exceptions.DatabaseException;
import org.example.togetjob.exceptions.EmailAlreadyExistsException;
import org.example.togetjob.exceptions.FileAccessException;
import org.example.togetjob.exceptions.UsernameTakeException;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.State;
import org.example.togetjob.view.boundary.RegisterBoundary;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.CliContext;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class RegisterState implements State {

    private static final String STUDENT = "student";
    private final RegisterBoundary registerBoundary = new RegisterBoundary();

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

        try {
            boolean registrationSuccess = registerBoundary.registerUser(userBean, infoBean);

            if (registrationSuccess) {
                Printer.print("Registration successful!");

                if (STUDENT.equalsIgnoreCase(roleInput)) {
                    context.setState(new HomeStudentState());
                } else {
                    context.setState(new HomeRecruiterState());
                }

            } else {
                Printer.print("Username already exists. Please try again.");
                context.setState(new RegisterState());
            }
        } catch (UsernameTakeException e) {
            Printer.print("Error: The username is already taken. Please, try again with a different username.");
            context.setState(new MainMenuState());
        } catch (EmailAlreadyExistsException e) {
            Printer.print("Error: The email is already taken. Please, try again with a different email.");
            context.setState(new MainMenuState());
        } catch (FileAccessException e) {
            Printer.print("Error accessing file. Please try again later.");
            context.setState(new ExitState());
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

        while (Period.between(dateOfBirth, LocalDate.now()).getYears() <= 18) {
            Printer.print("You must be over 18 years old to register. Please, re-enter your date of birth.");
            dateOfBirth = getValidDate(scanner);
        }

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

        String availability;
        while (true) {
            Printer.print("Enter availability (full-time or part-time): ");
            availability = scanner.nextLine().trim();
            if (availability.equalsIgnoreCase("full-time") || availability.equalsIgnoreCase("part-time")) {
                break;
            } else {
                Printer.print("Invalid availability. Please enter either 'full-time' or 'part-time'.");
            }
        }

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

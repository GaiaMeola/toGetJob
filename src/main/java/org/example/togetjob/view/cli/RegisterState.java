package org.example.togetjob.view.cli;

import org.example.togetjob.bean.RecruiterInfoBean;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.exceptions.*;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.State;
import org.example.togetjob.view.boundary.RegisterBoundary;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.CliContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;

public class RegisterState implements State {

    private static final String STUDENT = "student";
    private final RegisterBoundary registerBoundary = new RegisterBoundary();

    private static final String ERROR  = "Error: ";
    private static final String TRY  = " Please try again.";

    @Override
    public void showMenu() {
        Printer.print("\n ---Register---");
    }

    @Override
    public void goNext(Context contextState, String input) {
        CliContext context = (CliContext) contextState;
        Scanner scanner = context.getScanner();

        Printer.print("Welcome to toGetJob! Fill the following fields: ");

        RegisterUserBean userBean = collectUserInfo(scanner);

        Object infoBean = getUserRoleInfo(scanner, userBean.getRole());

        processRegistration(context, userBean, infoBean);
    }

    private RegisterUserBean collectUserInfo(Scanner scanner) {
        RegisterUserBean userBean = new RegisterUserBean();

        getValidInputWithValidation(scanner, "Enter username: ", userBean::setUsername);
        userBean.setPassword(collectValidPassword(scanner));
        getValidInputWithValidation(scanner, "Enter name: ", userBean::setName);
        getValidInputWithValidation(scanner, "Enter surname: ", userBean::setSurname);
        getValidInputWithValidation(scanner, "Enter email: ", userBean::setEmail);
        getValidInputWithValidation(scanner, "Enter role: ", userBean::setRole);

        return userBean;
    }

    private String collectValidPassword(Scanner scanner) {
        while (true) {
            try {
                String password = getValidInput(scanner, "Enter password: ");
                String checkPassword = getValidInput(scanner, "Confirm password: ");

                if (!password.equals(checkPassword)) {
                    Printer.print("Passwords do not match. Please try again.");
                } else {
                    return password;
                }
            } catch (InvalidPasswordException e) {
                Printer.print(ERROR + e.getMessage() + TRY);
            }
        }
    }

    private Object getUserRoleInfo(Scanner scanner, String role) {
        return STUDENT.equalsIgnoreCase(role) ? getStudentInfo(scanner) : getRecruiterInfo(scanner);
    }

    private void processRegistration(CliContext context, RegisterUserBean userBean, Object infoBean) {
        try {
            boolean registrationSuccess = registerBoundary.registerUser(userBean, infoBean);

            if (registrationSuccess) {
                Printer.print("Registration successful!");
                context.setState(STUDENT.equalsIgnoreCase(userBean.getRole()) ? new HomeStudentState() : new HomeRecruiterState());
            } else {
                Printer.print("Username already exists. Please try again.");
                context.setState(new RegisterState());
            }
        } catch (UsernameTakeException e) {
            handleRegistrationError(context, "Error: The username is already taken. Please, try again with a different username.", new MainMenuState());
        } catch (EmailAlreadyExistsException e) {
            handleRegistrationError(context, "Error: The email is already taken. Please, try again with a different email.", new MainMenuState());
        } catch (FileAccessException e) {
            handleRegistrationError(context, "Error accessing file. Please try again later.", new ExitState());
        } catch (DatabaseException e) {
            handleRegistrationError(context, e.getMessage(), new ExitState());
        } catch (Exception e) {
            handleRegistrationError(context, "An unexpected error occurred: " + e.getMessage(), new MainMenuState());
        }
    }

    private void handleRegistrationError(CliContext context, String message, State nextState) {
        Printer.print(message);
        context.setState(nextState);
    }

    private String getValidInput(Scanner scanner, String prompt) {
        Printer.print(prompt);
        return scanner.nextLine().trim();
    }

    private void getValidInputWithValidation(Scanner scanner, String prompt, Consumer<String> setter) {
        while (true) {
            try {
                String input = getValidInput(scanner, prompt);
                setter.accept(input);
                return;
            } catch (Exception e) {
                Printer.print(ERROR + e.getMessage() + TRY);
            }
        }
    }

    private StudentInfoBean getStudentInfo(Scanner scanner) {
        Printer.print("Please, complete your student profile:");

        StudentInfoBean studentInfoBean = new StudentInfoBean();

        getValidInputWithValidation(scanner, "Enter date of birth (yyyy-MM-dd): ",
                input -> studentInfoBean.setDateOfBirth(LocalDate.parse(input)));

        getValidInputWithValidation(scanner, "Enter phone number: ",
                studentInfoBean::setPhoneNumber);

        Printer.print("Enter degrees (comma-separated): ");
        studentInfoBean.setDegrees(getCommaSeparatedList(scanner));

        Printer.print("Enter courses attended (comma-separated): ");
        studentInfoBean.setCoursesAttended(getCommaSeparatedList(scanner));

        Printer.print("Enter certifications (comma-separated): ");
        studentInfoBean.setCertifications(getCommaSeparatedList(scanner));

        Printer.print("Enter work experiences (comma-separated): ");
        studentInfoBean.setWorkExperiences(getCommaSeparatedList(scanner));

        Printer.print("Enter skills (comma-separated): ");
        studentInfoBean.setSkills(getCommaSeparatedList(scanner));

        getValidInputWithValidation(scanner, "Enter availability (full-time / part-time): ",
                studentInfoBean::setAvailability);

        return studentInfoBean;
    }

    private List<String> getCommaSeparatedList(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                Printer.print("Error: Input cannot be empty. Please try again.");
                continue;
            }

            List<String> items = new ArrayList<>();
            for (String item : input.split(",")) {
                String trimmedItem = item.trim();
                if (!trimmedItem.isEmpty()) {
                    items.add(trimmedItem);
                }
            }

            if (items.isEmpty()) {
                Printer.print("Error: The list must contain at least one valid item. Please try again.");
            } else {
                return items;
            }
        }
    }

    private RecruiterInfoBean getRecruiterInfo(Scanner scanner) {
        Printer.print("Please, complete your recruiter profile:");

        RecruiterInfoBean recruiterInfoBean = new RecruiterInfoBean();
        while (true) {
            try {
                Printer.print("Enter the companies you work for (comma-separated): ");
                List<String> companies = getCommaSeparatedList(scanner);
                recruiterInfoBean.setCompanies(companies);
                break;
            } catch (InvalidCompanyListException e) {
                Printer.print(ERROR+ e.getMessage() + TRY);
            }
        }

        return recruiterInfoBean;
    }
}

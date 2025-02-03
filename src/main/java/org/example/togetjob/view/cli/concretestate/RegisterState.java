package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.bean.RecruiterInfoBean;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.boundary.RegisterBoundary;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class RegisterState implements CliState {

    private static final Logger logger = Logger.getLogger(RegisterState.class.getName());

    @Override
    public void showMenu() {
        logger.info("\n ---Register---");
    }

    @Override
    public void goNext() {
        logger.info("Registration complete, returning to main menu...");
    }

    @Override
    public void goNext(CliContext context, String input) {

        Scanner scanner = context.getScanner();

        logger.info("Welcome to toGetJob! Fill the following fields: ");
        logger.info("Enter username: ");
        String username = scanner.nextLine();
        logger.info("Enter password: ");
        String password = scanner.nextLine();
        logger.info("Confirm password: ");
        String checkPassword = scanner.nextLine();

        if (!password.equals(checkPassword)) {
            logger.warning("Passwords do not match. Please try again.");
            context.setState(new RegisterState()); // Rimani nello stato di registrazione
            return;
        }

        logger.info("Enter name: ");
        String name = scanner.nextLine();
        logger.info("Enter surname: ");
        String surname = scanner.nextLine();
        logger.info("Enter email: ");
        String email = scanner.nextLine();
        logger.info("Enter role (student/recruiter): ");
        String roleInput = scanner.nextLine().trim().toLowerCase();

        RegisterUserBean userBean = new RegisterUserBean(username, email, password, name, surname, roleInput, checkPassword);

        //polymorphism
        Object infoBean = null;

        if ("student".equals(roleInput)) {
            infoBean = getStudentInfo(scanner);
        } else if ("recruiter".equals(roleInput)) {
            infoBean = getRecruiterInfo(scanner);
        } else {
            logger.warning("Invalid role. Please try again.");
            context.setState(new RegisterState());
            return;
        }

        RegisterBoundary registerBoundary = new RegisterBoundary();
        boolean registrationSuccess = registerBoundary.registerUser(userBean, infoBean);

        if (registrationSuccess) {
            logger.info("Registration successful!");
            context.setState(new MainMenuState()); // Main Menù
        } else {
            logger.warning("Username already exists. Please try again.");
            context.setState(new RegisterState());
        }

    }

    private StudentInfoBean getStudentInfo(Scanner scanner) {
        logger.info("Please, complete your student profile:");

        logger.info("Enter date of birth (yyyy-mm-dd): ");
        LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine());
        logger.info("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        logger.info("Enter degrees (comma-separated): ");
        List<String> degrees = List.of(scanner.nextLine().split(","));
        logger.info("Enter courses attended (comma-separated): ");
        List<String> courses = List.of(scanner.nextLine().split(","));
        logger.info("Enter certifications (comma-separated): ");
        List<String> certifications = List.of(scanner.nextLine().split(","));
        logger.info("Enter work experiences (comma-separated): ");
        List<String> workExperiences = List.of(scanner.nextLine().split(","));
        logger.info("Enter skills (comma-separated): ");
        List<String> skills = List.of(scanner.nextLine().split(","));
        logger.info("Enter availability: ");
        String availability = scanner.nextLine();

        return new StudentInfoBean(dateOfBirth, phoneNumber, degrees, courses, certifications, workExperiences, skills, availability);
    }

    private RecruiterInfoBean getRecruiterInfo(Scanner scanner) {

        logger.info("Please, complete your recruiter profile:");

        logger.info("Enter the companies you work for (comma-separated): ");
        List<String> companies = List.of(scanner.nextLine().split(","));
        return new RecruiterInfoBean(companies);
    }

}

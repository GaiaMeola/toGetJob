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

public class RegisterState implements CliState {

    @Override
    public void showMenu() {

        System.out.println("\n ---Register---");

    }

    @Override
    public void goNext() {
        System.out.println("Registration complete, returning to main menu...");
    }

    @Override
    public void goNext(CliContext context, String input) {

        Scanner scanner = context.getScanner();

        System.out.println("Welcome to toGetJob! Fill the following fields: ");
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Confirm password: ");
        String checkPassword = scanner.nextLine();

        if (!password.equals(checkPassword)) {
            System.out.println("Passwords do not match. Please try again.");
            context.setState(new RegisterState()); // Rimani nello stato di registrazione
            return;
        }

        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter surname: ");
        String surname = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter role (student/recruiter): ");
        String roleInput = scanner.nextLine().trim().toLowerCase();

        RegisterUserBean userBean = new RegisterUserBean(username, email, password, name, surname, roleInput, checkPassword);

        //polymorphism
        Object infoBean = null;

        if ("student".equals(roleInput)) {
            infoBean = getStudentInfo(scanner);
        } else if ("recruiter".equals(roleInput)) {
            infoBean = getRecruiterInfo(scanner);
        } else {
            System.out.println("Invalid role. Please try again.");
            context.setState(new RegisterState());
            return;
        }

        RegisterBoundary registerBoundary = new RegisterBoundary();
        boolean registrationSuccess = registerBoundary.registerUser(userBean, infoBean);

        if (registrationSuccess) {
            System.out.println("Registration successful!");
            context.setState(new MainMenuState()); // Main Menù
        } else {
            System.out.println("Username already exists. Please try again.");
            context.setState(new RegisterState());
        }

    }

    private StudentInfoBean getStudentInfo(Scanner scanner) {
        System.out.println("Please, complete your student profile:");

        System.out.print("Enter date of birth (yyyy-mm-dd): ");
        LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter degrees (comma-separated): ");
        List<String> degrees = List.of(scanner.nextLine().split(","));
        System.out.print("Enter courses attended (comma-separated): ");
        List<String> courses = List.of(scanner.nextLine().split(","));
        System.out.print("Enter certifications (comma-separated): ");
        List<String> certifications = List.of(scanner.nextLine().split(","));
        System.out.print("Enter work experiences (comma-separated): ");
        List<String> workExperiences = List.of(scanner.nextLine().split(","));
        System.out.print("Enter skills (comma-separated): ");
        List<String> skills = List.of(scanner.nextLine().split(","));
        System.out.print("Enter availability: ");
        String availability = scanner.nextLine();

        return new StudentInfoBean(dateOfBirth, phoneNumber, degrees, courses, certifications, workExperiences, skills, availability);
    }

    private RecruiterInfoBean getRecruiterInfo(Scanner scanner) {

        System.out.println("Please, complete your recruiter profile:");

        System.out.print("Enter the companies you work for (comma-separated): ");
        List<String> companies = List.of(scanner.nextLine().split(","));
        return new RecruiterInfoBean(companies);
    }

}

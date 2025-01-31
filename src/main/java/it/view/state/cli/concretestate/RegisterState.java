package it.view.state.cli.concretestate;

import it.bean.RecruiterInfoBean;
import it.bean.RegisterUserBean;
import it.bean.StudentInfoBean;
import it.controller.registration.AbstractRegisterController;
import it.controller.registration.RegisterRecruiterController;
import it.controller.registration.RegisterStudentController;
import it.view.state.cli.abstractstate.CliState;
import it.view.state.cli.contextstate.CliContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class RegisterState implements CliState {

    @Override
    public void showMenu() {

        System.out.println("\n ---Register---");
        System.out.println("Please enter the following details to register:");
        System.out.println("1. Username");
        System.out.println("2. Password");
        System.out.println("3. Name");
        System.out.println("4. Surname");
        System.out.println("5. Email");
        System.out.println("6. Role (student/recruiter)");
        System.out.println("To submit, type 'submit'.");

    }

    @Override
    public void goNext() {
        System.out.println("Registration complete, returning to main menu...");
    }

    @Override
    public void goNext(CliContext context, String input) {

        Scanner scanner = context.getScanner();

        if (!"submit".equalsIgnoreCase(input)) {
            context.setState(new MainMenuState()); // go to Main Menu
            return;
        }

        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();
        System.out.print("Confirm password: ");
        String checkPassword = scanner.nextLine();
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter surname: ");
        String surname = scanner.nextLine();
        System.out.print("Enter email: ");
        String email = scanner.nextLine();
        System.out.print("Enter role (student/recruiter): ");
        String roleInput = scanner.nextLine().trim().toLowerCase();

        RegisterUserBean userBean = new RegisterUserBean(username, email, password, name, surname, roleInput, checkPassword);
        AbstractRegisterController registerController;

        //polymorphism
        Object infoBean = null;

        if ("student".equals(roleInput)) {
            infoBean = getStudentInfo(scanner);
            registerController = new RegisterStudentController((StudentInfoBean) infoBean);
        } else if ("recruiter".equals(roleInput)) {
            infoBean = getRecruiterInfo(scanner);
            registerController = new RegisterRecruiterController((RecruiterInfoBean) infoBean);
        } else {
            System.out.println("Invalid role. Please try again.");
            context.setState(new RegisterState());
            return;
        }

        boolean registrationSuccess = registerController.registerUser(userBean);

        if (registrationSuccess) {
            System.out.println("Registration successful!");
            context.setState(new MainMenuState()); // Passa al menu principale
        } else {
            System.out.println("Username already exists. Please try again.");
            context.setState(new RegisterState());
        }

    }

    private StudentInfoBean getStudentInfo(Scanner scanner) {
        System.out.println("Welcome! Please complete your student profile:");

        System.out.print("Enter your date of birth (yyyy-mm-dd): ");
        LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter your phone number: ");
        String phoneNumber = scanner.nextLine();
        System.out.print("Enter your degrees (comma-separated): ");
        List<String> degrees = List.of(scanner.nextLine().split(","));
        System.out.print("Enter courses attended (comma-separated): ");
        List<String> courses = List.of(scanner.nextLine().split(","));
        System.out.print("Enter certifications (comma-separated): ");
        List<String> certifications = List.of(scanner.nextLine().split(","));
        System.out.print("Enter work experiences (comma-separated): ");
        List<String> workExperiences = List.of(scanner.nextLine().split(","));
        System.out.print("Enter your skills (comma-separated): ");
        List<String> skills = List.of(scanner.nextLine().split(","));
        System.out.print("Enter your availability: ");
        String availability = scanner.nextLine();

        return new StudentInfoBean(dateOfBirth, phoneNumber, degrees, courses, certifications, workExperiences, skills, availability);
    }

    private RecruiterInfoBean getRecruiterInfo(Scanner scanner) {

        System.out.println("Welcome! Please complete your recruiter profile:");

        System.out.print("Enter the companies you work for (comma-separated): ");
        List<String> companies = List.of(scanner.nextLine().split(","));
        return new RecruiterInfoBean(companies);
    }

}

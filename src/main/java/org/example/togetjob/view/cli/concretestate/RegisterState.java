package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.bean.RecruiterInfoBean;
import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.bean.StudentInfoBean;
import org.example.togetjob.view.boundary.RegisterBoundary;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class RegisterState implements CliState {

    @Override
    public void showMenu() {

        Printer.print("\n ---Register---");

    }

    @Override
    public void goNext(CliContext context, String input) {

        Scanner scanner = context.getScanner();

        Printer.print("Welcome to toGetJob! Fill the following fields: ");
        Printer.print("Enter username: ");
        String username = scanner.nextLine();
        Printer.print("Enter password: ");
        String password = scanner.nextLine();
        Printer.print("Confirm password: ");
        String checkPassword = scanner.nextLine();

        if (!password.equals(checkPassword)) {
            Printer.print("Passwords do not match. Please try again.");
            context.setState(new RegisterState());
            return;
        }

        Printer.print("Enter name: ");
        String name = scanner.nextLine();
        Printer.print("Enter surname: ");
        String surname = scanner.nextLine();
        Printer.print("Enter email: ");
        String email = scanner.nextLine();
        Printer.print("Enter role (student/recruiter): ");
        String roleInput = scanner.nextLine().trim().toLowerCase();

        RegisterUserBean userBean = new RegisterUserBean(); // Empty

        userBean.setUsername(username);
        userBean.setPassword(password);
        userBean.setName(name);
        userBean.setSurname(surname);
        userBean.setEmail(email);
        userBean.setRoleInput(roleInput);

        //polymorphism
        Object infoBean;

        if ("student".equals(roleInput)) {
            infoBean = getStudentInfo(scanner);
        } else if ("recruiter".equals(roleInput)) {
            infoBean = getRecruiterInfo(scanner);
        } else {
            Printer.print("Invalid role. Please try again.");
            context.setState(new RegisterState());
            return;
        }

        RegisterBoundary registerBoundary = new RegisterBoundary();
        boolean registrationSuccess = registerBoundary.registerUser(userBean, infoBean);

        if (registrationSuccess) {
            Printer.print("Registration successful!");
            context.setState(new MainMenuState()); // Main Menù
        } else {
            Printer.print("Username already exists. Please try again.");
            context.setState(new MainMenuState());
        }

    }

    private StudentInfoBean getStudentInfo(Scanner scanner) {
        Printer.print("Please, complete your student profile:");

        Printer.print("Enter date of birth (yyyy-mm-dd): ");
        LocalDate dateOfBirth = LocalDate.parse(scanner.nextLine());
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

    private RecruiterInfoBean getRecruiterInfo(Scanner scanner) {
        Printer.print("Please, complete your recruiter profile:");

        Printer.print("Enter the companies you work for (comma-separated): ");
        List<String> companies = List.of(scanner.nextLine().split(","));

        RecruiterInfoBean recruiterInfoBean = new RecruiterInfoBean();
        recruiterInfoBean.setCompanies(companies);

        return recruiterInfoBean;
    }

}

package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.boundary.LoginBoundary;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.util.Scanner;

public class LoginState implements CliState {

    private final LoginBoundary loginBoundary = new LoginBoundary();

    @Override
    public void showMenu() {
        System.out.println("\n--- Login ---");
    }

    @Override
    public void goNext() {

    }

    @Override
    public void goNext(CliContext context, String input) {

            Scanner scanner = context.getScanner();

            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password= scanner.nextLine();

            boolean loginSuccess = loginBoundary.login(username, password);

            if (loginSuccess) {
                // Login ok
                String userRole = loginBoundary.getUserRole();
                System.out.println("Login successful!");

                if ("STUDENT".equalsIgnoreCase(userRole)) {
                    context.setState(new HomeStudentState());  // go to HomePageStudent
                } else if ("RECRUITER".equalsIgnoreCase(userRole)) {
                    context.setState(new HomeRecruiterState());  // go to HomePageRecruiter
                } else {
                    System.out.println("Unknown role. Please try again.");
                    context.setState(new LoginState());  // Goto Login
                }
            } else {
                System.out.println("Login failed. Please try again.");
                context.setState(new LoginState());  // Rimani nello stato di login
            }
    }
}

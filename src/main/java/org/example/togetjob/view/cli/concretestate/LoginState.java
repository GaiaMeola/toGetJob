package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.view.boundary.LoginBoundary;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.util.Scanner;

public class LoginState implements CliState {

    private final LoginBoundary loginBoundary = new LoginBoundary();

    @Override
    public void showMenu() {
        Printer.print("\n--- Login ---");
    }

    @Override
    public void goNext(CliContext context, String input) {

            Scanner scanner = context.getScanner();

            Printer.print("Enter username: ");
            String username = scanner.nextLine();
            Printer.print("Enter password: ");
            String password= scanner.nextLine();

            boolean loginSuccess = loginBoundary.login(username, password);

            if (loginSuccess) {
                // Login ok
                String userRole = loginBoundary.getUserRole();
                Printer.print("Login successful!");

                if ("Student".equalsIgnoreCase(userRole)) {
                    context.setState(new HomeStudentState());  // go to HomePageStudent
                } else if ("Recruiter".equalsIgnoreCase(userRole)) {
                    context.setState(new HomeRecruiterState());  // go to HomePageRecruiter
                } else {
                    Printer.print("Unknown role. Please try again.");
                    context.setState(new MainMenuState());  // Goto Login
                }
            } else {
                Printer.print("Login failed. Please try again.");
                context.setState(new MainMenuState());  //Login
            }
    }
}

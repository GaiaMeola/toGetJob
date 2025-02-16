package org.example.togetjob.view.cli;

import org.example.togetjob.exceptions.UserNotFoundException;
import org.example.togetjob.exceptions.WrongPasswordException;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.State;
import org.example.togetjob.view.boundary.LoginBoundary;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.CliContext;

import java.util.Scanner;

public class LoginState implements State {

    private final LoginBoundary loginBoundary = new LoginBoundary();

    @Override
    public void showMenu() {
        Printer.print("\n--- Login ---");
    }

    @Override
    public void goNext(Context contextState, String input) {

            CliContext context = (CliContext) contextState;

            Scanner scanner = context.getScanner();

            Printer.print("Enter username: ");
            String username = scanner.nextLine();
            Printer.print("Enter password: ");
            String password= scanner.nextLine();

        try {
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
                context.setState(new MainMenuState());  // Login
            }

        } catch (UserNotFoundException e) {
            Printer.print("Error: Username not found. Would you like to register?");
            // Optionally, redirect to a registration state
            Printer.print("Type 'yes' to register or 'no' to return to main menu.");

            String response = scanner.nextLine().trim().toLowerCase();
            if ("yes".equals(response)) {
                context.setState(new RegisterState());  // Redirect to register page
            } else {
                context.setState(new MainMenuState());  // Return to main menu
            }
        } catch (WrongPasswordException e) {
            Printer.print("Error: Incorrect password. Please try again.");
            context.setState(new MainMenuState());  // Retry login
        } catch (Exception e) {
            Printer.print("An unexpected error occurred: " + e.getMessage());
            context.setState(new MainMenuState());  // Retry login
        }
    }
}

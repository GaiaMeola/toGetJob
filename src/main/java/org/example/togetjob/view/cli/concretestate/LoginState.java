package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.boundary.LoginBoundary;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.util.Scanner;
import java.util.logging.Logger;

public class LoginState implements CliState {

    private static final Logger logger = Logger.getLogger(LoginState.class.getName());
    private final LoginBoundary loginBoundary = new LoginBoundary();

    @Override
    public void showMenu() {
        logger.info("\n--- Login ---");
    }

    @Override
    public void goNext() {
        // Non implementato
    }

    @Override
    public void goNext(CliContext context, String input) {

        Scanner scanner = context.getScanner();

        logger.info("Enter username: ");
        String username = scanner.nextLine();
        logger.info("Enter password: ");
        String password = scanner.nextLine();

        boolean loginSuccess = loginBoundary.login(username, password);

        if (loginSuccess) {
            // Login ok
            String userRole = loginBoundary.getUserRole();
            logger.info("Login successful!");

            if ("STUDENT".equalsIgnoreCase(userRole)) {
                context.setState(new HomeStudentState());  // go to HomePageStudent
            } else if ("RECRUITER".equalsIgnoreCase(userRole)) {
                context.setState(new HomeRecruiterState());  // go to HomePageRecruiter
            } else {
                logger.warning("Unknown role. Please try again.");
                context.setState(new MainMenuState());  // Goto Login
            }
        } else {
            logger.warning("Login failed. Please try again.");
            context.setState(new MainMenuState());  // Login
        }
    }
}

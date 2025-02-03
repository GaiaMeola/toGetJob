package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.boundary.LoginBoundary;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.util.Scanner;
import java.util.logging.Logger;

public class HomeRecruiterState implements CliState {

    private static final Logger logger = Logger.getLogger(HomeRecruiterState.class.getName());
    private final LoginBoundary loginBoundary = new LoginBoundary();

    @Override
    public void showMenu() {
        logger.info("\n ---Home - Recruiter---");
        logger.info("Welcome, Recruiter! You can do the following:");
        logger.info("1. View your profile");
        logger.info("2. Publish a Job Announcement");
        logger.info("3. Contact a Job Candidate");
        logger.info("4. View reviews");
        logger.info("5. Invite another recruiter to collaborate");
        logger.info("6. Logout");
        logger.info("7. Exit");
        logger.info("Choose an option: ");
    }

    @Override
    public void goNext() {
        // Non implementato
    }

    @Override
    public void goNext(CliContext context, String input) {

        Scanner scanner = context.getScanner();

        switch (input) {
            case "1":
                logger.info("Viewing your profile...");
                // Logica per visualizzare il profilo
                break;
            case "2":
                logger.info("Publishing a job announcement...");
                context.setState(new JobAnnouncementRecruiterState());
                break;
            case "3":
                logger.info("Contacting a job candidate...");
                // Logica per contattare un candidato
                break;
            case "4":
                logger.info("Viewing reviews...");
                // Logica per visualizzare le recensioni
                break;
            case "5":
                logger.info("Enter the email of the recruiter you want to invite: ");
                String recruiterEmail = scanner.nextLine();
                // Logica per inviare l'invito al recruiter
                break;
            case "6":
            case "logout":
                logger.info("Logging out...");
                loginBoundary.logout();
                context.setState(new MainMenuState());  // Torna al menu principale
                break;
            case "7":
            case "exit":
                logger.info("Exiting application...");
                context.setState(new ExitState());  // Chiudi l'applicazione (ExitState)
                break;
            default:
                logger.warning("Invalid option. Please try again.");
                context.setState(new HomeRecruiterState()); // Rimane nello stato corrente
                break;
        }
    }
}

package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.boundary.LoginBoundary;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.util.Scanner;
import java.util.logging.Logger;

public class HomeStudentState implements CliState {

    private static final Logger logger = Logger.getLogger(HomeStudentState.class.getName());
    private final LoginBoundary loginBoundary = new LoginBoundary();

    @Override
    public void showMenu() {
        logger.info("\n ---Home - Student---");
        logger.info("Welcome, Student! You can do the following:");
        logger.info("1. View your profile");
        logger.info("2. Vote a company");
        logger.info("3. Show Job Announcements");
        logger.info("4. View notifications");
        logger.info("5. Logout");
        logger.info("6. Exit");
        logger.info("Choose an option: ");
    }

    @Override
    public void goNext() {
        // Non implementato
    }

    @Override
    public void goNext(CliContext context, String input) {

        Scanner scanner = context.getScanner();

        switch (input.toLowerCase()) {
            case "1": // View profile
                logger.info("Displaying your profile...");
                // Qui inserisci il codice per visualizzare il profilo dello studente
                break;
            case "2": // Vote a company
                logger.info("Voting a company...");
                // Qui inserisci il codice per votare un'azienda
                break;
            case "3": // Show job announcements
                logger.info("Showing job announcements...");
                // Qui inserisci il codice per mostrare gli annunci di lavoro
                break;
            case "4": // View notifications
                logger.info("Viewing notifications...");
                // Qui inserisci il codice per visualizzare le notifiche
                break;
            case "5": // Logout
            case "logout":
                logger.info("Logging out...");
                loginBoundary.logout();
                context.setState(new MainMenuState()); // Torna al menu principale
                break;
            case "6": // Exit
            case "exit":
                logger.info("Exiting application...");
                context.setState(new ExitState());  // Esce dall'applicazione (ExitState)
                break;
            default:
                logger.warning("Invalid option. Please try again.");
                context.setState(new HomeStudentState()); // Rimane nello stato corrente
                break;
        }
    }
}

package it.view.cli.concretestate;

import it.boundary.LoginBoundary;
import it.view.cli.abstractstate.CliState;
import it.view.cli.contextstate.CliContext;
import session.SessionManager;

import java.util.Scanner;

public class HomeRecruiterState implements CliState {

    private final LoginBoundary loginBoundary = new LoginBoundary();

    @Override
    public void showMenu() {
        System.out.println("\n ---Home - Recruiter---");
        System.out.println("Welcome, Recruiter! You can do the following:");
        System.out.println("1. View your profile");
        System.out.println("2. Publish a Job Announcement");
        System.out.println("3. Contact a Job Candidate");
        System.out.println("4. View reviews");
        System.out.println("5. Invite another recruiter to collaborate");
        System.out.println("6. Logout");
        System.out.println("7. Exit");
        System.out.print("Choose an option: ");
    }

    @Override
    public void goNext() {

    }

    @Override
    public void goNext(CliContext context, String input) {

        Scanner scanner = context.getScanner();

        switch (input) {
            case "1":
                System.out.println("Viewing your profile...");
                // Logica per visualizzare il profilo
                break;
            case "2":
                System.out.println("Publishing a job announcement...");
                // Logica per pubblicare un annuncio di lavoro
                break;
            case "3":
                System.out.println("Contacting a job candidate...");
                // Logica per contattare un candidato
                break;
            case "4":
                System.out.println("Viewing reviews...");
                // Logica per visualizzare le recensioni
                break;
            case "5":
                // Opzione per invitare un altro recruiter
                System.out.println("Enter the email of the recruiter you want to invite: ");
                String recruiterEmail = scanner.nextLine();
                // Logica per inviare l'invito al recruiter
                break;
            case "6":
            case "logout":
                System.out.println("Logging out...");
                loginBoundary.logout();
                context.setState(new MainMenuState());  // Torna al menu principale
                break;
            case "7":
            case "exit":
                System.out.println("Exiting application...");
                context.setState(new ExitState());  // Chiudi l'applicazione (ExitState)
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                context.setState(new HomeRecruiterState()); // Rimane nello stato corrente
                break;
        }
    }
}

package it.view.state.cli.concretestate;

import it.view.state.cli.abstractstate.CliState;
import it.view.state.cli.contextstate.CliContext;

import java.util.Scanner;

public class HomeRecruiterState implements CliState {
    @Override
    public void showMenu() {
        System.out.println("\n ---Home - Recruiter---");
        System.out.println("Welcome, Recruiter! You can do the following:");
        System.out.println("1. Publish a job announcement");
        System.out.println("2. Contact a job candidate");
        System.out.println("3. View reviews");
        System.out.println("4. Invite another recruiter to collaborate");
        System.out.println("5. Logout");
        System.out.println("6. Exit the application");
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
                System.out.println("Publishing a job announcement...");
                // Logica per pubblicare un annuncio di lavoro
                break;
            case "2":
                System.out.println("Contacting a job candidate...");
                // Logica per contattare un candidato
                break;
            case "3":
                System.out.println("Viewing reviews...");
                // Logica per visualizzare le recensioni
                break;
            case "4":
                // Nuova opzione per invitare un altro recruiter
                System.out.println("Enter the email of the recruiter you want to invite: ");
                String recruiterEmail = scanner.nextLine();
                // Logica per inviare l'invito al recruiter (ad esempio, invio dell'email)
                break;
            case "5":
            case "logout":
                System.out.println("Logging out...");
                context.setState(new MainMenuState());  // Torna al menu principale
                break;
            case "6":
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

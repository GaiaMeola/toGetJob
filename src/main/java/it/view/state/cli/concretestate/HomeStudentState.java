package it.view.state.cli.concretestate;

import it.view.state.cli.abstractstate.CliState;
import it.view.state.cli.contextstate.CliContext;

import java.util.Scanner;

public class HomeStudentState implements CliState {
    @Override
    public void showMenu() {
            System.out.println("\n ---Home - Student---");
            System.out.println("Welcome, Student! You can do the following:");
            System.out.println("1. View your profile");
            System.out.println("2. Vote a company");
            System.out.println("3. Show job announcements");
            System.out.println("4. View notifications");
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

        switch (input.toLowerCase()) {
            case "1": // View profile
                System.out.println("Displaying your profile...");
                // Qui inserisci il codice per visualizzare il profilo dello studente
                break;
            case "2": // Vote a company
                System.out.println("Voting a company...");
                // Qui inserisci il codice per votare un'azienda
                break;
            case "3": // Show job announcements
                System.out.println("Showing job announcements...");
                // Qui inserisci il codice per mostrare gli annunci di lavoro
                break;
            case "4": // View notifications
                System.out.println("Viewing notifications...");
                // Qui inserisci il codice per visualizzare le notifiche
                break;
            case "5": // Logout
            case "logout":
                System.out.println("Logging out...");
                context.setState(new MainMenuState());  // Torna al menu principale
                break;
            case "6": // Exit
            case "exit":
                System.out.println("Exiting application...");
                context.setState(new ExitState());  // Esce dall'applicazione (ExitState)
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                context.setState(new HomeStudentState()); // Rimane nello stato corrente
                break;
        }
    }
}

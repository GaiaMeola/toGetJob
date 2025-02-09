package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.view.boundary.LoginBoundary;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;


public class HomeStudentState implements CliState {

    private final LoginBoundary loginBoundary = new LoginBoundary();

    @Override
    public void showMenu() {
            Printer.print("\n ---Home - Student---");
            Printer.print("Welcome, Student! You can do the following:");
            Printer.print("1. View your profile");
            Printer.print("2. Vote a company");
            Printer.print("3. Show Job Announcements");
            Printer.print("4. View notifications");
            Printer.print("5. Logout");
            Printer.print("6. Exit");
            Printer.print("Choose an option: ");
    }

    @Override
    public void goNext(CliContext context, String input) {

        switch (input.toLowerCase()) {
            case "1": // View profile
                Printer.print("Displaying your profile...");
                break;
            case "2": // Vote a company
                Printer.print("Voting a company...");
                break;
            case "3": // Show job announcements
                Printer.print("Showing job announcements...");
                context.setState(new SendAJobApplicationStudentState());
                break;
            case "4": // View notifications
                Printer.print("Viewing notifications...");
                break;
            case "5": // Logout
                Printer.print("Logging out...");
                loginBoundary.logout();
                context.setState(new MainMenuState());// Go to Main Menù
                break;
            case "6": // Exit
                Printer.print("Exiting application...");
                context.setState(new ExitState());  // Go to Exit State
                break;
            default:
                Printer.print("Invalid option. Please try again.");
                context.setState(new HomeStudentState()); // Home Student
                break;
        }
    }
}

package org.example.togetjob.view.cli;

import org.example.togetjob.model.entity.User;
import org.example.togetjob.session.SessionManager;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.State;
import org.example.togetjob.view.boundary.LoginBoundary;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.CliContext;


public class HomeStudentState implements State{

    private final LoginBoundary loginBoundary = new LoginBoundary();

    @Override
    public void showMenu() {
            Printer.print("\n ---Home - Student---");
            Printer.print("Welcome, Student! You can do the following:");
            Printer.print("1. View your profile");
            Printer.print("2. Vote a company");
            Printer.print("3. Send a job application");
            Printer.print("4. View notifications");
            Printer.print("5. Logout");
            Printer.print("6. Exit");
            Printer.print("Choose an option: ");
    }

    @Override
    public void goNext(Context contextState, String input) {

        CliContext context = (CliContext) contextState;

        try {
            switch (input.toLowerCase()) {
                case "1": // View profile
                    User currentUser = SessionManager.getInstance().getCurrentUser();
                    if (currentUser != null) {
                        currentUser.introduce();
                    } else {
                        Printer.print("Error: No user is currently logged in.");
                    }
                    context.setState(new HomeStudentState());
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
                    //** not implemented **//
                    break;
                case "5": // Logout
                    Printer.print("Logging out...");
                    loginBoundary.logout();
                    context.setState(new MainMenuState()); // Go to Main Menu
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
        } catch (IllegalStateException e) {
            Printer.print("An error occurred: " + e.getMessage());
            context.setState(new MainMenuState()); // Reset to main menu if there's an illegal state
        }
    }
}

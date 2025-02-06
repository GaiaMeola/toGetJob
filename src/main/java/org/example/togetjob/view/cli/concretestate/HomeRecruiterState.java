package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.boundary.LoginBoundary;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.util.Scanner;

public class HomeRecruiterState implements CliState {

    private final LoginBoundary loginBoundary = new LoginBoundary();

    @Override
    public void showMenu() {
        Printer.print("\n ---Home - Recruiter---");
        Printer.print("Welcome, Recruiter! You can do the following:");
        Printer.print("1. View your profile");
        Printer.print("2. Publish a Job Announcement");
        Printer.print("3. Contact a Job Candidate");
        Printer.print("4. View reviews");
        Printer.print("5. Invite another recruiter to collaborate");
        Printer.print("6. Logout");
        Printer.print("7. Exit");
        Printer.print("Choose an option: ");
    }

    @Override
    public void goNext(CliContext context, String input) {

        Scanner scanner = context.getScanner();

        switch (input) {
            case "1":
                Printer.print("Viewing your profile...");
                break;
            case "2":
                Printer.print("Publishing a job announcement...");
                context.setState(new PublishAJobAnnouncementRecruiterState());
                break;
            case "3":
                Printer.print("Contacting a job candidate...");
                break;
            case "4":
                Printer.print("Viewing reviews...");
                break;
            case "5":
                Printer.print("Enter the email of the recruiter you want to invite: ");
                scanner.nextLine(); //Email
                break;
            case "6":
            case "logout":
                Printer.print("Logging out...");
                loginBoundary.logout();
                context.setState(new MainMenuState());
                break;
            case "7":
            case "exit":
                Printer.print("Exiting application...");
                context.setState(new ExitState());  // (ExitState)
                break;
            default:
                Printer.print("Invalid option. Please try again.");
                context.setState(new HomeRecruiterState());
                break;
        }
    }
}

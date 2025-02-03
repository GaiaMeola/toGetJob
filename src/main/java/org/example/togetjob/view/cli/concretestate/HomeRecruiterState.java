package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.boundary.LoginBoundary;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

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
                break;
            case "2":
                System.out.println("Publishing a job announcement...");
                context.setState(new JobAnnouncementRecruiterState());
                break;
            case "3":
                System.out.println("Contacting a job candidate...");
                break;
            case "4":
                System.out.println("Viewing reviews...");
                break;
            case "5":
                System.out.println("Enter the email of the recruiter you want to invite: ");
                scanner.nextLine(); //Email
                break;
            case "6":
            case "logout":
                System.out.println("Logging out...");
                loginBoundary.logout();
                context.setState(new MainMenuState());
                break;
            case "7":
            case "exit":
                System.out.println("Exiting application...");
                context.setState(new ExitState());  // (ExitState)
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                context.setState(new HomeRecruiterState());
                break;
        }
    }
}

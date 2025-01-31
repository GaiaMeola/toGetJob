package it.view.cli.concretestate;

import it.bean.LoginUserBean;
import it.boundary.LoginBoundary;
import it.controller.LoginController;
import it.model.entity.User;
import it.view.cli.abstractstate.CliState;
import it.view.cli.contextstate.CliContext;
import session.SessionManager;

import java.util.Scanner;

public class LoginState implements CliState {

    private final LoginBoundary loginBoundary = new LoginBoundary();

    @Override
    public void showMenu() {
        System.out.println("\n--- Login ---");
        System.out.println("To log, type 'login'.");
    }

    @Override
    public void goNext() {

    }

    @Override
    public void goNext(CliContext context, String input) {

        Scanner scanner = context.getScanner();

        if("login".equalsIgnoreCase(input)){
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password= scanner.nextLine();

            boolean loginSuccess = loginBoundary.login(username, password);

            if (loginSuccess) {
                // Login ok
                String userRole = loginBoundary.getUserRole();
                System.out.println("Login successful!");


                if ("STUDENT".equalsIgnoreCase(userRole)) {
                    context.setState(new HomeStudentState());  // go to HomePageStudent
                } else if ("RECRUITER".equalsIgnoreCase(userRole)) {
                    context.setState(new HomeRecruiterState());  // go to HomePageRecruiter
                } else {
                    System.out.println("Unknown role. Please try again.");
                    context.setState(new LoginState());  // Goto Login
                }
            } else {
                // Login fallito, chiediamo di riprovare
                System.out.println("Login failed. Please try again.");
                context.setState(new LoginState());  // Rimani nello stato di login
            }
        }
    }
}

package it.view.state.cli.concretestate;

import it.bean.LoginUserBean;
import it.controller.LoginController;
import it.view.state.cli.abstractstate.CliState;
import it.view.state.cli.contextstate.CliContext;

import java.util.Scanner;

public class LoginState implements CliState {
    @Override
    public void showMenu() {
        System.out.println("\n--- Login ---");
        System.out.println("Please enter the following details to register:");
        System.out.println("1. Username");
        System.out.println("2. Email");
        System.out.println("3. Password");
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
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            System.out.print("Enter password: ");
            String password= scanner.nextLine();

            LoginUserBean userBean = new LoginUserBean(username, email, password);
            LoginController loginController = new LoginController();

            boolean loginSuccessful = loginController.loginUser(userBean);

            if (loginSuccessful) {
                // Login ok
                String userRole = loginController.getUserRole(userBean);
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

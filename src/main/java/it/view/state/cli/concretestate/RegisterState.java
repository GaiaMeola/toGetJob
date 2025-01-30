package it.view.state.cli.concretestate;

import it.bean.RegisterUserBean;
import it.controller.RegisterController;
import it.view.state.cli.abstractstate.CliState;
import it.view.state.cli.contextstate.CliContext;

import java.util.Scanner;

public class RegisterState implements CliState {

    @Override
    public void showMenu() {

        System.out.println("\n ---Register---");
        System.out.println("Please enter the following details to register:");
        System.out.println("1. Username");
        System.out.println("2. Password");
        System.out.println("3. Name");
        System.out.println("4. Surname");
        System.out.println("5. Email");
        System.out.println("6. Role (student/recruiter)");
        System.out.println("To submit, type 'submit'.");

    }

    @Override
    public void goNext() {
        System.out.println("Registration complete, returning to main menu...");
    }

    @Override
    public void goNext(CliContext context, String input) {

        Scanner scanner = context.getScanner();

        if("submit".equalsIgnoreCase(input)) {
            System.out.println("Submitting registration...");

            // Chiediamo i dati dell'utente
            System.out.print("Enter username: ");
            String username = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();
            System.out.print("Confirm password: ");  // Aggiungiamo la conferma della password
            String checkpassword = scanner.nextLine();  // Memorizziamo la conferma della password
            System.out.print("Enter name: ");
            String name = scanner.nextLine();
            System.out.print("Enter surname: ");
            String surname = scanner.nextLine();
            System.out.print("Enter email: ");
            String email = scanner.nextLine();
            System.out.print("Enter role (student/recruiter): ");
            String roleInput = scanner.nextLine();

            RegisterUserBean userBean = new RegisterUserBean(username, email, password, name, surname, roleInput, checkpassword);
            RegisterController registerController = new RegisterController();
            boolean registrationSuccess = registerController.registerUser(userBean);

            if (registrationSuccess) {
                System.out.println("Registration successful!");
                context.setState(new MainMenuState()); // Passa a un altro stato, per esempio LoginState
            } else {
                System.out.println("Username already exists. Please try again.");
                context.setState(new RegisterState());
            }

        } else {
            context.setState(new MainMenuState());
        }

    }
}

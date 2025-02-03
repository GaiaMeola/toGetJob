package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.view.cli.contextstate.CliContext;
import org.example.togetjob.view.cli.abstractstate.CliState;

public class MainMenuState implements CliState {

    @Override
    public void showMenu() {
        System.out.println("--- Welcome to toGetJob ---");
        System.out.println("1. Login");
        System.out.println("2. Register ");
        System.out.println("3. Exit ");
        System.out.print("Choose an option: ");
    }

    @Override
    public void goNext() {

    }

    @Override
    public void goNext(CliContext context, String input) {

        switch (input) { // View Login
            case "1":
                context.setState(new LoginState());
                break;
            case "2": // View Register
                context.setState(new RegisterState());
                break;
            case "3": //Exit
                context.setState(new ExitState());
                break;
            default:
                System.out.println("Invalid choice. Please, try again.");
                context.setState(new MainMenuState());
        }


    }
}

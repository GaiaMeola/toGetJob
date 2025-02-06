package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.cli.contextstate.CliContext;
import org.example.togetjob.view.cli.abstractstate.CliState;

public class MainMenuState implements CliState {

    @Override
    public void showMenu() {
        Printer.print("--- Welcome to toGetJob ---");
        Printer.print("1. Login");
        Printer.print("2. Register ");
        Printer.print("3. Exit ");
        Printer.print("Choose an option: ");
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
                Printer.print("Invalid choice. Please, try again.");
                context.setState(new MainMenuState());
        }


    }
}

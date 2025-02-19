package org.example.togetjob.view.cli;

import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.State;
import org.example.togetjob.state.CliContext;

public class MainMenuState implements State{

    @Override
    public void showMenu() {
        Printer.print("--- Welcome to toGetJob ---");
        Printer.print("1. Login");
        Printer.print("2. Register ");
        Printer.print("3. Exit ");
        Printer.print("Choose an option: ");
    }

    @Override
    public void goNext(Context contextState, String input) {

        CliContext context = (CliContext) contextState;

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

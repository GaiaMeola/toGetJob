package it.view.state.cli.concretestate;

import it.view.state.cli.contextstate.CliContext;
import it.view.state.cli.abstractstate.CliState;

public class MainMenuState implements CliState {

    @Override
    public void showMenu() {

        System.out.println("--- Main Menu ---");
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

        switch (input) {
            case "1":
                context.setState(new LoginState());
                break;
            case "2":
                context.setState(new RegisterState());
                break;
            case "3":
                context.setState(new ExitState());
                break;
            default:
                System.out.println("Invalid choice. Please, try again.");
                context.setState(new MainMenuState());
        }


    }
}

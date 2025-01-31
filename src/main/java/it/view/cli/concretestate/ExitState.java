package it.view.cli.concretestate;

import it.view.cli.abstractstate.CliState;
import it.view.cli.contextstate.CliContext;

public class ExitState implements CliState {
    @Override
    public void showMenu() {
        System.out.println("\n--- Exiting Application ---");
    }

    @Override
    public void goNext() {

    }

    @Override
    public void goNext(CliContext context, String input) {
        System.out.println("Thank you for using the application toGetJob. Goodbye!");
        System.exit(0);
    }
}

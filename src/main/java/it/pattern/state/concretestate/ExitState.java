package it.pattern.state.concretestate;

import it.pattern.state.abstractstate.CliState;
import it.pattern.state.contextstate.CliContext;

public class ExitState implements CliState {
    @Override
    public void showMenu() {
        System.out.println("\n ---Exit---");
    }

    @Override
    public void goNext(CliContext context, String input) {
        System.out.println("Thank you for using the application toGetJob. Goodbye!");
        System.exit(0);

    }
}

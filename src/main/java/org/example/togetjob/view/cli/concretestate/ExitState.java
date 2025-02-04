package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

public class ExitState implements CliState {
    @Override
    public void showMenu() {
        Printer.print("\n--- Exiting Application ---");
    }

    @Override
    public void goNext() {

    }

    @Override
    public void goNext(CliContext context, String input) {
        Printer.print("Thank you for using the application toGetJob. Goodbye!");
        System.exit(0);
    }
}

package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.contextstate.CliContext;

import java.util.logging.Logger;

public class ExitState implements CliState {

    private static final Logger logger = Logger.getLogger(ExitState.class.getName());

    @Override
    public void showMenu() {
        logger.info("\n--- Exiting Application ---");
    }

    @Override
    public void goNext() {

    }

    @Override
    public void goNext(CliContext context, String input) {
        logger.info("Thank you for using the application toGetJob. Goodbye!");
        System.exit(0); // Terminazione dell'applicazione
    }
}

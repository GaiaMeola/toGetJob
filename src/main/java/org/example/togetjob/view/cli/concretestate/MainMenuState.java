package org.example.togetjob.view.cli.concretestate;

import org.example.togetjob.view.cli.contextstate.CliContext;
import org.example.togetjob.view.cli.abstractstate.CliState;

import java.util.logging.Logger;

public class MainMenuState implements CliState {

    private static final Logger logger = Logger.getLogger(MainMenuState.class.getName());

    @Override
    public void showMenu() {
        logger.info("--- Welcome to toGetJob ---");
        logger.info("1. Login");
        logger.info("2. Register ");
        logger.info("3. Exit ");
        logger.info("Choose an option: ");
    }

    @Override
    public void goNext() {
        // Non implementato
    }

    @Override
    public void goNext(CliContext context, String input) {

        switch (input) {
            case "1": // Login
                context.setState(new LoginState());
                break;
            case "2": // Register
                context.setState(new RegisterState());
                break;
            case "3": // Exit
                context.setState(new ExitState());
                break;
            default:
                logger.warning("Invalid choice. Please, try again.");
                context.setState(new MainMenuState());
        }
    }
}

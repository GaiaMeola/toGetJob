package it.pattern.state.concretestate;

import it.pattern.state.abstractstate.CliState;
import it.pattern.state.contextstate.CliContext;

public class RegisterState implements CliState {
    @Override
    public void showMenu() {
            return;
    }

    @Override
    public void goNext(CliContext context, String input) {
        context.setState(new MainMenuState());
    }
}

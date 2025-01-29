package it.pattern.state.abstractstate;

import it.pattern.state.contextstate.CliContext;

public interface CliState {

        void showMenu();
        void goNext(CliContext context, String input);

}

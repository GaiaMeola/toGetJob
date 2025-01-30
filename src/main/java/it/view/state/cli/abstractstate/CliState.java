package it.view.state.cli.abstractstate;

import it.view.state.cli.contextstate.CliContext;

public interface CliState {
        void showMenu();
        void goNext();
        void goNext(CliContext context, String input);
}

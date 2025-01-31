package it.view.cli.abstractstate;

import it.view.cli.contextstate.CliContext;

public interface CliState {
        void showMenu();
        void goNext();
        void goNext(CliContext context, String input);
}

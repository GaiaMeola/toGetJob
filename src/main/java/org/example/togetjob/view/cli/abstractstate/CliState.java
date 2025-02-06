package org.example.togetjob.view.cli.abstractstate;

import org.example.togetjob.view.cli.contextstate.CliContext;

public interface CliState {
        void showMenu();
        void goNext(CliContext context, String input);
}

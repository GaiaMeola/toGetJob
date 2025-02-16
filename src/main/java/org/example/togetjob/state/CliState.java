package org.example.togetjob.state;

public interface CliState {
        void showMenu();
        void goNext(CliContext context, String input);
}

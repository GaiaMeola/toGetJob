package org.example.togetjob.view.cli;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.printer.Printer;
import org.example.togetjob.state.Context;
import org.example.togetjob.state.State;

public class ExitState implements State {
    @Override
    public void showMenu() {
        Printer.print("\n--- Exiting Application ---");
    }

    @Override
    public void goNext(Context contextState, String input) {

        Printer.print("Thank you for using the application toGetJob. Goodbye!");

        DatabaseConfig.getInstance().closeConnection(); //Connection closed
        Printer.print("Connection closed.");

        System.exit(0);
    }
}

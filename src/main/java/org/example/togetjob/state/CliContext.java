package org.example.togetjob.state;

import org.example.togetjob.printer.Printer;
import org.example.togetjob.view.cli.LoginState;
import org.example.togetjob.view.cli.MainMenuState;
import org.example.togetjob.view.cli.ExitState;
import org.example.togetjob.view.cli.RegisterState;

import java.util.Scanner;

public class CliContext implements Context {

    private State currentState;
    private final Scanner scanner;

    public CliContext() {
        this.currentState = new MainMenuState();
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void setState(State newState) {
        this.currentState = newState;
    }

    @Override
    public void initialize() {

        while (!(currentState instanceof ExitState)) {
            currentState.showMenu();
            String input = scanner.nextLine().trim();
            Printer.print("Input received: " + input);
            goNext(input);

            if (currentState instanceof LoginState) {
                goNext("login");
            } else if (currentState instanceof RegisterState) {
                goNext("register");
            }
            if (currentState instanceof ExitState) {
                goNext("exit");
            }
        }

        Printer.print("Close CLI...");
    }

    @Override
    public void goNext(String input) {
        currentState.goNext(this, input);
    }

    public Scanner getScanner() {
        return this.scanner;
    }

}

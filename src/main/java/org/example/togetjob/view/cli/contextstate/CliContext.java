package org.example.togetjob.view.cli.contextstate;

import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.concretestate.ExitState;

import java.util.Scanner;
import java.util.logging.Logger;

public class CliContext {

    private static final Logger logger = Logger.getLogger(CliContext.class.getName());
    private CliState currentState;
    private final Scanner scanner;

    public CliContext(CliState currentState) {
        this.currentState = currentState;
        this.scanner = new Scanner(System.in);
    }

    public void goNext(String input){
        currentState.goNext(this, input);
    }

    public void setState(CliState newState){
        this.currentState = newState;
    }

    public void showMenu(){
        currentState.showMenu();
    }

    // CLI begin
    public void startCLI(){
        while(!(currentState instanceof ExitState)){
            showMenu(); //Current State

            String input = scanner.nextLine().trim();
            logger.info("Input received: " + input);
            goNext(input);
        }
    }

    public Scanner getScanner() {
        return this.scanner;
    }
}

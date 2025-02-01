package org.example.togetjob.view.cli.contextstate;

import org.example.togetjob.view.cli.abstractstate.CliState;
import org.example.togetjob.view.cli.concretestate.ExitState;
import org.example.togetjob.view.cli.concretestate.LoginState;
import org.example.togetjob.view.cli.concretestate.RegisterState;

import java.util.Scanner;

public class CliContext {

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
            System.out.println("Input received: " + input);
            goNext(input);

           if (currentState instanceof LoginState) {
                goNext("login");
                continue;
            }

            if (currentState instanceof RegisterState) {
                goNext("register");
                continue;
            }

            if(currentState instanceof ExitState) {
                goNext("exit");
            } //Exit State
        }
    }

    public Scanner getScanner() {
        return this.scanner;
    }

}

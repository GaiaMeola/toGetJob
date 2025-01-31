package it.view.cli.contextstate;

import it.view.cli.abstractstate.CliState;
import it.view.cli.concretestate.ExitState;

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

    // Inizia la CLI e continua a ricevere input
    public void startCLI(){
        while(!(currentState instanceof ExitState)){
            //Continua finché non siamo nell'Exit
            showMenu(); //Menù del Current State
            String input = scanner.nextLine().trim();

            System.out.println("Input received: " + input);

            //Exit State
            if(input.equalsIgnoreCase("3")){
                setState(new ExitState());
                showMenu(); //Menù dell'Exit State
                goNext(input);
                break;
            }

            // Altrimenti, prosegue con l'elaborazione dell'input
            goNext(input);
        }

    }

    public Scanner getScanner() {
        return this.scanner;
    }

}

package it.pattern.state.contextstate;

import it.pattern.state.abstractstate.CliState;

import java.util.Scanner;

public class CliContext {

    private CliState currentState;

    public CliContext(CliState currentState) {
        this.currentState = currentState;
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
        Scanner scanner = new Scanner(System.in);
        while(true){
            showMenu();
            String input = scanner.nextLine();

            // Aggiunto il controllo per "exit" per uscire dal ciclo
            if(input.equalsIgnoreCase("exit")){
                System.out.println("Exiting...");
                break;  // Esce dal ciclo, terminando l'applicazione
            }

            // Altrimenti, prosegue con l'elaborazione dell'input
            goNext(input);
        }
        scanner.close(); // Chiude lo scanner alla fine
    }

}

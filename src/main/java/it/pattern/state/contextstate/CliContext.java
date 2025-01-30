package it.pattern.state.contextstate;

import it.pattern.state.abstractstate.CliState;

import java.util.Scanner;

public class CliContext {

    private CliState currentState;

    public CliContext(CliState currentState) {
        this.currentState = currentState;
    }

    public void goNext(Integer input){
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
            Integer input = scanner.nextInt();

            System.out.println("Input ricevuto: " + input);

            // Aggiunto il controllo per "exit" per uscire dal ciclo
            if( input == 3 ){
                goNext(input);
                goNext(input);
                break;  // Esce dal ciclo, terminando l'applicazione
            }

            // Altrimenti, prosegue con l'elaborazione dell'input
            goNext(input);
        }

        scanner.close(); // Chiude lo scanner alla fine
    }

}

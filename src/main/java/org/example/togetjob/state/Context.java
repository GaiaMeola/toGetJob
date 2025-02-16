package org.example.togetjob.state;

public interface Context {
    void initialize();  // UI (CLI o GUI)
    void goNext(String input); // next state
    void setState(State state); // new state
}


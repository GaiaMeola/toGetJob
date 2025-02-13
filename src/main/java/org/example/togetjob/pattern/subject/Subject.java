package org.example.togetjob.pattern.subject;

import org.example.togetjob.pattern.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public abstract class Subject implements Runnable {

    private final List<Observer> observers; //list of observers
    private final Object MUTEX = new Object(); //synchronize
    protected boolean isAlive; //to verify the status of subject

    protected Subject(List<Observer> observers, boolean isAlive) {
        this.observers = observers;
        this.isAlive = isAlive;
    }

    public void attach(Observer obs) {
        synchronized (MUTEX) {
            this.observers.add(obs); //add an observer
        }
    }

    protected void notifyObservers() {
        List<Observer> observersLocal = null;

        synchronized (MUTEX) {
            if (this.isThereAnythingToNotify()) {
                observersLocal = new ArrayList<>(this.observers);
            }

            if (observersLocal != null) {
                for (Observer obs : observersLocal) {
                    obs.update(); //send a notification to the observer
                }
            }
        }
    }

    @Override
    public void run() {
        this.isAlive = true;
        while (this.isAlive) {
            this.notifyObservers();
        }
    }

    protected abstract boolean isThereAnythingToNotify();
}

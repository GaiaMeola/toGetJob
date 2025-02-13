package org.example.togetjob.pattern.observer;

public abstract class Observer {

    protected boolean isAlive;

    protected Observer() {
        this.isAlive = false;
    }

    public void start() {
        this.isAlive = true;
    }

    public void stop() {
        this.isAlive = false;
    }

    public abstract void update();
}

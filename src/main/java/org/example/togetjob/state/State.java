package org.example.togetjob.state;

public interface State {
    void showMenu();
    void goNext(Context context, String event);
}


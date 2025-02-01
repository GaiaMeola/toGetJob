package org.example.togetjob.session;

import org.example.togetjob.model.entity.User;

public class SessionManager {

    private static SessionManager instance;
    private User currentUser;

    private SessionManager(){ }

    public static SessionManager getInstance(){
        if(instance == null){
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentUser(User user){
        this.currentUser = user;
    }

    public User getCurrentUser(){
        return this.currentUser;
    }

    public void clearSession(){
        this.currentUser = null;
    }

    public boolean isUserLoggedIn(){
            return this.currentUser != null;
    }

}

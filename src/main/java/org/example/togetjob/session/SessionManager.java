package org.example.togetjob.session;

import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.entity.Student;
import org.example.togetjob.model.entity.User;

public class SessionManager {

    private static SessionManager instance = null;
    private User currentUser;

    private SessionManager(){ }

    public static synchronized SessionManager getInstance() {

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

    public Recruiter getRecruiterFromSession() {
        if (this.currentUser instanceof Recruiter recruiter) {
            return recruiter;
        }
        return null;
    }

    public Student getStudentFromSession() {
        if (this.currentUser instanceof Student student) {
            return student;
        }
        return null;

    }

}

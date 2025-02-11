package org.example.togetjob.model.entity;

public abstract class User {

    private final String name;
    private final String surname;
    private final String username;
    private final String emailAddress;
    private final String password;
    private final Role role;

    protected User(String name, String surname, String username, String emailAddress, String password, Role role) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
        this.role = role;
    }

    public String obtainName() {
        return name;
    }

    public String obtainUsername() {
        return username;
    }

    public String obtainEmailAddress() {
        return emailAddress;
    }

    public String obtainPassword() {
        return password;
    }

    public Role obtainRole() {
        return role;
    }

    public String obtainSurname() { return surname; }

    // public abstract void introduce() ;

}

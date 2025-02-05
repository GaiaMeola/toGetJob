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

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public String getSurname() { return surname; }
}

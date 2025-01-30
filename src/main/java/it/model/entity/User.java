package it.model.entity;

public abstract class User {

    private String name;
    private String surname;
    private String username;
    private String emailAddress;
    private String password;
    private Role role;

    public User(String name, String surname, String username, String emailAddress, String password, Role role) {
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

    public String getSurname() {
        return surname;
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
}

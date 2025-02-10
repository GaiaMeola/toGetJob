package org.example.togetjob.bean;

public class RegisterUserBean extends LoginUserBean {

    private String name;
    private String surname;
    private String role;
    private String emailAddress;

    public RegisterUserBean() {
        /* builder */
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setSurname(String surname) { this.surname = surname; }

    public void setEmail(String email) { this.emailAddress = email; }

    public void setRoleInput(String roleInput) { this.role = roleInput; }
}

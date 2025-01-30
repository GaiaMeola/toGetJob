package it.bean;

public class RegisterUserBean extends LoginUserBean {

    private String name;
    private String surname;
    private String role;
    private String checkpassword;

    public RegisterUserBean(String name, String surname, String role, String checkpassword) {
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.checkpassword = checkpassword;
    }

    public RegisterUserBean() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getCheckpassword() {
        return checkpassword;
    }

    public void setCheckpassword(String checkpassword) {
        this.checkpassword = checkpassword;
    }
}

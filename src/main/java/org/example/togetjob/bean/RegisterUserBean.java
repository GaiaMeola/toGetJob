package org.example.togetjob.bean;

public class RegisterUserBean extends LoginUserBean {

    private String name;
    private String surname;
    private String role;
    private String emailAddress;
    private String checkPassword;

    public RegisterUserBean(String username, String password, String name, String surname, String emailAddress, String role, String checkPassword) {
        super(username, password);  // (LoginUserBean)
        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.role = role;
        this.checkPassword = checkPassword;  //
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

}

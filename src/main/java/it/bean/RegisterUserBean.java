package it.bean;

public class RegisterUserBean extends LoginUserBean {

    private String name;
    private String surname;
    private String role;
    private String checkPassword;

    public RegisterUserBean(String username, String emailAddress, String password, String name, String surname, String role, String checkPassword) {
        super(username, emailAddress, password);  // Chiamata al costruttore della classe base (LoginUserBean)
        this.name = name;
        this.surname = surname;
        this.role = role;
        this.checkPassword = checkPassword;  // Inizializzazione del campo checkpassword
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

    public String getCheckPassword() {
        return checkPassword;
    }

    public void setCheckPassword(String checkPassword) {
        this.checkPassword = checkPassword;
    }
}

package org.example.togetjob.bean;

import org.example.togetjob.exceptions.InvalidPasswordException;
import org.example.togetjob.exceptions.InvalidUsernameException;

public class LoginUserBean {

    private String username;
    private String password;

    public LoginUserBean() {
        /* builder */
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws InvalidUsernameException {
        if (username == null || username.trim().isEmpty()) {
            throw new InvalidUsernameException("Username cannot be empty.");
        }
        if (username.length() < 5) {
            throw new InvalidUsernameException("Username must be at least 5 characters long.");
        }
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws InvalidPasswordException {
        if (password == null || password.trim().isEmpty()) {
            throw new InvalidPasswordException("Password cannot be empty.");
        }
        if (password.length() < 6) {
            throw new InvalidPasswordException("Password must be at least 6 characters long.");
        }
        if (!password.matches(".*[A-Z].*") || !password.matches(".*[a-z].*") || !password.matches(".*\\d.*")) { //NOSONAR
            throw new InvalidPasswordException("Password must contain at least one uppercase letter, one lowercase letter, and one digit.");
        }
        this.password = password;
    }
}

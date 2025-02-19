package org.example.togetjob.bean;

import org.example.togetjob.model.entity.Role;
import org.example.togetjob.exceptions.InvalidNameException;
import org.example.togetjob.exceptions.InvalidSurnameException;
import org.example.togetjob.exceptions.InvalidEmailException;
import org.example.togetjob.exceptions.InvalidRoleException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterUserBean extends LoginUserBean {

    private String name;
    private String surname;
    private String role;
    private String emailAddress;

    private static final String NOT_VALID = "' is not valid";

    public RegisterUserBean() {
        /* builder */
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (validateName(name)) {
            this.name = name;
        } else {
            throw new InvalidNameException("Name '" + name + NOT_VALID);
        }
    }

    public String getSurname() {
        return surname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (validateRole(role)) {
            this.role = role;
        } else {
            throw new InvalidRoleException("Role '" + role + NOT_VALID);
        }
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setSurname(String surname) {
        if (validateSurname(surname)) {
            this.surname = surname;
        } else {
            throw new InvalidSurnameException("Surname '" + surname + NOT_VALID);
        }
    }

    public void setEmail(String email) {
        if (validateEmail(email)) {
            this.emailAddress = email;
        } else {
            throw new InvalidEmailException("Email '" + email +NOT_VALID);
        }
    }

    private boolean validateName(String name) {
        return name != null && name.length() >= 2;
    }

    private boolean validateSurname(String surname) {
        return surname != null && surname.length() >= 2;
    }

    private boolean validateEmail(String email) {
        if (email == null || email.isEmpty()) return false;
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$"; //NOSONAR
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean validateRole(String role) {
        try {
            Role.valueOf(role.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

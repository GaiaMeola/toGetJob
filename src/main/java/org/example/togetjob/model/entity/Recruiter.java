package org.example.togetjob.model.entity;

import java.util.List;

public class Recruiter extends User {

    private List<String> companies;

    //User
    public Recruiter(String username, String password, String name, String surname, String emailAddress, Role role) {
        super(name, surname, username, emailAddress, password, role); // User
    }

    public Recruiter(String name, String surname, String username, String emailAddress, String password, Role role, List<String> companies) {
        super(name, surname, username, emailAddress, password, role);
        this.companies = companies;
    }

    public List<String> getCompanies() {
        return companies;
    }

}
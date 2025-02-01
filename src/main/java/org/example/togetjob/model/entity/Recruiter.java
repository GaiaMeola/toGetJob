package org.example.togetjob.model.entity;

import java.util.List;

public class Recruiter extends User {

    private List<String> companies;
    private List<Recruiter> collaborators;

    //User
    public Recruiter(String username, String password, String name, String surname, String emailAddress, Role role) {
        super(name, surname, username, emailAddress, password, role); // Chiamata alla superclasse User
    }

    public Recruiter(String name, String surname, String username, String emailAddress, String password, Role role, List<String> companies, List<Recruiter> collaborators) {
        super(name, surname, username, emailAddress, password, role);
        this.companies = companies;
        this.collaborators = collaborators;
    }


    public List<String> getCompanies() {
        return companies;
    }

    public List<Recruiter> getCollaborators() {
        return collaborators;
    }

}
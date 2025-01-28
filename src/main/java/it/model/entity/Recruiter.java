package it.model.entity;

import java.util.List;

public class Recruiter extends User{

    private List<String> companies;
    private List<Recruiter> collaborators;

    public Recruiter(String name, String surname, String username, Role role, List<String> companies, List<Recruiter> collaborators) {
        super(name, surname, username, role);
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
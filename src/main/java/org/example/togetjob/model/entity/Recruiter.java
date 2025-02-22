package org.example.togetjob.model.entity;

import org.example.togetjob.printer.Printer;

import java.util.List;

public class Recruiter extends User {

    private List<String> companies;

    //User
    public Recruiter(String name, String surname, String username, String emailAddress, String password, Role role) {
        super(name, surname, username, emailAddress, password, role);
    }

    @Override
    public void introduce() {
        Printer.print("\n--- Recruiter Profile ---");
        Printer.print(" Hello recruiter " + obtainName() + " " + obtainSurname() + " !");
    }

    //Recruiter
    public Recruiter(String name, String surname, String username, String emailAddress, String password, Role role, List<String> companies) {
        super(name, surname, username, emailAddress, password, role);
        this.companies = companies;
    }

    public List<String> obtainCompanies() {
        return this.companies;
    }

}
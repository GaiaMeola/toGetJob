package org.example.togetjob.model.factory;

import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.entity.Role;

import java.util.List;

public class RecruiterFactory {

    private RecruiterFactory() {
    }

    public static Recruiter createRecruiter(String name, String surname, String username, String emailAddress,
                                            String password, List<String> companies) {
        return new Recruiter(name, surname, username, emailAddress, password, Role.RECRUITER, companies);
    }
}

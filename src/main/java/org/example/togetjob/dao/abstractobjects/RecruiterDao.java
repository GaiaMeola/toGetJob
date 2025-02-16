package org.example.togetjob.dao.abstractobjects;

import org.example.togetjob.model.entity.Recruiter;

import java.util.Optional;

public interface RecruiterDao {

    void saveRecruiter(Recruiter recruiter);
    Optional<Recruiter> getRecruiter(String username);
}

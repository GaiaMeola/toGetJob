package org.example.togetjob.model.dao.abstractobjects;

import org.example.togetjob.model.entity.Recruiter;

import java.util.List;
import java.util.Optional;

public interface RecruiterDao {

    boolean saveRecruiter(Recruiter recruiter);
    Optional<Recruiter> getRecruiter(String username);
    List<Recruiter> getAllRecruiter();
    boolean updateRecruiter(Recruiter recruiter);
    boolean deleteRecruiter(String username);
    boolean recruiterExists(String username);
}

package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.model.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.entity.Recruiter;

import java.util.List;
import java.util.Optional;

public class DataBaseRecruiterDao implements RecruiterDao {
    @Override
    public void saveRecruiter(Recruiter recruiter) {
    }

    @Override
    public Optional<Recruiter> getRecruiter(String username) {
        return Optional.empty();
    }

    @Override
    public List<Recruiter> getAllRecruiter() {
        return List.of();
    }

    @Override
    public boolean updateRecruiter(Recruiter recruiter) {
        return false;
    }

    @Override
    public boolean deleteRecruiter(String username) {
        return false;
    }

    @Override
    public boolean recruiterExists(String username) {
        return false;
    }
}

package it.model.dao.concreteobjects;

import it.model.dao.abstractobjects.RecruiterDao;
import it.model.entity.Recruiter;

import java.util.List;
import java.util.Optional;

public class DataBaseRecruiterDao implements RecruiterDao {
    @Override
    public boolean saveRecruiter(Recruiter recruiter) {
        return false;
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

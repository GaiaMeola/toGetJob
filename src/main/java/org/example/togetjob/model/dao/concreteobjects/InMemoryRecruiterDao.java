package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.model.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.entity.Recruiter;

import java.util.*;

public class InMemoryRecruiterDao implements RecruiterDao {

    private static Map<String, Recruiter> recruiters = new HashMap<>();

    @Override
    public boolean saveRecruiter(Recruiter recruiter) {
        if (recruiters.containsKey(recruiter.getUsername())) {
            return false;
        }
        recruiters.put(recruiter.getUsername(), recruiter);
        return true;
    }

    @Override
    public Optional<Recruiter> getRecruiter(String username) {
        return Optional.ofNullable(recruiters.get(username));
    }

    @Override
    public List<Recruiter> getAllRecruiter() {
        return new ArrayList<>(recruiters.values());
    }

    @Override
    public boolean updateRecruiter(Recruiter recruiter) {
        if (!recruiters.containsKey(recruiter.getUsername())) {
            return false;
        }
        recruiters.put(recruiter.getUsername(), recruiter);
        return true;
    }

    @Override
    public boolean deleteRecruiter(String username) {
        return recruiters.remove(username) != null;
    }

    @Override
    public boolean recruiterExists(String username) {
        return recruiters.containsKey(username);
    }
}

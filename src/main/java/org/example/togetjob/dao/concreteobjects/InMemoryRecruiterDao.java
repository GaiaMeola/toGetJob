package org.example.togetjob.dao.concreteobjects;

import org.example.togetjob.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.entity.Recruiter;

import java.util.*;

public class InMemoryRecruiterDao implements RecruiterDao {

    private static final Map<String, Recruiter> recruiters = new HashMap<>();

    @Override
    public void saveRecruiter(Recruiter recruiter) {
        if (recruiters.containsKey(recruiter.obtainUsername())) {
            return;
        }
        recruiters.put(recruiter.obtainUsername(), recruiter);
    }

    @Override
    public Optional<Recruiter> getRecruiter(String username) {
        return Optional.ofNullable(recruiters.get(username));
    }

    @Override
    public boolean updateRecruiter(Recruiter recruiter) {
        if (!recruiters.containsKey(recruiter.obtainUsername())) {
            return false;
        }
        recruiters.put(recruiter.obtainUsername(), recruiter);
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

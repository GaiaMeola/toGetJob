package org.example.togetjob.dao.concreteobjects;

import org.example.togetjob.dao.abstractobjects.UserDao;
import org.example.togetjob.exceptions.EmailAlreadyExistsException;
import org.example.togetjob.model.entity.User;

import java.util.*;

public class InMemoryUserDao implements UserDao {

    private static final Map<String, User> users = new HashMap<>();
    private static final Map<String, String> emails = new HashMap<>();

    @Override
    public boolean saveUser(User user) throws EmailAlreadyExistsException {

        if (emails.containsKey(user.obtainEmailAddress())) {
            throw new EmailAlreadyExistsException("The email address is already registered.");
        }


        if (users.containsKey(user.obtainUsername())) {
            return false; // Username exists
        }


        users.put(user.obtainUsername(), user);
        emails.put(user.obtainEmailAddress(), user.obtainUsername());

        return true;
    }

    @Override
    public Optional<User> getUser(String username) {
        return Optional.ofNullable(users.get(username));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}

package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.model.dao.abstractobjects.UserDao;
import org.example.togetjob.model.entity.User;

import java.util.*;

public class InMemoryUserDao implements UserDao {

    private static final Map<String, User> users = new HashMap<>();

    @Override
    public boolean saveUser(User user) {
        if (users.containsKey(user.getUsername())) {
            return false; // Username exists
        }
        users.put(user.getUsername(), user);
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

    @Override
    public boolean updateUser(User user) {
        if (!users.containsKey(user.getUsername())) {
            return false; // User does not exist
        }

        users.put(user.getUsername(), user);
        return true;
    }

    @Override
    public boolean deleteUser(String username) {
        return users.remove(username) != null;
    }

    @Override
    public boolean userExists(String username) {
        return users.containsKey(username);
    }
}

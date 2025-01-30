package it.model.dao.concreteobjects;

import it.model.dao.abstractobjects.UserDao;
import it.model.entity.User;

import java.util.*;

public class InMemoryUserDao implements UserDao {

    private static Map<String, User> users = new HashMap<>();

    @Override
    public boolean saveUser(User user) {
        if (users.containsKey(user.getUsername())) {
            return false; // username già esistente
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
        // Controlla se l'utente esiste
        if (!users.containsKey(user.getUsername())) {
            return false; // l'utente non esiste
        }

        // Aggiorna i dati dell'utente
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

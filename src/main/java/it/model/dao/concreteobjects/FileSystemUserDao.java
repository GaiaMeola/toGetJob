package it.model.dao.concreteobjects;

import it.model.dao.abstractobjects.UserDao;
import it.model.entity.User;

import java.util.List;
import java.util.Optional;

public class FileSystemUserDao implements UserDao {

    @Override
    public boolean saveUser(User user) {
        return false;
    }

    @Override
    public Optional<User> getUser(String username) {
        return Optional.empty();
    }

    @Override
    public List<User> getAllUsers() {
        return List.of();
    }

    @Override
    public boolean updateUser(User user) {
        return false;
    }

    @Override
    public boolean deleteUser(String username) {
        return false;
    }

    @Override
    public boolean userExists(String username) {
        return false;
    }
}

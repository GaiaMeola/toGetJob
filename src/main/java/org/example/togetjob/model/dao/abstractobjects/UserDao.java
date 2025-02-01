package org.example.togetjob.model.dao.abstractobjects;

import org.example.togetjob.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    boolean saveUser(User user);
    Optional<User> getUser(String username);
    List<User> getAllUsers();
    boolean updateUser(User user);
    boolean deleteUser(String username);
    boolean userExists(String username);
}

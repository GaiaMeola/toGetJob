package org.example.togetjob.dao.abstractobjects;

import org.example.togetjob.model.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    boolean saveUser(User user);
    Optional<User> getUser(String username);
    List<User> getAllUsers();
}

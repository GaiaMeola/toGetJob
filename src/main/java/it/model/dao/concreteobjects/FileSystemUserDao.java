package it.model.dao.concreteobjects;

import it.model.dao.abstractobjects.UserDao;
import it.model.entity.User;

public class FileSystemUserDao implements UserDao {
    @Override
    public void createUser(User user) {
    }

    @Override
    public User getUser(int id) {
        return null;
    }
}

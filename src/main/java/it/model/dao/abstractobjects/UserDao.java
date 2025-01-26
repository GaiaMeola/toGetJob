package it.model.dao.abstractobjects;

import it.model.entity.User;

public interface UserDao {

    void createUser(User user);
    User getUser(int id);

}

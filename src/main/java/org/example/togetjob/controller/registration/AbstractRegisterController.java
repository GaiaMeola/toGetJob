package org.example.togetjob.controller.registration;

import org.example.togetjob.bean.RegisterUserBean;
import org.example.togetjob.exceptions.DatabaseException;
import org.example.togetjob.exceptions.EmailAlreadyExistsException;
import org.example.togetjob.exceptions.FileAccessException;
import org.example.togetjob.exceptions.UsernameTakeException;
import org.example.togetjob.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import org.example.togetjob.dao.abstractobjects.UserDao;
import org.example.togetjob.model.entity.User;

import java.util.Optional;

public abstract class AbstractRegisterController {

    protected final UserDao userDao;

    protected AbstractRegisterController(){
        this.userDao = AbstractFactoryDaoSingleton.getFactoryDao().createUserDao();
    }

    public boolean registerUser(RegisterUserBean userBean) throws UsernameTakeException, DatabaseException, FileAccessException, EmailAlreadyExistsException {

        Optional<User> existingUser = userDao.getUser(userBean.getUsername());

        if (existingUser.isPresent()) {
            throw new UsernameTakeException("Sorry, username " + userBean.getUsername() + " is already taken. Please select another one !"); // User already exists
        }

        User user = createUser(userBean);
        boolean saved = userDao.saveUser(user);

        if (saved) {
            saveUserSpecificData(user);
        }

        return saved;
    }

    protected abstract User createUser(RegisterUserBean userBean);

    protected abstract void saveUserSpecificData(User user);

}

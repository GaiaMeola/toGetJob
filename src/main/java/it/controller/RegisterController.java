package it.controller;

import it.bean.RegisterUserBean;
import it.model.dao.abstractfactorydao.AbstractFactoryDaoSingleton;
import it.model.dao.abstractobjects.UserDao;
import it.model.entity.Student;
import it.model.entity.Recruiter;
import it.model.entity.User;
import it.model.entity.Role;

public class RegisterController {

    private UserDao userDao;

    // Costruttore che inizializza il DAO
    public RegisterController() {
        this.userDao = AbstractFactoryDaoSingleton.getFactoryDao().createUserDao();
    }

    // Metodo per registrare un utente
    public boolean registerUser(RegisterUserBean userBean) {
        // Verifica se l'username è già esistente
        if (userDao.userExists(userBean.getUsername())) {
            return false; // Username già in uso
        }

        String plainPassword = userBean.getPassword();  // Temporaneamente senza hashing

        // Convertiamo la stringa del ruolo in un enum Role
        Role role = Role.fromId(userBean.getRole());  // La stringa della bean viene tradotta in Role enum

        // Creiamo l'utente in base al ruolo
        User user;
        switch (role) {
            case STUDENT:
                // Creazione di uno Student (tramite costruttore che aggiunge dettagli specifici)
                user = new Student(userBean.getUsername(), plainPassword, userBean.getName(), userBean.getSurname(),
                        userBean.getEmailAddress(), role);
                break;
            case RECRUITER:
                // Creazione di un Recruiter
                user = new Recruiter(userBean.getUsername(), plainPassword, userBean.getName(), userBean.getSurname(),
                        userBean.getEmailAddress(), role);
                break;
            default:
                // Se il ruolo non è valido, lanciamo un'eccezione
                throw new IllegalArgumentException("Ruolo non valido: " + role);
        }

        // Salviamo l'utente tramite il DAO
        return userDao.saveUser(user);
    }
}

package org.example.togetjob.dao.concreteobjects;

import org.example.togetjob.dao.abstractobjects.UserDao;
import org.example.togetjob.exceptions.EmailAlreadyExistsException;
import org.example.togetjob.exceptions.FileAccessException;
import org.example.togetjob.model.entity.*;

import java.io.*;
import java.util.*;

public class FileSystemUserDao implements UserDao {
    private static final String PATH_NAME = "src/main/resources/files_txt/User.txt";
    private static final String ERROR = "Error reading user from file.";

    @Override
    public boolean saveUser(User user) throws EmailAlreadyExistsException {
        if (userExists(user.obtainUsername())) {
            return false; // The user already exists
        }
        if (emailExists(user.obtainEmailAddress())) {
            throw new EmailAlreadyExistsException("The email address is already registered.");
        }
        return appendToFile(userToLine(user));
    }

    @Override
    public Optional<User> getUser(String username) {
        return readAllUsers().stream()
                .filter(user -> user.obtainUsername().equals(username))
                .findFirst();
    }

    @Override
    public List<User> getAllUsers() {
        return readAllUsers();
    }

    private boolean userExists(String username) {
        return readAllUsers().stream()
                .anyMatch(user -> user.obtainUsername().equals(username));
    }

    private boolean emailExists(String email) {
        return readAllUsers().stream()
                .anyMatch(user -> user.obtainEmailAddress().equals(email));
    }

    private List<User> readAllUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                User user = parseUser(line);
                if (user != null) {
                    users.add(user);
                }
            }
        } catch (IOException e) {
           throw new FileAccessException(ERROR);
        }
        return users;
    }

    private boolean appendToFile(String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME, true))) {
            writer.write(line);
            writer.newLine();
            return true;
        } catch (IOException e) {
            throw new FileAccessException(ERROR);
        }
    }

    private User parseUser(String line) {
        String[] data = line.split(";");
        if (data.length < 6) return null;

        try {
            Role role = Role.valueOf(data[5].trim());
            return (role == Role.STUDENT)
                    ? new Student(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), role)
                    : new Recruiter(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), role);
        } catch (IllegalArgumentException e) {
            throw new FileAccessException(ERROR);
        }
    }

    private String userToLine(User user) {
        return String.join(";", user.obtainName(), user.obtainSurname(), user.obtainUsername(),
                user.obtainEmailAddress(), user.obtainPassword(), user.obtainRole().toString());
    }
}

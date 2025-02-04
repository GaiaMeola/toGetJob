package org.example.togetjob.model.dao.concreteobjects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.togetjob.model.dao.abstractobjects.UserDao;
import org.example.togetjob.model.entity.User;
import org.example.togetjob.printer.Printer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileSystemUserDao implements UserDao {

    private static final String PATH_NAME = "files_json/User.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private File getUserFile() {
        URL resource = getClass().getClassLoader().getResource(PATH_NAME);
        if (resource == null) {
            Printer.print("File non trovato: files_json/User.json");
            return null;
        }
        Printer.print("File trovato a: " + resource.getPath());
        return new File(resource.getFile());
    }

    @Override
    public boolean saveUser(User user) {
        try {
            List<User> users = getAllUsers();

            if (users.stream().anyMatch(u -> u.getUsername().equals(user.getUsername()))) {
                Printer.print("The user: " + user.getUsername() + " already exists.");
                return false;
            }

            users.add(user);
            objectMapper.writeValue(getUserFile(), users);
            Printer.print("The user: " + user.getUsername() + " has been successfully saved in the File System");
            return true;

        } catch (IOException e) {
            Printer.print("Error saving user: " + user.getUsername());
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Optional<User> getUser(String username) {
        return getAllUsers().stream().filter(user -> user.getUsername().equals(username)).findFirst();
    }

    @Override
    public List<User> getAllUsers() {
        try {
            File file = getUserFile();
            if (!file.exists()) {
                return new ArrayList<>();
            }

            return objectMapper.readValue(file, new TypeReference<List<User>>() {});

        } catch (IOException e) {
            Printer.print("Error retrieving users from File System");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public boolean updateUser(User user) {
        try {
            List<User> users = getAllUsers();
            boolean found = false;

            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUsername().equals(user.getUsername())) {
                    users.set(i, user); // Update user data
                    found = true;
                    break;
                }
            }

            if (!found) {
                Printer.print("The user: " + user.getUsername() + " doesn't exist.");
                return false;
            }

            objectMapper.writeValue(new File(PATH_NAME), users);
            Printer.print("The user: " + user.getUsername() + " has been successfully updated.");
            return true;

        } catch (IOException e) {
            Printer.print("An unexpected error occurred: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteUser(String username) {
        try {
            List<User> users = getAllUsers();
            boolean removed = users.removeIf(user -> user.getUsername().equals(username));
            if (!removed) {
                Printer.print("The user: " + username + " doesn't exist.");
                return false;
            }

            objectMapper.writeValue(getUserFile(), users);
            Printer.print("The user: " + username + " has been successfully deleted.");
            return true;

        } catch (IOException e) {
            Printer.print("The user: " + username + " cannot be deleted");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean userExists(String username) {
        return getAllUsers().stream().anyMatch(user -> user.getUsername().equals(username));
    }
}

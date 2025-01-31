package it.model.dao.concreteobjects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.model.dao.abstractobjects.UserDao;
import it.model.entity.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileSystemUserDao implements UserDao {

    private static final String PATH_NAME = "/Users/enrico_talone/IdeaProjects/toGetJob/src/main/resources/org/example/togetjob/files_json/User.json";
    //private static final String PATH_NAME = "files_json/User.json" ;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean saveUser(User user) {
        try {
            List<User> users = getAllUsers();

            if (users.stream().anyMatch(u -> u.getUsername().equals(user.getUsername()))) {
                System.out.println("The user: " + user.getUsername() + " already exists.");
                return false;
            }

            users.add(user);
            objectMapper.writeValue(new File(PATH_NAME), users);
            System.out.println("The user: " + user.getUsername() + " has been successfully saved in the File System");
            return true;

        } catch (IOException e) {
            System.out.println("The user: " + user.getUsername() + " cannot be saved in the File System");
            System.out.println(e.getMessage());
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
            List<User> users = new ArrayList<>(objectMapper.readValue(new File(PATH_NAME), new TypeReference<List<User>>() {}));

            if (users.isEmpty()) {
                return new ArrayList<>();
            }

            return users;

        } catch (IOException e) {
            System.out.println("Users cannot be retrieved from File System");
            System.out.println(e.getMessage());
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
                System.out.println("The user: " + user.getUsername() + " doesn't exist.");
                return false;
            }

            objectMapper.writeValue(new File(PATH_NAME), users);
            System.out.println("The user: " + user.getUsername() + " has been successfully updated.");
            return true;

        } catch (IOException e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteUser(String username) {
        try {
            List<User> users = getAllUsers();
            boolean removed = users.removeIf(user -> user.getUsername().equals(username));
            if (!removed) {
                System.out.println("The user: " + username + " doesn't exist.");
                return false;
            }

            objectMapper.writeValue(new File(PATH_NAME), users);
            System.out.println("The user: " + username + " has been successfully deleted.");
            return true;

        } catch (IOException e) {
            System.out.println("The user: " + username + " cannot be deleted");
            System.out.println(e.getMessage());
            return false;
        }
    }

    @Override
    public boolean userExists(String username) {
        try {
            List<User> users = objectMapper.readValue(new File(PATH_NAME), new TypeReference<List<User>>() {});

            if (users.isEmpty()) {
                System.out.println("There aren't any users in the File System");
                return false;
            }

            boolean presence = users.stream().anyMatch(user -> user.getUsername().equals(username));

            if (presence) {
                System.out.println("The user: " + username + " exists");
                return true;
            }

            System.out.println("The user: " + username + " doesn't exist");
            return false;

        } catch (IOException e) {
            System.out.println("The user: " + username + " cannot be searched");
            System.out.println(e.getMessage());
            return false;
        }
    }
}

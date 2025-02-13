package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.model.dao.abstractobjects.UserDao;
import org.example.togetjob.model.entity.*;

import java.io.*;
import java.util.*;

public class FileSystemUserDao implements UserDao {
    private static final String PATH_NAME = "src/main/resources/files_txt/User.txt";

    @Override
    public boolean saveUser(User user) {
        if (userExists(user.obtainUsername())) {
            return false; // The user already exists
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

    @Override
    public boolean updateUser(User user) {
        List<User> users = readAllUsers();
        boolean found = false;

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).obtainUsername().equals(user.obtainUsername())) {
                users.set(i, user); // Update user
                found = true;
                break;
            }
        }

        return found && writeAllUsers(users);
    }

    @Override
    public boolean deleteUser(String username) {
        List<User> users = readAllUsers();
        boolean removed = users.removeIf(user -> user.obtainUsername().equals(username));

        return removed && writeAllUsers(users);
    }

    @Override
    public boolean userExists(String username) {
        return readAllUsers().stream()
                .anyMatch(user -> user.obtainUsername().equals(username));
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
            e.printStackTrace(); // Log errori
        }
        return users;
    }

    private boolean writeAllUsers(List<User> users) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME))) {
            for (User user : users) {
                writer.write(userToLine(user));
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean appendToFile(String line) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME, true))) {
            writer.write(line);
            writer.newLine();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
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
            e.printStackTrace();
            return null;
        }
    }

    private String userToLine(User user) {
        return String.join(";", user.obtainName(), user.obtainSurname(), user.obtainUsername(),
                user.obtainEmailAddress(), user.obtainPassword(), user.obtainRole().toString());
    }
}

package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.model.dao.abstractobjects.UserDao;
import org.example.togetjob.model.entity.*;
import org.example.togetjob.model.entity.Role;

import java.io.*;
import java.util.*;

public class FileSystemUserDao implements UserDao {
    private static final String PATH_NAME = "src/main/resources/files_txt/User.txt";

    @Override
    public boolean saveUser(User user) {
        if (userExists(user.obtainUsername())) {
            return false; // The user with this username already exists
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME, true))) {
            writer.write(user.obtainName() + ";" + user.obtainSurname() + ";" + user.obtainUsername() + ";" +
                    user.obtainEmailAddress() + ";" + user.obtainPassword() + ";" + user.obtainRole());
            writer.newLine();
            return true;
        } catch (IOException | IllegalArgumentException e) {
            return false; // Error during file writing or parsing the role
        }
    }

    @Override
    public Optional<User> getUser(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 6 && data[2].trim().equals(username)) {
                    // Found the user with the matching username
                    Role role = Role.valueOf(data[5].trim());
                    User user = (role == Role.STUDENT)
                            ? new Student(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), role)
                            : new Recruiter(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), role);
                    return Optional.of(user);
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            // Handle error during file reading or parsing the role
        }
        return Optional.empty(); // User not found
    }

    @Override
    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Split the line based on the semicolon
                String[] data = line.split(";");
                // Verify that the line contains the correct data
                if (data.length >= 6) {
                    Role role = Role.valueOf(data[5].trim()); // Remove extra spaces
                    User user = (role == Role.STUDENT)
                            ? new Student(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), role)
                            : new Recruiter(data[0].trim(), data[1].trim(), data[2].trim(), data[3].trim(), data[4].trim(), role);
                    users.add(user);
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            // Handle error during file reading or parsing the role
        }
        return users;
    }

    @Override
    public boolean updateUser(User user) {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 6 && data[2].trim().equals(user.obtainUsername())) {
                    // Find the user's line to update and replace it with the new data
                    line = user.obtainName() + "," + user.obtainSurname() + "," + user.obtainUsername() + "," +
                            user.obtainEmailAddress() + "," + user.obtainPassword() + "," + user.obtainRole();
                }
                lines.add(line); // Add the line to the buffer
            }
        } catch (IOException | IllegalArgumentException e) {
            return false; // Error during file reading or parsing the role
        }

        // Rewrite the file with the updated data
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine(); // Write newline
            }
            return true;
        } catch (IOException e) {
            return false; // Error during file writing
        }
    }

    @Override
    public boolean deleteUser(String username) {
        if (!userExists(username)) {
            return false; // User does not exist, so we cannot delete it.
        }

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 6 && !data[2].trim().equals(username)) {
                    // Add only the lines that do not match the user to be deleted
                    lines.add(line);
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            return false; // Error during file reading or parsing the role
        }

        // Rewrite the file without the deleted user
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine(); // Write newline
            }
            return true;
        } catch (IOException e) {
            return false; // Error during file writing
        }
    }

    @Override
    public boolean userExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 6 && data[2].trim().equals(username)) {
                    return true; // User found
                }
            }
        } catch (IOException | IllegalArgumentException e) {
            // Handle error during file reading or parsing the role
        }
        return false; // User not found
    }
}

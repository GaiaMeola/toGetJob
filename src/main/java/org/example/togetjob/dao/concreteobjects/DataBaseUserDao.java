package org.example.togetjob.dao.concreteobjects;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.exceptions.DatabaseException;
import org.example.togetjob.exceptions.EmailAlreadyExistsException;
import org.example.togetjob.dao.abstractobjects.UserDao;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.entity.Role;
import org.example.togetjob.model.entity.Student;
import org.example.togetjob.model.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataBaseUserDao implements UserDao {

    private static final String SQL_INSERT_USER = "INSERT INTO USER (Username, Name, Surname, EmailAddress, Password, Role) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_USER = "SELECT Username, Name, Surname, EmailAddress, Password, Role FROM USER WHERE Username = ?";
    private static final String SQL_SELECT_ALL_USERS = "SELECT Username, Name, Surname, EmailAddress, Password, Role FROM USER";
    private static final String SQL_SELECT_USER_BY_EMAIL = "SELECT COUNT(*) FROM USER WHERE EmailAddress = ?";

    @Override
    public boolean saveUser(User user) throws DatabaseException, EmailAlreadyExistsException {

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_USER_BY_EMAIL)) {

            stmt.setString(1, user.obtainEmailAddress());
            ResultSet rs = stmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                throw new EmailAlreadyExistsException("The email address is already registered.");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error checking email existence in the database.");
        }


        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_USER)) {

            stmt.setString(1, user.obtainUsername());
            stmt.setString(2, user.obtainName());
            stmt.setString(3, user.obtainSurname());
            stmt.setString(4, user.obtainEmailAddress());
            stmt.setString(5, user.obtainPassword());
            stmt.setString(6, user.obtainRole().toString());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error saving user to the database, try again later.");
        }
    }

    @Override
    public Optional<User> getUser(String username) throws DatabaseException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_USER)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("Name");
                String surname = rs.getString("Surname");
                String emailAddress = rs.getString("EmailAddress");
                String password = rs.getString("Password");
                String roleString = rs.getString("Role");
                Role role = mapRole(roleString);

                if (role == null) {
                    throw new DatabaseException("Invalid role value in the database: " + roleString);
                }

                User user = createUserByRole(name, surname, username, emailAddress, password, role);
                return Optional.of(user);
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving user from the database.");
        }
        return Optional.empty();
    }

    private Role mapRole(String roleString) {
        if ("Student".equalsIgnoreCase(roleString)) {
            return Role.STUDENT;
        } else if ("Recruiter".equalsIgnoreCase(roleString)) {
            return Role.RECRUITER;
        } else {
            return null;
        }
    }

    private User createUserByRole(String name, String surname, String username, String emailAddress, String password, Role role) {
        return switch (role) {
            case STUDENT -> new Student(name, surname, username, emailAddress, password, role);
            case RECRUITER -> new Recruiter(name, surname, username, emailAddress, password, role);
        };
    }

    @Override
    public List<User> getAllUsers() throws DatabaseException {
        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_USERS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("Username");
                String name = rs.getString("Name");
                String surname = rs.getString("Surname");
                String emailAddress = rs.getString("EmailAddress");
                String password = rs.getString("Password");
                Role role = Role.valueOf(rs.getString("Role").toUpperCase());

                User user;
                if (role.equals(Role.STUDENT)) {
                    user = new Student(name, surname, username, emailAddress, password, role);
                } else {
                    user = new Recruiter(name, surname, username, emailAddress, password, role);
                }

                users.add(user);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving all users from the database.");
        }

        return users;
    }
}

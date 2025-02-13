package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.exceptions.DatabaseException;
import org.example.togetjob.model.dao.abstractobjects.UserDao;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.entity.Role;
import org.example.togetjob.model.entity.Student;
import org.example.togetjob.model.entity.User;
import org.example.togetjob.printer.Printer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataBaseUserDao implements UserDao {

    private static final String SQL_INSERT_USER = "INSERT INTO USER (Username, Name, Surname, EmailAddress, Password, Role) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_USER = "SELECT Username, Name, Surname, EmailAddress, Password, Role FROM USER WHERE Username = ?";
    private static final String SQL_SELECT_ALL_USERS = "SELECT Username, Name, Surname, EmailAddress, Password, Role FROM USER";
    private static final String SQL_UPDATE_USER = "UPDATE USER SET Name = ?, Surname = ?, EmailAddress = ?, Password = ?, Role = ? WHERE Username = ?";
    private static final String SQL_DELETE_USER = "DELETE FROM USER WHERE Username = ?";
    private static final String SQL_CHECK_USER_EXISTS = "SELECT COUNT(*) FROM USER WHERE Username = ?";

    @Override
    public boolean saveUser(User user) throws DatabaseException {
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
            throw new DatabaseException("Error saving user to the database , try again later");
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
            throw new DatabaseException("Error retrieving user from the database");
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


        //Connection
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_USERS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) { //loop
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
            throw new DatabaseException("Error retrieving all users from the database");
        }

        return users;
    }

    @Override
    public boolean updateUser(User user) throws DatabaseException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_USER)) {

            stmt.setString(1, user.obtainName());
            stmt.setString(2, user.obtainSurname());
            stmt.setString(3, user.obtainEmailAddress());
            stmt.setString(4, user.obtainPassword());
            stmt.setString(5, user.obtainRole().name());
            stmt.setString(6, user.obtainUsername());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating user in the database");
        }
    }

    @Override
    public boolean deleteUser(String username) throws DatabaseException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_USER)) {

            stmt.setString(1, username);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting user from the database");
        }
    }

    @Override
    public boolean userExists(String username) throws DatabaseException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_CHECK_USER_EXISTS)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error checking if user exists in the database");
        }

        return false;
    }
}

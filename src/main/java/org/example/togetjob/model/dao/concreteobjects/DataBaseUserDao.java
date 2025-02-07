package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.model.dao.abstractobjects.UserDao;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.entity.Role;
import org.example.togetjob.model.entity.Student;
import org.example.togetjob.model.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataBaseUserDao implements UserDao {

    @Override
    public boolean saveUser(User user) {
        String sql = "INSERT INTO USER (Username, Name, Surname, EmailAddress, Password, Role) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getSurname());
            stmt.setString(4, user.getEmailAddress());
            stmt.setString(5, user.getPassword());
            stmt.setString(6, user.getRole().name());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Log the exception or handle it as needed
            throw new RuntimeException("Error saving user to the database", e);
        }
    }

    @Override
    public Optional<User> getUser(String username) {
        String sql = "SELECT Username, Name, Surname, EmailAddress, Password, Role FROM USER WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
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

                return Optional.of(user);
            }
        } catch (SQLException e) {
            // Log the exception or handle it as needed
            throw new RuntimeException("Error retrieving user from the database", e);
        }
        return Optional.empty();
    }

    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT Username, Name, Surname, EmailAddress, Password, Role FROM USER";
        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
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
            // Log the exception or handle it as needed
            throw new RuntimeException("Error retrieving all users from the database", e);
        }

        return users;
    }

    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE USER SET Name = ?, Surname = ?, EmailAddress = ?, Password = ?, Role = ? WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getSurname());
            stmt.setString(3, user.getEmailAddress());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getRole().name());
            stmt.setString(6, user.getUsername());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            // Log the exception or handle it as needed
            throw new RuntimeException("Error updating user in the database", e);
        }
    }

    @Override
    public boolean deleteUser(String username) {
        String sql = "DELETE FROM USER WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            // Log the exception or handle it as needed
            throw new RuntimeException("Error deleting user from the database", e);
        }
    }

    @Override
    public boolean userExists(String username) {
        String sql = "SELECT COUNT(*) FROM USER WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            // Log the exception or handle it as needed
            throw new RuntimeException("Error checking if user exists in the database", e);
        }

        return false;
    }
}
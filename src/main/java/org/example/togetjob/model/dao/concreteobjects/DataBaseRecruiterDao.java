package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.model.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.entity.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataBaseRecruiterDao implements RecruiterDao {

    private static final String INSERT_RECRUITER_SQL =
            "INSERT INTO RECRUITER (Username, Name, Surname, EmailAddress, Password, Role, Companies) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_RECRUITER_BY_USERNAME_SQL =
            "SELECT Username, Name, Surname, EmailAddress, Password, Role, Companies FROM RECRUITER WHERE Username = ?";
    private static final String SELECT_ALL_RECRUITERS_SQL =
            "SELECT Username, Name, Surname, EmailAddress, Password, Role, Companies FROM RECRUITER";
    private static final String UPDATE_RECRUITER_SQL =
            "UPDATE RECRUITER SET Name = ?, Surname = ?, EmailAddress = ?, Password = ?, Companies = ? WHERE Username = ?";
    private static final String DELETE_RECRUITER_SQL =
            "DELETE FROM RECRUITER WHERE Username = ?";
    private static final String CHECK_RECRUITER_EXISTS_SQL =
            "SELECT COUNT(*) FROM RECRUITER WHERE Username = ?";

    @Override
    public void saveRecruiter(Recruiter recruiter) {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_RECRUITER_SQL)) {

            stmt.setString(1, recruiter.getUsername());
            stmt.setString(2, recruiter.getName());
            stmt.setString(3, recruiter.getSurname());
            stmt.setString(4, recruiter.getEmailAddress());
            stmt.setString(5, recruiter.getPassword());
            stmt.setString(6, recruiter.getRole().name()); // Role (RECRUITER)
            stmt.setString(7, String.join(",", recruiter.getCompanies())); // Convert list to comma-separated string

            stmt.executeUpdate(); // Executes the insertion into the database
        } catch (SQLException e) {
            // Log or handle exception as needed
        }
    }

    @Override
    public Optional<Recruiter> getRecruiter(String username) {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_RECRUITER_BY_USERNAME_SQL)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("Name");
                String surname = rs.getString("Surname");
                String emailAddress = rs.getString("EmailAddress");
                String password = rs.getString("Password");
                Role role = Role.valueOf(rs.getString("Role").toUpperCase()); // Get Role as enum

                String companiesString = rs.getString("Companies");
                List<String> companies = List.of(companiesString.split(",")); // Convert string back to list

                Recruiter recruiter = new Recruiter(name, surname, username, emailAddress, password, role, companies);
                return Optional.of(recruiter); // Return recruiter object
            }

        } catch (SQLException e) {
            // Log or handle exception as needed
        }

        return Optional.empty(); // Return empty if no recruiter is found
    }

    @Override
    public List<Recruiter> getAllRecruiter() {
        List<Recruiter> recruiters = new ArrayList<>();
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_RECRUITERS_SQL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("Username");
                String name = rs.getString("Name");
                String surname = rs.getString("Surname");
                String emailAddress = rs.getString("EmailAddress");
                String password = rs.getString("Password");
                Role role = Role.valueOf(rs.getString("Role").toUpperCase()); // Convert role string to Role enum

                String companiesString = rs.getString("Companies");
                List<String> companies = List.of(companiesString.split(",")); // Convert companies string to list

                Recruiter recruiter = new Recruiter(name, surname, username, emailAddress, password, role, companies);
                recruiters.add(recruiter); // Add recruiter to list
            }

        } catch (SQLException e) {
            // Log or handle exception as needed
        }

        return recruiters; // Return the list of recruiters
    }

    @Override
    public boolean updateRecruiter(Recruiter recruiter) {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_RECRUITER_SQL)) {

            stmt.setString(1, recruiter.getName());
            stmt.setString(2, recruiter.getSurname());
            stmt.setString(3, recruiter.getEmailAddress());
            stmt.setString(4, recruiter.getPassword());
            stmt.setString(5, String.join(",", recruiter.getCompanies())); // Convert list to comma-separated string
            stmt.setString(6, recruiter.getUsername());

            int rowsUpdated = stmt.executeUpdate(); // Execute the update query
            return rowsUpdated > 0; // Return true if at least one row was updated
        } catch (SQLException e) {
            // Log or handle exception as needed
        }

        return false; // Return false if no rows were updated or an error occurred
    }

    @Override
    public boolean deleteRecruiter(String username) {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_RECRUITER_SQL)) {

            stmt.setString(1, username);
            int rowsDeleted = stmt.executeUpdate(); // Execute the delete query
            return rowsDeleted > 0; // Return true if at least one row was deleted
        } catch (SQLException e) {
            // Log or handle exception as needed
        }

        return false; // Return false if no rows were deleted or an error occurred
    }

    @Override
    public boolean recruiterExists(String username) {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_RECRUITER_EXISTS_SQL)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1); // Get count of matching rows
                    return count > 0; // Return true if count is greater than 0
                }
            }
        } catch (SQLException e) {
            // Log or handle exception as needed
        }

        return false; // Return false if no matching rows are found
    }
}
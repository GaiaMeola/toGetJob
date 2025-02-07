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

    @Override
    public void saveRecruiter(Recruiter recruiter) {
        String sql = "INSERT INTO RECRUITER (Username, Name, Surname, EmailAddress, Password, Role, Companies) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, recruiter.getUsername());
            stmt.setString(2, recruiter.getName());
            stmt.setString(3, recruiter.getSurname());
            stmt.setString(4, recruiter.getEmailAddress());
            stmt.setString(5, recruiter.getPassword());
            stmt.setString(6, recruiter.getRole().name());

            // Convert the list of companies to a comma-separated string
            stmt.setString(7, String.join(",", recruiter.getCompanies()));

            stmt.executeUpdate();  // Execute the insert statement
        } catch (SQLException e) {
            // Handle error without printing stack trace
        }
    }

    @Override
    public Optional<Recruiter> getRecruiter(String username) {
        String sql = "SELECT Username, Name, Surname, EmailAddress, Password, Role, Companies FROM RECRUITER WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Retrieve data from the result set
                String name = rs.getString("Name");
                String surname = rs.getString("Surname");
                String emailAddress = rs.getString("EmailAddress");
                String password = rs.getString("Password");
                Role role = Role.valueOf(rs.getString("Role").toUpperCase());

                // Retrieve companies and convert to a list
                String companiesString = rs.getString("Companies");
                List<String> companies = List.of(companiesString.split(","));

                // Create and return the Recruiter object
                Recruiter recruiter = new Recruiter(name, surname, username, emailAddress, password, role, companies);

                return Optional.of(recruiter);  // Return recruiter
            }

        } catch (SQLException e) {
            // Handle error without printing stack trace
        }

        return Optional.empty();  // Return empty if recruiter not found
    }

    @Override
    public List<Recruiter> getAllRecruiter() {
        String sql = "SELECT Username, Name, Surname, EmailAddress, Password, Role, Companies FROM RECRUITER";
        List<Recruiter> recruiters = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Extract data from result set
                String username = rs.getString("Username");
                String name = rs.getString("Name");
                String surname = rs.getString("Surname");
                String emailAddress = rs.getString("EmailAddress");
                String password = rs.getString("Password");
                Role role = Role.valueOf(rs.getString("Role").toUpperCase());

                // Extract companies and convert to list
                String companiesString = rs.getString("Companies");
                List<String> companies = List.of(companiesString.split(","));

                // Create recruiter object and add to list
                Recruiter recruiter = new Recruiter(name, surname, username, emailAddress, password, role, companies);
                recruiters.add(recruiter); // Add to list
            }

        } catch (SQLException e) {
            // Handle error without printing stack trace
        }

        return recruiters;  // Return list of recruiters
    }

    @Override
    public boolean updateRecruiter(Recruiter recruiter) {
        String sql = "UPDATE RECRUITER SET Name = ?, Surname = ?, EmailAddress = ?, Password = ?, Companies = ? WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set query parameters
            stmt.setString(1, recruiter.getName());
            stmt.setString(2, recruiter.getSurname());
            stmt.setString(3, recruiter.getEmailAddress());
            stmt.setString(4, recruiter.getPassword());

            // Companies must be stored as a comma-separated string
            String companies = String.join(",", recruiter.getCompanies());
            stmt.setString(5, companies);

            stmt.setString(6, recruiter.getUsername());

            // Execute update and return true if at least one record is updated
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            // Handle error without printing stack trace
        }
        return false;  // Return false if no rows are updated
    }

    @Override
    public boolean deleteRecruiter(String username) {
        String sql = "DELETE FROM RECRUITER WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameter for username
            stmt.setString(1, username);

            // Execute delete and return true if at least one row is deleted
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            // Handle error without printing stack trace
        }
        return false;  // Return false if no rows are deleted
    }

    @Override
    public boolean recruiterExists(String username) {
        String sql = "SELECT COUNT(*) FROM RECRUITER WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameter for username
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);  // Get number of matching rows
                    return count > 0;  // Return true if count > 0
                }
            }
        } catch (SQLException e) {
            // Handle error without printing stack trace
        }
        return false;  // Return false if no matching rows are found
    }
}

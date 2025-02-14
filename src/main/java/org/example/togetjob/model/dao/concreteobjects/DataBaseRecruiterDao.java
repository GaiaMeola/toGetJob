package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.exceptions.DatabaseException;
import org.example.togetjob.model.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.dao.abstractobjects.UserDao;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DataBaseRecruiterDao implements RecruiterDao {

    private static final String INSERT_RECRUITER_SQL =
            "INSERT INTO RECRUITER (Username, Companies) VALUES (?, ?)";
    private static final String SELECT_RECRUITER_BY_USERNAME_SQL =
            "SELECT Username, Companies FROM RECRUITER WHERE Username = ?";
    private static final String SELECT_ALL_RECRUITERS_SQL =
            "SELECT Username, Companies FROM RECRUITER";
    private static final String UPDATE_RECRUITER_SQL =
            "UPDATE RECRUITER SET Companies = ? WHERE Username = ?";
    private static final String DELETE_RECRUITER_SQL =
            "DELETE FROM RECRUITER WHERE Username = ?";
    private static final String CHECK_RECRUITER_EXISTS_SQL =
            "SELECT COUNT(*) FROM RECRUITER WHERE Username = ?";

    private final UserDao dataBaseUserDao;

    public DataBaseRecruiterDao(UserDao dataBaseUserDao) {
        this.dataBaseUserDao = dataBaseUserDao;
    }

    @Override
    public void saveRecruiter(Recruiter recruiter) throws DatabaseException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_RECRUITER_SQL)) {

            stmt.setString(1, recruiter.obtainUsername());
            stmt.setString(2, String.join(",", recruiter.obtainCompanies())); // Convert list to comma-separated string
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error saving user to the database");
        }
    }

    @Override
    public Optional<Recruiter> getRecruiter(String username) {

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_RECRUITER_BY_USERNAME_SQL)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();


            if (rs.next()) {
                String companiesString = rs.getString("Companies");
                List<String> companies = Arrays.stream(companiesString.split(","))
                        .map(String::trim)
                        .toList();

                Optional<User> userOptional = dataBaseUserDao.getUser(username);
                if (userOptional.isPresent()) {
                    User user = userOptional.get();
                    Recruiter recruiter = new Recruiter(user.obtainName(), user.obtainSurname(), user.obtainUsername(),
                            user.obtainEmailAddress(), user.obtainPassword(),
                            user.obtainRole(), companies);
                    return Optional.of(recruiter);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error obtaining recruiter from database");
        }

        return Optional.empty();
    }

    @Override
    public boolean updateRecruiter(Recruiter recruiter) {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_RECRUITER_SQL)) {

            stmt.setString(1, String.join(",", recruiter.obtainCompanies())); // Convert list to comma-separated string
            stmt.setString(2, recruiter.obtainUsername());

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating Recruiter");
        }
    }

    @Override
    public boolean deleteRecruiter(String username) {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_RECRUITER_SQL)) {

            stmt.setString(1, username);
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting Recruiter");
        }
    }

    @Override
    public boolean recruiterExists(String username) {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_RECRUITER_EXISTS_SQL)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error Recruiter not found");
        }

        return false;
    }
}

package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Recruiter;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DataBaseJobAnnouncementDao implements JobAnnouncementDao {

    private static final String SELECT_JOB_ANNOUNCEMENT_BY_ID =
            "SELECT JobTitle, JobType, RoleJob, Location, WorkingHours, CompanyName, Salary, Description, isActive, RecruiterName FROM JOBANNOUNCEMENT WHERE ID = ?";

    private static final String SELECT_JOB_ANNOUNCEMENT_BY_TITLE_AND_RECRUITER =
            "SELECT JobTitle, JobType, RoleJob, Location, WorkingHours, CompanyName, Salary, Description, isActive, RecruiterName" +
                    "FROM JOBANNOUNCEMENT" +
                    "WHERE JobTitle = ? AND RecruiterName = ? ";

    private static final String SELECT_ALL_JOB_ANNOUNCEMENTS =
            "SELECT JobTitle, JobType, RoleJob, Location, WorkingHours, CompanyName, Salary, Description, isActive, RecruiterName" +
                    "FROM JOBANNOUNCEMENT";

    private static final String UPDATE_JOB_ANNOUNCEMENT =
            "UPDATE JOBANNOUNCEMENT SET JobTitle = ?, JobType = ?, RoleJob = ?, Location = ?, WorkingHours = ?, CompanyName = ?, Salary = ?, Description = ?, isActive = ? WHERE ID = ?";

    private static final String DELETE_JOB_ANNOUNCEMENT =
            "DELETE FROM JOBANNOUNCEMENT WHERE ID = ?";

    private static final String SELECT_JOB_ANNOUNCEMENT_ID =
            "SELECT ID FROM JOBANNOUNCEMENT WHERE JobTitle = ? AND RecruiterName = ?";

    private final DataBaseRecruiterDao daoRecruiter;

    public DataBaseJobAnnouncementDao(DataBaseRecruiterDao daoRecruiter) {
        this.daoRecruiter = daoRecruiter;
    }

    @Override
    public boolean saveJobAnnouncement(JobAnnouncement jobAnnouncement) {
        String sql = "INSERT INTO JOBANNOUNCEMENT (JobTitle, JobType, RoleJob, Location, WorkingHours, CompanyName, Salary, Description, isActive, RecruiterName) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, jobAnnouncement.getJobTitle());
            stmt.setString(2, jobAnnouncement.getJobType());
            stmt.setString(3, jobAnnouncement.getRole());
            stmt.setString(4, jobAnnouncement.getLocation());
            stmt.setInt(5, jobAnnouncement.getWorkingHours());
            stmt.setString(6, jobAnnouncement.getCompanyName());
            stmt.setDouble(7, jobAnnouncement.getSalary());
            stmt.setString(8, jobAnnouncement.getDescription());
            stmt.setBoolean(9, jobAnnouncement.getActive());
            stmt.setString(10, jobAnnouncement.getRecruiter().getUsername());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Log error if needed or handle exception
            return false;
        }
    }

    @Override
    public Optional<JobAnnouncement> getJobAnnouncement(String jobTitle, Recruiter recruiter) {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_JOB_ANNOUNCEMENT_BY_TITLE_AND_RECRUITER)) {

            stmt.setString(1, jobTitle);
            stmt.setString(2, recruiter.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(createJobAnnouncementFromResultSet(rs, recruiter));
                }
            }
        } catch (SQLException e) {
            // Log error if needed or handle exception
        }
        return Optional.empty();
    }

    @Override
    public boolean updateJobAnnouncement(JobAnnouncement jobAnnouncement) {
        Optional<Integer> jobAnnouncementIdOpt = getJobAnnouncementId(jobAnnouncement);

        if (jobAnnouncementIdOpt.isEmpty()) {
            return false;
        }

        Integer jobAnnouncementId = jobAnnouncementIdOpt.get();

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_JOB_ANNOUNCEMENT)) {

            stmt.setString(1, jobAnnouncement.getJobTitle());
            stmt.setString(2, jobAnnouncement.getJobType());
            stmt.setString(3, jobAnnouncement.getRole());
            stmt.setString(4, jobAnnouncement.getLocation());
            stmt.setInt(5, jobAnnouncement.getWorkingHours());
            stmt.setString(6, jobAnnouncement.getCompanyName());
            stmt.setDouble(7, jobAnnouncement.getSalary());
            stmt.setString(8, jobAnnouncement.getDescription());
            stmt.setBoolean(9, jobAnnouncement.getActive());
            stmt.setInt(10, jobAnnouncementId); // Correct index for ID

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Log error if needed or handle exception
            return false;
        }
    }

    @Override
    public boolean deleteJobAnnouncement(JobAnnouncement jobAnnouncement) {
        Optional<Integer> jobAnnouncementIdOpt = getJobAnnouncementId(jobAnnouncement);

        if (jobAnnouncementIdOpt.isEmpty()) {
            return false;
        }

        Integer jobAnnouncementId = jobAnnouncementIdOpt.get();

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_JOB_ANNOUNCEMENT)) {

            stmt.setInt(1, jobAnnouncementId); // Correct index for ID
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            // Log error if needed or handle exception
            return false;
        }
    }

    @Override
    public boolean jobAnnouncementExists(String jobTitle, Recruiter recruiter) {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_JOB_ANNOUNCEMENT_BY_TITLE_AND_RECRUITER)) {

            stmt.setString(1, jobTitle);
            stmt.setString(2, recruiter.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Correct column index for COUNT
                }
            }
        } catch (SQLException e) {
            // Log error if needed or handle exception
        }
        return false;
    }

    @Override
    public List<JobAnnouncement> getAllJobAnnouncements(Recruiter recruiter) {
        String sql = "SELECT JobTitle, JobType, RoleJob, Location, WorkingHours, CompanyName, Salary, Description, isActive, RecruiterName " +
                "FROM JOBANNOUNCEMENT WHERE RecruiterName = ?";
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, recruiter.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                List<JobAnnouncement> jobAnnouncements = new ArrayList<>();
                while (rs.next()) {
                    jobAnnouncements.add(createJobAnnouncementFromResultSet(rs, recruiter));
                }
                return jobAnnouncements;
            }
        } catch (SQLException e) {
            // Log error if needed or handle exception
        }
        return Collections.emptyList();
    }

    @Override
    public List<JobAnnouncement> getAllJobAnnouncements() {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_JOB_ANNOUNCEMENTS)) {

            try (ResultSet rs = stmt.executeQuery()) {
                List<JobAnnouncement> jobAnnouncements = new ArrayList<>();
                while (rs.next()) {
                    String recruiterName = rs.getString("RecruiterName");
                    Optional<Recruiter> recruiter = daoRecruiter.getRecruiter(recruiterName);
                    jobAnnouncements.add(createJobAnnouncementFromResultSet(rs, recruiter.orElse(null)));
                }
                return jobAnnouncements;
            }
        } catch (SQLException e) {
            // Log error if needed or handle exception
        }
        return Collections.emptyList();
    }

    @Override
    public Optional<Integer> getJobAnnouncementId(String jobTitle, String recruiterName) {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_JOB_ANNOUNCEMENT_ID)) {

            stmt.setString(1, jobTitle);
            stmt.setString(2, recruiterName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(rs.getInt("ID")); // Correct column name for ID
                }
            }
        } catch (SQLException e) {
            // Log error if needed or handle exception
        }
        return Optional.empty();
    }

    @Override
    public Optional<JobAnnouncement> getJobAnnouncementById(Integer jobAnnouncementId) {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_JOB_ANNOUNCEMENT_BY_ID)) {

            stmt.setInt(1, jobAnnouncementId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String recruiterName = rs.getString("RecruiterName");
                    Optional<Recruiter> recruiter = daoRecruiter.getRecruiter(recruiterName);
                    return recruiter.map(r -> {
                        try {
                            return createJobAnnouncementFromResultSet(rs, r);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        } catch (SQLException e) {
            // Log error if needed or handle exception
        }
        return Optional.empty();
    }

    private Optional<Integer> getJobAnnouncementId(JobAnnouncement jobAnnouncement) {
        return getJobAnnouncementId(jobAnnouncement.getJobTitle(), jobAnnouncement.getRecruiter().getUsername());
    }

    private JobAnnouncement createJobAnnouncementFromResultSet(ResultSet rs, Recruiter recruiter) throws SQLException {
        String jobTitle = rs.getString("JobTitle");
        String jobType = rs.getString("JobType");
        String role = rs.getString("RoleJob");
        String location = rs.getString("Location");
        int workingHours = rs.getInt("WorkingHours");
        String companyName = rs.getString("CompanyName");
        double salary = rs.getDouble("Salary");
        String description = rs.getString("Description");
        boolean isActive = rs.getBoolean("isActive");

        return new JobAnnouncement(jobTitle, jobType, role, location, workingHours, companyName, salary, description, isActive, recruiter);
    }
}

package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.exceptions.DatabaseConnectionException;
import org.example.togetjob.exceptions.DatabaseException;
import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.factory.JobAnnouncementFactory;

import java.sql.*;
import java.util.*;

public class DataBaseJobAnnouncementDao implements JobAnnouncementDao {

    private static final String INSERT_JOB_ANNOUNCEMENT =
            "INSERT INTO JOBANNOUNCEMENT (JobTitle, JobType, RoleJob, Location, WorkingHours, CompanyName, Salary, Description, isActive, RecruiterName) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SELECT_JOB_ANNOUNCEMENTS_BY_RECRUITER =
            "SELECT * FROM JOBANNOUNCEMENT WHERE RecruiterName = ?";
    private static final String SELECT_ALL_JOB_ANNOUNCEMENTS =
            "SELECT * FROM JOBANNOUNCEMENT";
    private static final String DELETE_JOB_ANNOUNCEMENT =
            "DELETE FROM JOBANNOUNCEMENT WHERE ID = ?";
    private static final String SELECT_JOB_ID =
            "SELECT ID FROM JOBANNOUNCEMENT WHERE JobTitle = ? AND RecruiterName = ?";
    private static final String CHECK_EXISTENCE =
            "SELECT COUNT(*) FROM JOBANNOUNCEMENT WHERE JobTitle = ? AND RecruiterName = ?";
    private static final String SELECT_JOB_ANNOUNCEMENT_BY_ID =
            "SELECT JobTitle, JobType, RoleJob, Location, WorkingHours, CompanyName, Salary, Description, isActive, RecruiterName FROM JOBANNOUNCEMENT WHERE ID = ?";

    private final RecruiterDao recruiterDao;

    public DataBaseJobAnnouncementDao(RecruiterDao recruiterDao) {
        this.recruiterDao = recruiterDao;
    }

    @Override
    public boolean saveJobAnnouncement(JobAnnouncement jobAnnouncement) throws DatabaseException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(INSERT_JOB_ANNOUNCEMENT)) {

            stmt.setString(1, jobAnnouncement.obtainJobTitle());
            stmt.setString(2, jobAnnouncement.obtainJobType());
            stmt.setString(3, jobAnnouncement.obtainJobRole());
            stmt.setString(4, jobAnnouncement.obtainLocation());
            stmt.setInt(5, jobAnnouncement.obtainWorkingHours());
            stmt.setString(6, jobAnnouncement.obtainCompanyName());
            stmt.setDouble(7, jobAnnouncement.obtainSalary());
            stmt.setString(8, jobAnnouncement.obtainDescription());
            stmt.setBoolean(9, jobAnnouncement.isJobActive());
            stmt.setString(10, jobAnnouncement.getRecruiter().obtainUsername());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Error saving job announcement to the database", e);
        }
    }

    @Override
    public boolean updateJobAnnouncement(JobAnnouncement jobAnnouncement) {
        return false;
    }

    @Override
    public boolean deleteJobAnnouncement(JobAnnouncement jobAnnouncement) throws DatabaseException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_JOB_ANNOUNCEMENT)) {

            int jobId = getJobAnnouncementId(jobAnnouncement.obtainJobTitle(), jobAnnouncement.getRecruiter().obtainUsername())
                    .orElseThrow(() -> new DatabaseException("Job announcement not found"));

            stmt.setInt(1, jobId);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Error deleting job announcement", e);
        }
    }

    @Override
    public boolean jobAnnouncementExists(String jobTitle, Recruiter recruiter) throws DatabaseException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(CHECK_EXISTENCE)) {

            stmt.setString(1, jobTitle);
            stmt.setString(2, recruiter.obtainUsername());
            ResultSet rs = stmt.executeQuery();

            return rs.next() && rs.getInt(1) > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Error checking job announcement existence", e);
        }
    }

    @Override
    public List<JobAnnouncement> getAllJobAnnouncements(Recruiter recruiter) throws DatabaseException {
        return getJobAnnouncementsByQuery(SELECT_JOB_ANNOUNCEMENTS_BY_RECRUITER, recruiter.obtainUsername());
    }

    @Override
    public List<JobAnnouncement> getAllJobAnnouncements() throws DatabaseConnectionException {
        return getJobAnnouncementsByQuery(SELECT_ALL_JOB_ANNOUNCEMENTS, null);
    }

    @Override
    public Optional<JobAnnouncement> getJobAnnouncement(String jobTitle, Recruiter recruiter) throws DatabaseException {
        return getJobAnnouncementId(jobTitle, recruiter.obtainUsername())
                .flatMap(this::getJobAnnouncementById);
    }

    @Override
    public Optional<Integer> getJobAnnouncementId(String jobTitle, String recruiterName) throws DatabaseException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_JOB_ID)) {

            stmt.setString(1, jobTitle);
            stmt.setString(2, recruiterName);
            ResultSet rs = stmt.executeQuery();

            return rs.next() ? Optional.of(rs.getInt("ID")) : Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving job announcement ID", e);
        }
    }

    @Override
    public Optional<JobAnnouncement> getJobAnnouncementById(int jobAnnouncementId) throws DatabaseException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_JOB_ANNOUNCEMENT_BY_ID)) {

            stmt.setInt(1, jobAnnouncementId);
            ResultSet rs = stmt.executeQuery();

            return rs.next() ? Optional.of(buildJobAnnouncement(rs)) : Optional.empty();

        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving job announcement by ID", e);
        }
    }

    private List<JobAnnouncement> getJobAnnouncementsByQuery(String query, String recruiterName) throws DatabaseException {
        List<JobAnnouncement> jobAnnouncements = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            if (recruiterName != null) {
                stmt.setString(1, recruiterName);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    jobAnnouncements.add(buildJobAnnouncement(rs));
                }
            }

        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving job announcements", e);
        }

        return jobAnnouncements;
    }

    private JobAnnouncement buildJobAnnouncement(ResultSet rs) throws SQLException {
        JobAnnouncement jobAnnouncement = JobAnnouncementFactory.createJobAnnouncement(
                rs.getString("JobTitle"),
                rs.getString("JobType"),
                rs.getString("RoleJob"),
                rs.getString("Location"),
                rs.getInt("WorkingHours"),
                rs.getString("CompanyName"),
                rs.getDouble("Salary")
        );

        JobAnnouncementFactory.completeJobAnnouncement(
                jobAnnouncement,
                rs.getString("Description"),
                null,
                rs.getBoolean("isActive")
        );

        String recruiterName = rs.getString("RecruiterName");
        Optional<Recruiter> recruiterOpt = recruiterDao.getRecruiter(recruiterName);
        recruiterOpt.ifPresent(jobAnnouncement::setRecruiter);

        return jobAnnouncement;
    }
}

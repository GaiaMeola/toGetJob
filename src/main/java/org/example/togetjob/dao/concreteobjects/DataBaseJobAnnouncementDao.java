package org.example.togetjob.dao.concreteobjects;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.exceptions.DatabaseConnectionException;
import org.example.togetjob.exceptions.DatabaseException;
import org.example.togetjob.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.factory.JobAnnouncementFactory;

import java.sql.*;
import java.util.*;

public class DataBaseJobAnnouncementDao implements JobAnnouncementDao {

    private static final String INSERT_JOB_ANNOUNCEMENT =
            "INSERT INTO JOBANNOUNCEMENT (JobTitle, JobType, RoleJob, Location, WorkingHours, CompanyName, Salary, Description, isActive, RecruiterName) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_JOB_ANNOUNCEMENT_BY_ID =
            "SELECT ID, JobTitle, JobType, RoleJob, Location, WorkingHours, CompanyName, Salary, Description, isActive, RecruiterName FROM JOBANNOUNCEMENT WHERE ID = ?";

    private static final String SELECT_ALL_JOB_ANNOUNCEMENTS =
            "SELECT ID, JobTitle, JobType, RoleJob, Location, WorkingHours, CompanyName, Salary, Description, isActive, RecruiterName FROM JOBANNOUNCEMENT";

    private static final String DELETE_JOB_ANNOUNCEMENT =
            "DELETE FROM JOBANNOUNCEMENT WHERE ID = ?";

    private static final String SELECT_JOB_ID =
            "SELECT ID FROM JOBANNOUNCEMENT WHERE JobTitle = ? AND RecruiterName = ?";

    private static final String UPDATE_JOB_ANNOUNCEMENT =
            "UPDATE JOBANNOUNCEMENT SET JobTitle = ?, JobType = ?, RoleJob = ?, Location = ?, WorkingHours = ?, CompanyName = ?, Salary = ?, Description = ?, isActive = ?, RecruiterName = ? WHERE ID = ?";

    private static final String SELECT_JOB_ANNOUNCEMENTS_BY_RECRUITER =
            "SELECT ID, JobTitle, JobType, RoleJob, Location, WorkingHours, CompanyName, Salary, Description, isActive, RecruiterName " +
                    "FROM JOBANNOUNCEMENT WHERE RecruiterName = ?";

    private static final String COLUMN_JOB_TITLE = "JobTitle";
    private static final String COLUMN_JOB_TYPE = "JobType";
    private static final String COLUMN_ROLE = "RoleJob";
    private static final String COLUMN_LOCATION = "Location";
    private static final String COLUMN_WORKING_HOURS = "WorkingHours";
    private static final String COLUMN_COMPANY_NAME = "CompanyName";
    private static final String COLUMN_SALARY = "Salary";
    private static final String COLUMN_DESCRIPTION = "Description";
    private static final String COLUMN_IS_ACTIVE = "isActive";
    private static final String COLUMN_RECRUITER_NAME = "RecruiterName";

    private final DataBaseRecruiterDao recruiterDao;

    public DataBaseJobAnnouncementDao(DataBaseRecruiterDao recruiterDao) {
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
            throw new DatabaseException("Error saving job announcement to the database");
        }
    }

    @Override
    public boolean updateJobAnnouncement(JobAnnouncement jobAnnouncement) throws DatabaseException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(UPDATE_JOB_ANNOUNCEMENT)) {

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

            int jobId = getJobAnnouncementId(jobAnnouncement.obtainJobTitle(), jobAnnouncement.getRecruiter().obtainUsername())
                    .orElseThrow(() -> new DatabaseException("Job announcement not found"));

            stmt.setInt(11, jobId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating job announcement");
        }
    }

    public boolean deleteJobAnnouncement(JobAnnouncement jobAnnouncement) throws DatabaseException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(DELETE_JOB_ANNOUNCEMENT)) {

            Optional<Integer> jobIdOpt = getJobAnnouncementId(jobAnnouncement.obtainJobTitle(), jobAnnouncement.getRecruiter().obtainUsername());

            if (jobIdOpt.isEmpty()) {
                return false;
            }

            int jobId = jobIdOpt.get();
            stmt.setInt(1, jobId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting job announcement", e);
        }
    }

    @Override
    public List<JobAnnouncement> getAllJobAnnouncements(Recruiter recruiter) {
        List<JobAnnouncement> jobAnnouncements = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_JOB_ANNOUNCEMENTS_BY_RECRUITER)) {

            stmt.setString(1, recruiter.obtainUsername());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    jobAnnouncements.add(createJobAnnouncementFromResultSet(rs, recruiter));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving job announcements for recruiter: " + recruiter.obtainUsername());
        }

        return jobAnnouncements;
    }


    @Override
    public List<JobAnnouncement> getAllJobAnnouncements() throws DatabaseConnectionException {
        List<JobAnnouncement> jobAnnouncements = new ArrayList<>();
        List<String> recruiterUsernames = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_ALL_JOB_ANNOUNCEMENTS);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String jobTitle = rs.getString(COLUMN_JOB_TITLE);
                String jobType = rs.getString(COLUMN_JOB_TYPE);
                String role = rs.getString(COLUMN_ROLE);
                String location = rs.getString(COLUMN_LOCATION);
                int workingHours = rs.getInt(COLUMN_WORKING_HOURS);
                String companyName = rs.getString(COLUMN_COMPANY_NAME);
                double salary = rs.getDouble(COLUMN_SALARY);
                String description = rs.getString(COLUMN_DESCRIPTION);
                boolean isActive = rs.getBoolean(COLUMN_IS_ACTIVE);
                String recruiterName = rs.getString(COLUMN_RECRUITER_NAME);

                JobAnnouncement jobAnnouncement = JobAnnouncementFactory.createJobAnnouncement(
                        jobTitle, jobType, role, location, workingHours, companyName, salary);
                org.example.togetjob.model.factory.JobAnnouncementFactory.completeJobAnnouncement(jobAnnouncement, description, null, isActive);
                jobAnnouncements.add(jobAnnouncement);
                recruiterUsernames.add(recruiterName);
            }

        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error retrieving job announcements from the database");
        }

        for (int i = 0; i < jobAnnouncements.size(); i++) {
            String recruiterName = recruiterUsernames.get(i);

            Optional<Recruiter> recruiterOptional = recruiterDao.getRecruiter(recruiterName);
            if (recruiterOptional.isPresent()) {
                Recruiter recruiter = recruiterOptional.get();
                jobAnnouncements.get(i).setRecruiter(recruiter);
            } else {
                jobAnnouncements.get(i).setRecruiter(null);
            }
        }

        return jobAnnouncements;
    }

    @Override
    public Optional<JobAnnouncement> getJobAnnouncement(String jobTitle, Recruiter recruiter) throws DatabaseException {
        return getJobAnnouncementId(jobTitle, recruiter.obtainUsername())
                .flatMap(this::getJobAnnouncementById);
    }

    public Optional<Integer> getJobAnnouncementId(String jobTitle, String recruiterName) throws DatabaseException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_JOB_ID)) {

            stmt.setString(1, jobTitle);
            stmt.setString(2, recruiterName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(rs.getInt("ID"));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving job announcement ID");
        }
        return Optional.empty();
    }

    public Optional<JobAnnouncement> getJobAnnouncementById(int jobAnnouncementId) throws DatabaseException {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SELECT_JOB_ANNOUNCEMENT_BY_ID)) {

            stmt.setInt(1, jobAnnouncementId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                String jobTitle = rs.getString(COLUMN_JOB_TITLE);
                String jobType = rs.getString(COLUMN_JOB_TYPE);
                String role = rs.getString(COLUMN_ROLE);
                String location = rs.getString(COLUMN_LOCATION);
                int workingHours = rs.getInt(COLUMN_WORKING_HOURS);
                String companyName = rs.getString(COLUMN_COMPANY_NAME);
                double salary = rs.getDouble(COLUMN_SALARY);
                String description = rs.getString(COLUMN_DESCRIPTION);
                boolean isActive = rs.getBoolean(COLUMN_IS_ACTIVE);
                String recruiterName = rs.getString(COLUMN_RECRUITER_NAME);

                JobAnnouncement jobAnnouncement = org.example.togetjob.model.factory.JobAnnouncementFactory.createJobAnnouncement(
                        jobTitle, jobType, role, location, workingHours, companyName, salary);
                org.example.togetjob.model.factory.JobAnnouncementFactory.completeJobAnnouncement(jobAnnouncement, description, null, isActive);

                Optional<Recruiter> recruiterOpt = recruiterDao.getRecruiter(recruiterName);
                if (recruiterOpt.isPresent()) {
                    jobAnnouncement.setRecruiter(recruiterOpt.get());
                } else {
                    jobAnnouncement.setRecruiter(null);
                }

                return Optional.of(jobAnnouncement);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving job announcement by ID", e);
        }
    }

    private JobAnnouncement createJobAnnouncementFromResultSet(ResultSet rs, Recruiter recruiter) throws SQLException {

        JobAnnouncement jobAnnouncement = org.example.togetjob.model.factory.JobAnnouncementFactory.createJobAnnouncement(rs.getString(COLUMN_JOB_TITLE), rs.getString(COLUMN_JOB_TYPE), rs.getString(COLUMN_ROLE), rs.getString(COLUMN_LOCATION), rs.getInt(COLUMN_WORKING_HOURS), rs.getString(COLUMN_COMPANY_NAME), rs.getDouble(COLUMN_SALARY));
        org.example.togetjob.model.factory.JobAnnouncementFactory.completeJobAnnouncement(jobAnnouncement, rs.getString(COLUMN_DESCRIPTION), recruiter, rs.getBoolean(COLUMN_IS_ACTIVE));
        return jobAnnouncement;

    }

}

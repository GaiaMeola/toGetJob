package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.model.dao.abstractobjects.JobApplicationDao;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.model.entity.Status;
import org.example.togetjob.model.entity.Student;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataBaseJobApplicationDao implements JobApplicationDao {

    // Column names as constants
    private static final String COLUMN_APPLICATION_DATE = "ApplicationDate";
    private static final String COLUMN_STATUS = "Status";
    private static final String COLUMN_COVER_LETTER = "CoverLetter";
    private static final String COLUMN_JOB_ANNOUNCEMENT_ID = "JobAnnouncementID";
    private static final String COLUMN_USERNAME_STUDENT = "UsernameStudent";

    // Error message constants
    private static final String ERROR_JOB_ANNOUNCEMENT_NOT_FOUND = "Job Announcement not found";
    private static final String ERROR_JOB_APPLICATION_NOT_FOUND = "Job Application not found";
    private static final String ERROR_DATABASE = "Database error while saving job application";

    // SQL query constants
    private static final String SQL_INSERT_JOB_APPLICATION = "INSERT INTO JOBAPPLICATION (ApplicationDate, UsernameStudent, Status, CoverLetter, JobAnnouncementID) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_JOB_APPLICATION = "SELECT ApplicationDate, Status, CoverLetter FROM JOBAPPLICATION WHERE UsernameStudent = ? AND JobAnnouncementID = ?";

    private static final String SQL_UPDATE_JOB_APPLICATION = "UPDATE JOBAPPLICATION SET Status = ?, CoverLetter = ? WHERE UsernameStudent = ? AND JobAnnouncementID = ?";

    private static final String SQL_DELETE_JOB_APPLICATION = "DELETE FROM JOBAPPLICATION WHERE UsernameStudent = ? AND JobAnnouncementID = ?";

    private static final String SQL_CHECK_JOB_APPLICATION_EXISTS = "SELECT 1 FROM JOBAPPLICATION WHERE UsernameStudent = ? AND JobAnnouncementID = ? LIMIT 1";

    private static final String SQL_SELECT_ALL_JOB_APPLICATIONS = "SELECT ApplicationDate, Status, CoverLetter, JobAnnouncementID, UsernameStudent "
            + "FROM JOBAPPLICATION "
            + "JOIN JobAnnouncement ON JOBAPPLICATION.JobAnnouncementID = JobAnnouncement.ID "
            + "WHERE UsernameStudent = ?";

    private static final String SQL_SELECT_JOB_APPLICATIONS_BY_ANNOUNCEMENT = "SELECT ApplicationDate, Status, CoverLetter, UsernameStudent "
            + "FROM JOBAPPLICATION "
            + "WHERE JobAnnouncementID = ?";


    DataBaseJobAnnouncementDao dataBaseJobAnnouncementDao;
    DataBaseStudentDao dataBaseStudentDao;

    public DataBaseJobApplicationDao(DataBaseJobAnnouncementDao dataBaseJobAnnouncementDao, DataBaseStudentDao dataBaseStudentDao) {
        this.dataBaseJobAnnouncementDao = dataBaseJobAnnouncementDao;
        this.dataBaseStudentDao = dataBaseStudentDao;
    }

    @Override
    public void saveJobApplication(JobApplication jobApplication) {
        try {
            // Retrieve JobAnnouncement ID
            Optional<Integer> jobAnnouncementId = dataBaseJobAnnouncementDao.getJobAnnouncementId(
                    jobApplication.getJobAnnouncement().getJobTitle(),
                    jobApplication.getJobAnnouncement().getRecruiter().getUsername()
            );

            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new RuntimeException(ERROR_JOB_ANNOUNCEMENT_NOT_FOUND));

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_JOB_APPLICATION)) {

                stmt.setDate(1, Date.valueOf(jobApplication.getApplicationDate()));
                stmt.setString(2, jobApplication.getStudent().getUsername());
                stmt.setString(3, jobApplication.getStatus().toString());
                stmt.setString(4, jobApplication.getCoverLetter());
                stmt.setInt(5, jobAnnouncementIdValue);

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(ERROR_DATABASE + ": " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<JobApplication> getJobApplication(Student student, JobAnnouncement jobAnnouncement) {
        try {
            Optional<Integer> jobAnnouncementId = dataBaseJobAnnouncementDao.getJobAnnouncementId(
                    jobAnnouncement.getJobTitle(),
                    jobAnnouncement.getRecruiter().getUsername()
            );

            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new RuntimeException(ERROR_JOB_ANNOUNCEMENT_NOT_FOUND));

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_JOB_APPLICATION)) {

                stmt.setString(1, student.getUsername());
                stmt.setInt(2, jobAnnouncementIdValue);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        LocalDate applicationDate = rs.getDate(COLUMN_APPLICATION_DATE).toLocalDate();
                        Status status = Status.valueOf(rs.getString(COLUMN_STATUS).toUpperCase());
                        String coverLetter = rs.getString(COLUMN_COVER_LETTER);

                        JobApplication jobApplication = new JobApplication(applicationDate, student, status, coverLetter, jobAnnouncement);
                        return Optional.of(jobApplication);
                    }
                }
            }
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(ERROR_JOB_APPLICATION_NOT_FOUND + ": " + e.getMessage(), e);
        }

        return Optional.empty();
    }

    @Override
    public boolean updateJobApplication(JobApplication jobApplication) {
        try {
            Optional<Integer> jobAnnouncementId = dataBaseJobAnnouncementDao.getJobAnnouncementId(
                    jobApplication.getJobAnnouncement().getJobTitle(),
                    jobApplication.getJobAnnouncement().getRecruiter().getUsername()
            );

            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new RuntimeException(ERROR_JOB_ANNOUNCEMENT_NOT_FOUND));

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_JOB_APPLICATION)) {

                stmt.setString(1, jobApplication.getStatus().toString());
                stmt.setString(2, jobApplication.getCoverLetter());
                stmt.setString(3, jobApplication.getStudent().getUsername());
                stmt.setInt(4, jobAnnouncementIdValue);

                stmt.executeUpdate();
                return true;
            }
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(ERROR_DATABASE + ": " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteJobApplication(JobApplication jobApplication) {
        try {
            Optional<Integer> jobAnnouncementId = dataBaseJobAnnouncementDao.getJobAnnouncementId(
                    jobApplication.getJobAnnouncement().getJobTitle(),
                    jobApplication.getJobAnnouncement().getRecruiter().getUsername()
            );

            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new RuntimeException(ERROR_JOB_ANNOUNCEMENT_NOT_FOUND));

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_JOB_APPLICATION)) {

                stmt.setString(1, jobApplication.getStudent().getUsername());
                stmt.setInt(2, jobAnnouncementIdValue);
                stmt.executeUpdate();
            }
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(ERROR_DATABASE + ": " + e.getMessage(), e);
        }
    }

    @Override
    public boolean jobApplicationExists(Student student, JobAnnouncement jobAnnouncement) {
        try {
            Optional<Integer> jobAnnouncementId = dataBaseJobAnnouncementDao.getJobAnnouncementId(
                    jobAnnouncement.getJobTitle(),
                    jobAnnouncement.getRecruiter().getUsername()
            );

            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new RuntimeException(ERROR_JOB_ANNOUNCEMENT_NOT_FOUND));

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_CHECK_JOB_APPLICATION_EXISTS)) {

                stmt.setString(1, student.getUsername());
                stmt.setInt(2, jobAnnouncementIdValue);

                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next(); // If a row is returned, the job application exists
                }
            }
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(ERROR_DATABASE + ": " + e.getMessage(), e);
        }
    }

    @Override
    public List<JobApplication> getAllJobApplications(Student student) {
        List<JobApplication> jobApplications = new ArrayList<>();

        try {
            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_JOB_APPLICATIONS)) {

                stmt.setString(1, student.getUsername());

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        LocalDate applicationDate = rs.getDate(COLUMN_APPLICATION_DATE).toLocalDate();
                        Status status = Status.valueOf(rs.getString(COLUMN_STATUS).toUpperCase());
                        String coverLetter = rs.getString(COLUMN_COVER_LETTER);

                        int jobAnnouncementId = rs.getInt(COLUMN_JOB_ANNOUNCEMENT_ID);
                        JobAnnouncement jobAnnouncement = dataBaseJobAnnouncementDao.getJobAnnouncementById(jobAnnouncementId)
                                .orElseThrow(() -> new RuntimeException(ERROR_JOB_ANNOUNCEMENT_NOT_FOUND));

                        JobApplication jobApplication = new JobApplication(applicationDate, student, status, coverLetter, jobAnnouncement);
                        jobApplications.add(jobApplication);
                    }
                }
            }
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(ERROR_DATABASE + ": " + e.getMessage(), e);
        }

        return jobApplications;
    }

    @Override
    public List<JobApplication> getJobApplicationsByAnnouncement(JobAnnouncement jobAnnouncement) {
        List<JobApplication> jobApplications = new ArrayList<>();

        try {
            Optional<Integer> jobAnnouncementId = dataBaseJobAnnouncementDao.getJobAnnouncementId(
                    jobAnnouncement.getJobTitle(),
                    jobAnnouncement.getRecruiter().getUsername()
            );

            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new RuntimeException(ERROR_JOB_ANNOUNCEMENT_NOT_FOUND));

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_JOB_APPLICATIONS_BY_ANNOUNCEMENT)) {

                stmt.setInt(1, jobAnnouncementIdValue);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        LocalDate applicationDate = rs.getDate(COLUMN_APPLICATION_DATE).toLocalDate();
                        Status status = Status.valueOf(rs.getString(COLUMN_STATUS).toUpperCase());
                        String coverLetter = rs.getString(COLUMN_COVER_LETTER);

                        String studentUsername = rs.getString(COLUMN_USERNAME_STUDENT);
                        Student student = dataBaseStudentDao.getStudent(studentUsername)
                                .orElseThrow(() -> new RuntimeException(ERROR_JOB_APPLICATION_NOT_FOUND));

                        JobApplication jobApplication = new JobApplication(applicationDate, student, status, coverLetter, jobAnnouncement);
                        jobApplications.add(jobApplication);
                    }
                }
            }
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(ERROR_DATABASE + ": " + e.getMessage(), e);
        }

        return jobApplications;
    }

}
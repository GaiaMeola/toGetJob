package org.example.togetjob.dao.concreteobjects;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.exceptions.DatabaseException;
import org.example.togetjob.dao.abstractobjects.JobApplicationDao;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.JobApplication;
import org.example.togetjob.model.entity.Status;
import org.example.togetjob.model.entity.Student;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class DataBaseJobApplicationDao implements JobApplicationDao {

    // Column names as constants
    private static final String COLUMN_APPLICATION_DATE = "ApplicationDate";
    private static final String COLUMN_STATUS = "Status";
    private static final String COLUMN_COVER_LETTER = "CoverLetter";
    private static final String COLUMN_JOB_ANNOUNCEMENT_ID = "JobAnnouncementID";
    private static final String COLUMN_USERNAME_STUDENT = "UsernameStudent";

    // Error message constants
    private static final String ERROR_JOB_ANNOUNCEMENT_NOT_FOUND = "Job Announcement not found";
    private static final String ERROR_DATABASE = "Database error while saving job application";
    private static final String ERROR_DATABASE_EXECUTION_FAILED = "Error executing the insert query";
    private static final String ERROR_UNEXPECTED_ERROR = "Unexpected error occurred while saving the job application";
    private static final String ERROR_STUDENT_NOT_FOUND = "Student not found in the database.";


    // SQL query constants
    private static final String SQL_INSERT_JOB_APPLICATION = "INSERT INTO JOBAPPLICATION (ApplicationDate, UsernameStudent, Status, CoverLetter, JobAnnouncementID) VALUES (?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_JOB_APPLICATION = "SELECT ApplicationDate, Status, CoverLetter FROM JOBAPPLICATION WHERE UsernameStudent = ? AND JobAnnouncementID = ?";

    private static final String SQL_UPDATE_JOB_APPLICATION = "UPDATE JOBAPPLICATION SET Status = ?, CoverLetter = ? WHERE UsernameStudent = ? AND JobAnnouncementID = ?";

    private static final String SQL_DELETE_JOB_APPLICATION = "DELETE FROM JOBAPPLICATION WHERE UsernameStudent = ? AND JobAnnouncementID = ?";

    private static final String SQL_SELECT_ALL_JOB_APPLICATIONS = "SELECT ApplicationDate, Status, CoverLetter, JobAnnouncementID, UsernameStudent "
            + "FROM JOBAPPLICATION "
            + "JOIN JobAnnouncement ON JOBAPPLICATION.JobAnnouncementID = JobAnnouncement.ID "
            + "WHERE UsernameStudent = ?";

    private static final String SQL_SELECT_JOB_APPLICATIONS_BY_ANNOUNCEMENT = "SELECT ApplicationDate, Status, CoverLetter, UsernameStudent "
            + "FROM `JOBAPPLICATION` "
            + "WHERE `JobAnnouncementID` = ?";

    private final DataBaseJobAnnouncementDao jobAnnouncementDao;
    private final DataBaseStudentDao studentDao;

    public DataBaseJobApplicationDao(DataBaseJobAnnouncementDao jobAnnouncementDao, DataBaseStudentDao studentDao) {
        this.jobAnnouncementDao = jobAnnouncementDao;
        this.studentDao = studentDao;
    }

    @Override
    public void saveJobApplication(JobApplication jobApplication) throws DatabaseException{
        // Retrieve the JobAnnouncement ID
        int jobAnnouncementIdValue = getJobAnnouncementId(jobApplication);

        // Save the job application using the retrieved ID
        saveJobApplicationToDatabase(jobApplication, jobAnnouncementIdValue);
    }

    private int getJobAnnouncementId(JobApplication jobApplication) {
        try {
            // Retrieve JobAnnouncement ID
            Optional<Integer> jobAnnouncementId = jobAnnouncementDao.getJobAnnouncementId(
                    jobApplication.getJobAnnouncement().obtainJobTitle(),
                    jobApplication.getJobAnnouncement().getRecruiter().obtainUsername()
            );

            return jobAnnouncementId.orElseThrow(() ->
                    new DatabaseException(ERROR_JOB_ANNOUNCEMENT_NOT_FOUND)
            );
        } catch (DatabaseException e) {
            throw e; // Rethrow the specific DatabaseException if caught
        } catch (Exception e) {
            throw new DatabaseException(ERROR_UNEXPECTED_ERROR, e); // Catch all other exceptions
        }
    }

    private void saveJobApplicationToDatabase(JobApplication jobApplication, int jobAnnouncementIdValue) {
        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_JOB_APPLICATION)) {

            // Set the values in the PreparedStatement
            stmt.setDate(1, Date.valueOf(jobApplication.obtainApplicationDate()));  // Application Date
            stmt.setString(2, jobApplication.getStudent().obtainUsername());       // Student Username
            stmt.setString(3, jobApplication.obtainStatus().toString());           // Status
            stmt.setString(4, jobApplication.obtainCoverLetter());                 // Cover Letter
            stmt.setInt(5, jobAnnouncementIdValue);                             // Job Announcement ID

            // Execute the query
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DatabaseException(ERROR_DATABASE_EXECUTION_FAILED, e);
        }
    }


    @Override
    public Optional<JobApplication> getJobApplication(Student student, JobAnnouncement jobAnnouncement)throws DatabaseException {
        try {
            Optional<Integer> jobAnnouncementId = jobAnnouncementDao.getJobAnnouncementId(
                    jobAnnouncement.obtainJobTitle(),
                    jobAnnouncement.getRecruiter().obtainUsername()
            );

            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new DatabaseException(ERROR_JOB_ANNOUNCEMENT_NOT_FOUND));

            // Use try-with-resources for both Connection and PreparedStatement
            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_JOB_APPLICATION)) {

                stmt.setString(1, student.obtainUsername());
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
        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException(ERROR_UNEXPECTED_ERROR, e);
        }

        // Return Optional.empty() if no job application found
        return Optional.empty();
    }


    @Override
    public void updateJobApplication(JobApplication jobApplication) throws DatabaseException {
        try {
            Optional<Integer> jobAnnouncementId = jobAnnouncementDao.getJobAnnouncementId(
                    jobApplication.getJobAnnouncement().obtainJobTitle(),
                    jobApplication.getJobAnnouncement().getRecruiter().obtainUsername()
            );

            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new DatabaseException(ERROR_JOB_ANNOUNCEMENT_NOT_FOUND));

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_JOB_APPLICATION)) {

                stmt.setString(1, jobApplication.obtainStatus().toString());
                stmt.setString(2, jobApplication.obtainCoverLetter());
                stmt.setString(3, jobApplication.getStudent().obtainUsername());
                stmt.setInt(4, jobAnnouncementIdValue);

                stmt.executeUpdate();
            }
        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException(ERROR_DATABASE);
        }

    }

    @Override
    public void deleteJobApplication(JobApplication jobApplication) throws DatabaseException {
        try {
            Optional<Integer> jobAnnouncementId = jobAnnouncementDao.getJobAnnouncementId(
                    jobApplication.getJobAnnouncement().obtainJobTitle(),
                    jobApplication.getJobAnnouncement().getRecruiter().obtainUsername()
            );

            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new DatabaseException(ERROR_JOB_ANNOUNCEMENT_NOT_FOUND));

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_JOB_APPLICATION)) {

                stmt.setString(1, jobApplication.getStudent().obtainUsername());
                stmt.setInt(2, jobAnnouncementIdValue);
                stmt.executeUpdate();
            }
        } catch (SQLException | DatabaseException e) {
            throw new DatabaseException(ERROR_DATABASE);
        }

    }

    @Override
    public List<JobApplication> getAllJobApplications(Student student) throws DatabaseException {
        List<JobApplication> jobApplications = new ArrayList<>();
        String username = student.obtainUsername();
        Map<JobApplication, Integer> jobApplicationToAnnouncementId = new HashMap<>();

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_JOB_APPLICATIONS)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate applicationDate = rs.getDate(COLUMN_APPLICATION_DATE).toLocalDate();
                    Status status = Status.valueOf(rs.getString(COLUMN_STATUS).toUpperCase());
                    String coverLetter = rs.getString(COLUMN_COVER_LETTER);
                    int jobAnnouncementId = rs.getInt(COLUMN_JOB_ANNOUNCEMENT_ID);

                    JobApplication jobApplication = new JobApplication(applicationDate, student, status, coverLetter);

                    jobApplicationToAnnouncementId.put(jobApplication, jobAnnouncementId);

                    jobApplications.add(jobApplication);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving job applications from the database", e);
        }

        if (jobApplicationToAnnouncementId.isEmpty()) {
            return jobApplications;
        }

        Map<Integer, JobAnnouncement> jobAnnouncementMap = new HashMap<>();
        for (int jobAnnouncementId : new HashSet<>(jobApplicationToAnnouncementId.values())) {
            jobAnnouncementDao.getJobAnnouncementById(jobAnnouncementId)
                    .ifPresent(jobAnnouncement -> jobAnnouncementMap.put(jobAnnouncementId, jobAnnouncement));
        }

        for (Map.Entry<JobApplication, Integer> entry : jobApplicationToAnnouncementId.entrySet()) {
            JobApplication jobApplication = entry.getKey();
            int jobAnnouncementId = entry.getValue();
            jobApplication.setJobAnnouncement(jobAnnouncementMap.get(jobAnnouncementId));
        }

        return jobApplications;
    }



    @Override
    public List<JobApplication> getJobApplicationsByJobAnnouncement(JobAnnouncement jobAnnouncement) throws DatabaseException {
        List<JobApplication> jobApplications = new ArrayList<>();
        Set<String> studentUsernames = new HashSet<>();


        int jobAnnouncementId = jobAnnouncementDao.getJobAnnouncementId(
                jobAnnouncement.obtainJobTitle(),
                jobAnnouncement.getRecruiter().obtainUsername()
        ).orElseThrow(() -> new DatabaseException(ERROR_JOB_ANNOUNCEMENT_NOT_FOUND));


        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_JOB_APPLICATIONS_BY_ANNOUNCEMENT)) {

            stmt.setInt(1, jobAnnouncementId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate applicationDate = rs.getDate(COLUMN_APPLICATION_DATE).toLocalDate();
                    Status status = Status.valueOf(rs.getString(COLUMN_STATUS).toUpperCase());
                    String coverLetter = rs.getString(COLUMN_COVER_LETTER);
                    String studentUsername = rs.getString(COLUMN_USERNAME_STUDENT);


                    studentUsernames.add(studentUsername);


                    JobApplication jobApplication = new JobApplication(applicationDate, status, coverLetter, jobAnnouncement);
                    jobApplications.add(jobApplication);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException(ERROR_DATABASE, e);
        }


        Map<String, Student> studentCache = getStudentsByUsernames(studentUsernames);


        int i = 0;
        for (String studentUsername : studentUsernames) {
            Student student = studentCache.get(studentUsername);

            if (student != null) {
                jobApplications.get(i).setStudent(student);
            } else {
                throw new DatabaseException(ERROR_STUDENT_NOT_FOUND);
            }
            i++;
        }
        return jobApplications;
    }


    private Map<String, Student> getStudentsByUsernames(Set<String> studentUsernames) throws DatabaseException {
        Map<String, Student> studentCache = new HashMap<>();

        for (String username : studentUsernames) {
            Optional<Student> studentOpt = studentDao.getStudent(username);
            if (studentOpt.isPresent()) {
                studentCache.put(username, studentOpt.get());
            } else {
                throw new DatabaseException(ERROR_STUDENT_NOT_FOUND);
            }
        }

        return studentCache;
    }
}
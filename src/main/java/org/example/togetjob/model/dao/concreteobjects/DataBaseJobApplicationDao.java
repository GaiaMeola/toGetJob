package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.model.dao.abstractobjects.JobApplicationDao;
import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.entity.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataBaseJobApplicationDao implements JobApplicationDao {

    private static final String COLUMN_APPLICATION_DATE = "ApplicationDate";
    private static final String COLUMN_STATUS = "Status";
    private static final String COLUMN_COVER_LETTER = "CoverLetter";

    private final JobAnnouncementDao daoJobAnnouncement;

    public DataBaseJobApplicationDao(JobAnnouncementDao jobAnnouncementDao) {
        this.daoJobAnnouncement = jobAnnouncementDao;
    }

    @Override
    public void saveJobApplication(JobApplication jobApplication) {
        String sql = "INSERT INTO JOBAPPLICATION (ApplicationDate, UsernameStudent, Status, CoverLetter, JobAnnouncementID) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Optional<Integer> jobAnnouncementId = daoJobAnnouncement.getJobAnnouncementId(
                    jobApplication.getJobAnnouncement().getJobTitle(),
                    jobApplication.getJobAnnouncement().getRecruiter().getName()
            );

            if (jobAnnouncementId.isEmpty()) {
                return;
            }

            stmt.setDate(1, Date.valueOf(jobApplication.getApplicationDate()));
            stmt.setString(2, jobApplication.getStudent().getUsername());
            stmt.setString(3, jobApplication.getStatus().toString());
            stmt.setString(4, jobApplication.getCoverLetter());
            stmt.setInt(5, jobAnnouncementId.get());

            stmt.executeUpdate();
        } catch (SQLException ignored) {
            // Silently ignoring the exception
        }
    }

    @Override
    public Optional<JobApplication> getJobApplication(Student student, JobAnnouncement jobAnnouncement) {
        String sql = "SELECT ApplicationDate, Status, CoverLetter FROM JOBAPPLICATION WHERE UsernameStudent = ? AND JobAnnouncementID = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Optional<Integer> jobAnnouncementId = daoJobAnnouncement.getJobAnnouncementId(
                    jobAnnouncement.getJobTitle(),
                    jobAnnouncement.getRecruiter().getName()
            );

            if (jobAnnouncementId.isEmpty()) {
                return Optional.empty();
            }

            stmt.setString(1, student.getUsername());
            stmt.setInt(2, jobAnnouncementId.get());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LocalDate applicationDate = rs.getDate(COLUMN_APPLICATION_DATE).toLocalDate();
                    Status status = Status.valueOf(rs.getString(COLUMN_STATUS).toUpperCase());
                    String coverLetter = rs.getString(COLUMN_COVER_LETTER);

                    return Optional.of(new JobApplication(applicationDate, student, status, coverLetter, jobAnnouncement));
                }
            }
        } catch (SQLException ignored) {
            // Silently ignoring the exception
        }

        return Optional.empty();
    }

    @Override
    public boolean updateJobApplication(JobApplication jobApplication) {
        String sql = "UPDATE JOBAPPLICATION SET Status = ?, CoverLetter = ? WHERE UsernameStudent = ? AND JobAnnouncementID = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Optional<Integer> jobAnnouncementId = daoJobAnnouncement.getJobAnnouncementId(
                    jobApplication.getJobAnnouncement().getJobTitle(),
                    jobApplication.getJobAnnouncement().getRecruiter().getName()
            );

            if (jobAnnouncementId.isEmpty()) {
                return false;
            }

            stmt.setString(1, jobApplication.getStatus().toString());
            stmt.setString(2, jobApplication.getCoverLetter());
            stmt.setString(3, jobApplication.getStudent().getUsername());
            stmt.setInt(4, jobAnnouncementId.get());

            return stmt.executeUpdate() > 0;
        } catch (SQLException ignored) {
            // Silently ignoring the exception
        }

        return false;
    }

    @Override
    public void deleteJobApplication(JobApplication jobApplication) {
        String sql = "DELETE FROM JOBAPPLICATION WHERE UsernameStudent = ? AND JobAnnouncementID = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Optional<Integer> jobAnnouncementId = daoJobAnnouncement.getJobAnnouncementId(
                    jobApplication.getJobAnnouncement().getJobTitle(),
                    jobApplication.getJobAnnouncement().getRecruiter().getName()
            );

            if (jobAnnouncementId.isEmpty()) {
                return;
            }

            stmt.setString(1, jobApplication.getStudent().getUsername());
            stmt.setInt(2, jobAnnouncementId.get());

            stmt.executeUpdate();
        } catch (SQLException ignored) {
            // Silently ignoring the exception
        }
    }

    @Override
    public boolean jobApplicationExists(Student student, JobAnnouncement jobAnnouncement) {
        String sql = "SELECT COUNT(*) FROM JOBAPPLICATION WHERE UsernameStudent = ? AND JobAnnouncementID = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Optional<Integer> jobAnnouncementId = daoJobAnnouncement.getJobAnnouncementId(
                    jobAnnouncement.getJobTitle(),
                    jobAnnouncement.getRecruiter().getName()
            );

            if (jobAnnouncementId.isEmpty()) {
                return false;
            }

            stmt.setString(1, student.getUsername());
            stmt.setInt(2, jobAnnouncementId.get());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException ignored) {
            // Silently ignoring the exception
        }

        return false;
    }

    @Override
    public List<JobApplication> getAllJobApplications(Student student) {
        List<JobApplication> jobApplications = new ArrayList<>();
        String sql = "SELECT ApplicationDate, Status, CoverLetter, JobAnnouncementID FROM JOBAPPLICATION WHERE UsernameStudent = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate applicationDate = rs.getDate(COLUMN_APPLICATION_DATE).toLocalDate();
                    Status status = Status.valueOf(rs.getString(COLUMN_STATUS).toUpperCase());
                    String coverLetter = rs.getString(COLUMN_COVER_LETTER);
                    int jobAnnouncementId = rs.getInt("JobAnnouncementID");

                    Optional<JobAnnouncement> jobAnnouncement = daoJobAnnouncement.getJobAnnouncementById(jobAnnouncementId);

                    jobAnnouncement.ifPresent(announcement -> jobApplications.add(new JobApplication(applicationDate, student, status, coverLetter, announcement)));
                }
            }
        } catch (SQLException ignored) {
            // Silently ignoring the exception
        }

        return jobApplications;
    }

    @Override
    public List<JobApplication> getJobApplicationsByAnnouncement(JobAnnouncement jobAnnouncement) {
        List<JobApplication> jobApplications = new ArrayList<>();
        String sql = "SELECT ApplicationDate, UsernameStudent, Status, CoverLetter FROM JOBAPPLICATION WHERE JobAnnouncementID = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            Optional<Integer> jobAnnouncementId = daoJobAnnouncement.getJobAnnouncementId(
                    jobAnnouncement.getJobTitle(),
                    jobAnnouncement.getRecruiter().getName()
            );

            if (jobAnnouncementId.isEmpty()) {
                return jobApplications;
            }

            stmt.setInt(1, jobAnnouncementId.get());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate applicationDate = rs.getDate(COLUMN_APPLICATION_DATE).toLocalDate();
                    String usernameStudent = rs.getString("UsernameStudent");
                    Status status = Status.valueOf(rs.getString(COLUMN_STATUS).toUpperCase());
                    String coverLetter = rs.getString(COLUMN_COVER_LETTER);

                    Optional<Student> student = getStudentByUsername(usernameStudent);

                    student.ifPresent(st -> jobApplications.add(new JobApplication(applicationDate, st, status, coverLetter, jobAnnouncement)));
                }
            }
        } catch (SQLException ignored) {
            // Silently ignoring the exception
        }

        return jobApplications;
    }

    private  Optional<Student> getStudentByUsername(String username) {
        String sql = "SELECT Username, Name, Surname, EmailAddress, Password, Role FROM STUDENT WHERE Username = ?";

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

                Student student = new Student(name, surname, username, emailAddress, password, role);
                return Optional.of(student);
            }

        } catch (SQLException ignored) {
            // Silently ignoring the exception
        }

        return Optional.empty();
    }


}

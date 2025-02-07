package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.exceptions.DatabaseException;
import org.example.togetjob.model.dao.abstractobjects.InterviewSchedulingDao;
import org.example.togetjob.model.dao.abstractobjects.StudentDao;
import org.example.togetjob.model.entity.InterviewScheduling;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Student;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataBaseInterviewSchedulingDao implements InterviewSchedulingDao {

    private static final String SQL_INSERT_INTERVIEW_SCHEDULING = "INSERT INTO SCHEDULINGINTERVIEW (Subject, Greeting, Introduction, InterviewDateTime, Location, Candidate, JobAnnouncement) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_INTERVIEW_SCHEDULING = "SELECT Subject, Greeting, Introduction, InterviewDateTime, Location, Candidate, JobAnnouncement FROM SCHEDULINGINTERVIEW WHERE Candidate = ? AND JobAnnouncement = ?";
    private static final String SQL_SELECT_ALL_INTERVIEW_SCHEDULING = "SELECT Subject, Greeting, Introduction, InterviewDateTime, Location, Candidate, JobAnnouncement FROM SCHEDULINGINTERVIEW WHERE JobAnnouncement = ?";
    private static final String SQL_CHECK_INTERVIEW_SCHEDULING_EXISTS = "SELECT 1 FROM SCHEDULINGINTERVIEW WHERE Candidate = ? AND JobAnnouncement = ? LIMIT 1";

    public static final String JOB_ANNOUNCEMENT_NOT_FOUND = "Job Announcement not found";
    public static final String STUDENT_NOT_FOUND = "Student not found";

    private final DataBaseJobAnnouncementDao dataBaseJobAnnouncementDao;
    private final StudentDao studentDao;

    // Constructor updated to inject StudentDao as well
    public DataBaseInterviewSchedulingDao(DataBaseJobAnnouncementDao dataBaseJobAnnouncementDao, StudentDao studentDao) {
        this.dataBaseJobAnnouncementDao = dataBaseJobAnnouncementDao;
        this.studentDao = studentDao;
    }

    @Override
    public void saveInterviewScheduling(InterviewScheduling interviewScheduling) throws DatabaseException {
        try {
            // Retrieve JobAnnouncement ID using DataBaseJobAnnouncementDao
            Optional<Integer> jobAnnouncementId = dataBaseJobAnnouncementDao.getJobAnnouncementId(
                    interviewScheduling.getJobAnnouncement().getJobTitle(),
                    interviewScheduling.getJobAnnouncement().getRecruiter().getUsername()
            );

            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new DatabaseException(JOB_ANNOUNCEMENT_NOT_FOUND));

            // Use StudentDao to retrieve Student object by username if not already present
            Student candidate = studentDao.getStudent(interviewScheduling.getCandidate().getUsername())
                    .orElseThrow(() -> new DatabaseException(STUDENT_NOT_FOUND));

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_INTERVIEW_SCHEDULING)) {

                stmt.setString(1, interviewScheduling.getSubject());
                stmt.setString(2, interviewScheduling.getGreeting());
                stmt.setString(3, interviewScheduling.getIntroduction());
                stmt.setTimestamp(4, Timestamp.valueOf(interviewScheduling.getInterviewDateTime()));
                stmt.setString(5, interviewScheduling.getLocation());
                stmt.setString(6, candidate.getUsername());
                stmt.setInt(7, jobAnnouncementIdValue);

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error while saving interview scheduling");
        }
    }

    @Override
    public Optional<InterviewScheduling> getInterviewScheduling(Student student, JobAnnouncement jobAnnouncement) throws DatabaseException {
        try {
            // Retrieve JobAnnouncement ID using DataBaseJobAnnouncementDao
            Optional<Integer> jobAnnouncementId = dataBaseJobAnnouncementDao.getJobAnnouncementId(
                    jobAnnouncement.getJobTitle(),
                    jobAnnouncement.getRecruiter().getUsername()
            );

            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new DatabaseException(JOB_ANNOUNCEMENT_NOT_FOUND));

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_INTERVIEW_SCHEDULING)) {

                stmt.setString(1, student.getUsername());
                stmt.setInt(2, jobAnnouncementIdValue);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String subject = rs.getString("Subject");
                        String greeting = rs.getString("Greeting");
                        String introduction = rs.getString("Introduction");
                        LocalDateTime interviewDateTime = rs.getTimestamp("InterviewDateTime").toLocalDateTime();
                        String location = rs.getString("Location");

                        InterviewScheduling interviewScheduling = new InterviewScheduling(subject, greeting, introduction, interviewDateTime, location, student, jobAnnouncement);
                        return Optional.of(interviewScheduling);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error while retrieving interview scheduling");
        }
        return Optional.empty();
    }

    @Override
    public List<InterviewScheduling> getAllInterviewScheduling(JobAnnouncement jobAnnouncement) throws DatabaseException {
        List<InterviewScheduling> interviewSchedulings = new ArrayList<>();
        try {
            // Retrieve JobAnnouncement ID using DataBaseJobAnnouncementDao
            Optional<Integer> jobAnnouncementId = dataBaseJobAnnouncementDao.getJobAnnouncementId(
                    jobAnnouncement.getJobTitle(),
                    jobAnnouncement.getRecruiter().getUsername()
            );

            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new DatabaseException(JOB_ANNOUNCEMENT_NOT_FOUND));

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_INTERVIEW_SCHEDULING)) {

                stmt.setInt(1, jobAnnouncementIdValue);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String subject = rs.getString("Subject");
                        String greeting = rs.getString("Greeting");
                        String introduction = rs.getString("Introduction");
                        LocalDateTime interviewDateTime = rs.getTimestamp("InterviewDateTime").toLocalDateTime();
                        String location = rs.getString("Location");

                        String candidateUsername = rs.getString("Candidate");
                        Student candidate = studentDao.getStudent(candidateUsername)
                                .orElseThrow(() -> new DatabaseException(STUDENT_NOT_FOUND));

                        InterviewScheduling interviewScheduling = new InterviewScheduling(subject, greeting, introduction, interviewDateTime, location, candidate, jobAnnouncement);
                        interviewSchedulings.add(interviewScheduling);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error while retrieving all interview scheduling");
        }
        return interviewSchedulings;
    }

    @Override
    public boolean interviewSchedulingExists(Student student, JobAnnouncement jobAnnouncement) throws DatabaseException {
        try {
            // Retrieve JobAnnouncement ID using DataBaseJobAnnouncementDao
            Optional<Integer> jobAnnouncementId = dataBaseJobAnnouncementDao.getJobAnnouncementId(
                    jobAnnouncement.getJobTitle(),
                    jobAnnouncement.getRecruiter().getUsername()
            );

            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new DatabaseException(JOB_ANNOUNCEMENT_NOT_FOUND));

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_CHECK_INTERVIEW_SCHEDULING_EXISTS)) {

                stmt.setString(1, student.getUsername());
                stmt.setInt(2, jobAnnouncementIdValue);

                try (ResultSet rs = stmt.executeQuery()) {
                    return rs.next(); // If a row is returned, the interview scheduling exists
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error while checking interview scheduling existence");
        }
    }
}

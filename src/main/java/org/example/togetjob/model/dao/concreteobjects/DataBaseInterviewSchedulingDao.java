package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.exceptions.DatabaseException;
import org.example.togetjob.model.dao.abstractobjects.InterviewSchedulingDao;
import org.example.togetjob.model.dao.abstractobjects.StudentDao;
import org.example.togetjob.model.entity.InterviewScheduling;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Student;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.*;

public class DataBaseInterviewSchedulingDao implements InterviewSchedulingDao {

    private static final String SQL_INSERT_INTERVIEW_SCHEDULING = "INSERT INTO SCHEDULINGINTERVIEW (Subject, Greeting, Introduction, InterviewDateTime, Location, Candidate, JobAnnouncementID) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_INTERVIEW_SCHEDULING = "SELECT Subject, Greeting, Introduction, InterviewDateTime, Location, Candidate, JobAnnouncementID FROM SCHEDULINGINTERVIEW WHERE Candidate = ? AND JobAnnouncementID = ?";
    private static final String SQL_SELECT_ALL_INTERVIEW_SCHEDULING = "SELECT Subject, Greeting, Introduction, InterviewDateTime, Location, Candidate, JobAnnouncementID FROM SCHEDULINGINTERVIEW WHERE JobAnnouncementID = ?";
    private static final String SQL_CHECK_INTERVIEW_SCHEDULING_EXISTS = "SELECT 1 FROM SCHEDULINGINTERVIEW WHERE Candidate = ? AND JobAnnouncementID = ? LIMIT 1";

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

            Optional<Integer> jobAnnouncementId = dataBaseJobAnnouncementDao.getJobAnnouncementId(
                    interviewScheduling.getJobAnnouncement().getJobTitle(),
                    interviewScheduling.getJobAnnouncement().getRecruiter().getUsername()
            );


            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new DatabaseException(JOB_ANNOUNCEMENT_NOT_FOUND));


            Student candidate = studentDao.getStudent(interviewScheduling.getCandidate().getUsername())
                    .orElseThrow(() -> new DatabaseException(STUDENT_NOT_FOUND));


            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_INSERT_INTERVIEW_SCHEDULING)) {


                stmt.setString(1, interviewScheduling.getSubject());
                stmt.setString(2, interviewScheduling.getGreeting());
                stmt.setString(3, interviewScheduling.getIntroduction());
                stmt.setDate(4, Date.valueOf(interviewScheduling.getInterviewDateTime().toLocalDate())); // Usa `Date` per solo la data
                stmt.setString(5, interviewScheduling.getLocation());
                stmt.setString(6, candidate.getUsername());
                stmt.setInt(7, jobAnnouncementIdValue);

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error while saving interview scheduling: " + e.getMessage());
        }
    }

    @Override
    public Optional<InterviewScheduling> getInterviewScheduling(Student student, JobAnnouncement jobAnnouncement) throws DatabaseException {
        try {

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

            throw new DatabaseException("Database error while retrieving interview scheduling: " + e.getMessage(), e);
        }
        return Optional.empty();
    }


    @Override
    public List<InterviewScheduling> getAllInterviewScheduling(JobAnnouncement jobAnnouncement) throws DatabaseException {
        List<InterviewScheduling> interviewSchedulings = new ArrayList<>();
        try {

            Optional<Integer> jobAnnouncementId = dataBaseJobAnnouncementDao.getJobAnnouncementId(
                    jobAnnouncement.getJobTitle(),
                    jobAnnouncement.getRecruiter().getUsername()
            );

            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new DatabaseException(JOB_ANNOUNCEMENT_NOT_FOUND));


            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_INTERVIEW_SCHEDULING)) {


                stmt.setInt(1, jobAnnouncementIdValue);


                try (ResultSet rs = stmt.executeQuery()) {

                    Map<String, Student> studentsMap = new HashMap<>();


                    while (rs.next()) {
                        String candidateUsername = rs.getString("Candidate");


                        if (!studentsMap.containsKey(candidateUsername)) {
                            Optional<Student> candidateOpt = studentDao.getStudent(candidateUsername);
                            Student candidate = candidateOpt.orElseThrow(() -> new DatabaseException(STUDENT_NOT_FOUND));
                            studentsMap.put(candidateUsername, candidate);
                        }
                    }


                    rs.beforeFirst();

                    while (rs.next()) {
                        String subject = rs.getString("Subject");
                        String greeting = rs.getString("Greeting");
                        String introduction = rs.getString("Introduction");
                        LocalDateTime interviewDateTime = rs.getTimestamp("InterviewDateTime").toLocalDateTime();
                        String location = rs.getString("Location");
                        String candidateUsername = rs.getString("Candidate");


                        Student candidate = studentsMap.get(candidateUsername);
                        if (candidate == null) {
                            throw new DatabaseException(STUDENT_NOT_FOUND);
                        }


                        InterviewScheduling interviewScheduling = new InterviewScheduling(
                                subject, greeting, introduction, interviewDateTime, location, candidate, jobAnnouncement
                        );
                        interviewSchedulings.add(interviewScheduling);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error while retrieving all interview scheduling", e);
        }
        return interviewSchedulings;
    }

    @Override
    public boolean interviewSchedulingExists(Student student, JobAnnouncement jobAnnouncement) throws DatabaseException {
        try {

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
                    return rs.next();
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error while checking interview scheduling existence", e);
        }
    }

}

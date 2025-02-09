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
    private static final String SQL_SELECT_ALL_INTERVIEW_SCHEDULING_BY_STUDENT = "SELECT Subject, Greeting, Introduction, InterviewDateTime, Location, Candidate, JobAnnouncementID FROM SchedulingInterview WHERE Candidate = ?";
    private static final String SQL_DELETE_INTERVIEW_SCHEDULING = "DELETE FROM SCHEDULINGINTERVIEW WHERE Candidate = ? AND JobAnnouncementID = ?";
    private static final String SQL_UPDATE_INTERVIEW_SCHEDULING = "UPDATE SCHEDULINGINTERVIEW SET Subject = ?, Greeting = ?, Introduction = ?, InterviewDateTime = ?, Location = ? WHERE Candidate = ? AND JobAnnouncementID = ?";

    private static final String COLUMN_SUBJECT = "Subject";
    private static final String COLUMN_GREETING = "Greeting";
    private static final String COLUMN_INTRODUCTION = "Introduction";
    private static final String COLUMN_INTERVIEW_DATETIME = "InterviewDateTime";
    private static final String COLUMN_LOCATION = "Location";
    private static final String COLUMN_CANDIDATE = "Candidate";
    private static final String COLUM_JOB_ANNOUNCEMENT = "JobAnnouncementID";

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

                        String subject = rs.getString(COLUMN_SUBJECT);
                        String greeting = rs.getString(COLUMN_GREETING);
                        String introduction = rs.getString(COLUMN_INTRODUCTION);
                        LocalDateTime interviewDateTime = rs.getTimestamp(COLUMN_INTERVIEW_DATETIME).toLocalDateTime();
                        String location = rs.getString(COLUMN_LOCATION);


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
            // 1. Ottenere l'ID del JobAnnouncement
            int jobAnnouncementId = dataBaseJobAnnouncementDao.getJobAnnouncementId(
                    jobAnnouncement.getJobTitle(),
                    jobAnnouncement.getRecruiter().getUsername()
            ).orElseThrow(() -> new DatabaseException(JOB_ANNOUNCEMENT_NOT_FOUND));

            // 2. Recuperare gli interview scheduling (senza gli studenti, ma salvando i candidateUsername)
            Set<String> candidateUsernames = new HashSet<>();
            Map<String, InterviewScheduling> schedulingMap = new HashMap<>();

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_INTERVIEW_SCHEDULING)) {

                stmt.setInt(1, jobAnnouncementId);

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        String subject = rs.getString(COLUMN_SUBJECT);
                        String greeting = rs.getString(COLUMN_GREETING);
                        String introduction = rs.getString(COLUMN_INTRODUCTION);
                        LocalDateTime interviewDateTime = rs.getTimestamp(COLUMN_INTERVIEW_DATETIME).toLocalDateTime();
                        String location = rs.getString(COLUMN_LOCATION);
                        String candidateUsername = rs.getString(COLUMN_CANDIDATE);

                        // Memorizzo gli username dei candidati
                        candidateUsernames.add(candidateUsername);

                        // Creo un InterviewScheduling senza candidato (lo assegneremo dopo)
                        InterviewScheduling interviewScheduling = new InterviewScheduling(
                                subject, greeting, introduction, interviewDateTime, location, null, jobAnnouncement
                        );
                        schedulingMap.put(candidateUsername, interviewScheduling);
                    }
                }
            }

            // 3. Recuperare gli studenti in un'unica chiamata a StudentDAO
            Map<String, Student> studentsMap = new HashMap<>();
            for (String username : candidateUsernames) {
                Optional<Student> studentOpt = studentDao.getStudent(username);
                Student student = studentOpt.orElseThrow(() -> new DatabaseException(STUDENT_NOT_FOUND));
                studentsMap.put(username, student);
            }

            // 4. Assegnare gli studenti agli InterviewScheduling e costruire la lista finale
            for (Map.Entry<String, InterviewScheduling> entry : schedulingMap.entrySet()) {
                String username = entry.getKey();
                InterviewScheduling scheduling = entry.getValue();
                Student candidate = studentsMap.get(username);
                if (candidate == null) {
                    throw new DatabaseException(STUDENT_NOT_FOUND);
                }
                scheduling.setCandidate(candidate);
                interviewSchedulings.add(scheduling);
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

    @Override
    public List<InterviewScheduling> getAllInterviewScheduling(Student student) throws DatabaseException {
        List<InterviewScheduling> interviewSchedulings = new ArrayList<>();
        List<Integer> jobAnnouncementIds = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL_INTERVIEW_SCHEDULING_BY_STUDENT)) {

            stmt.setString(1, student.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String subject = rs.getString(COLUMN_SUBJECT);
                    String greeting = rs.getString(COLUMN_GREETING);
                    String introduction = rs.getString(COLUMN_INTRODUCTION);
                    LocalDateTime interviewDateTime = rs.getTimestamp(COLUMN_INTERVIEW_DATETIME).toLocalDateTime();
                    String location = rs.getString(COLUMN_LOCATION);
                    int jobAnnouncementId = rs.getInt(COLUM_JOB_ANNOUNCEMENT);

                    InterviewScheduling tempInterview = new InterviewScheduling(subject, greeting, introduction, interviewDateTime, location, student, null);
                    interviewSchedulings.add(tempInterview);
                    jobAnnouncementIds.add(jobAnnouncementId);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error while retrieving interviews for student", e);
        }

        for (int i = 0; i < interviewSchedulings.size(); i++) {
            int jobAnnouncementId = jobAnnouncementIds.get(i);
            JobAnnouncement jobAnnouncement = dataBaseJobAnnouncementDao.getJobAnnouncementById(jobAnnouncementId)
                    .orElseThrow(() -> new DatabaseException(JOB_ANNOUNCEMENT_NOT_FOUND));
            interviewSchedulings.get(i).setJobAnnouncement(jobAnnouncement);
        }

        return interviewSchedulings;
    }

    @Override
    public void deleteInterviewScheduling(InterviewScheduling interviewScheduling) throws DatabaseException {
        try {
            Optional<Integer> jobAnnouncementId = dataBaseJobAnnouncementDao.getJobAnnouncementId(
                    interviewScheduling.getJobAnnouncement().getJobTitle(),
                    interviewScheduling.getJobAnnouncement().getRecruiter().getUsername()
            );

            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new DatabaseException(JOB_ANNOUNCEMENT_NOT_FOUND));

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_DELETE_INTERVIEW_SCHEDULING)) {

                stmt.setString(1, interviewScheduling.getCandidate().getUsername());
                stmt.setInt(2, jobAnnouncementIdValue);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DatabaseException("Interview Scheduling not found to delete.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error while deleting interview scheduling", e);
        }
    }

    @Override
    public void updateInterviewScheduling(InterviewScheduling interviewScheduling) throws DatabaseException {
        try {
            Optional<Integer> jobAnnouncementId = dataBaseJobAnnouncementDao.getJobAnnouncementId(
                    interviewScheduling.getJobAnnouncement().getJobTitle(),
                    interviewScheduling.getJobAnnouncement().getRecruiter().getUsername()
            );

            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new DatabaseException(JOB_ANNOUNCEMENT_NOT_FOUND));

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(SQL_UPDATE_INTERVIEW_SCHEDULING)) {

                stmt.setString(1, interviewScheduling.getSubject());
                stmt.setString(2, interviewScheduling.getGreeting());
                stmt.setString(3, interviewScheduling.getIntroduction());
                stmt.setTimestamp(4, Timestamp.valueOf(interviewScheduling.getInterviewDateTime()));
                stmt.setString(5, interviewScheduling.getLocation());
                stmt.setString(6, interviewScheduling.getCandidate().getUsername());
                stmt.setInt(7, jobAnnouncementIdValue);

                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new DatabaseException("Interview Scheduling not found to update.");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error while updating interview scheduling", e);
        }
    }

}

package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.dao.abstractobjects.JobApplicationDao;
import org.example.togetjob.model.dao.abstractobjects.StudentDao;
import org.example.togetjob.model.entity.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataBaseJobApplicationDao implements JobApplicationDao {

    JobAnnouncementDao daoJobAnnouncement = new DataBaseJobAnnouncementDao() ;
    StudentDao daoStudent = new DataBaseStudentDao() ;

    @Override
    public void saveJobApplication(JobApplication jobApplication) {
        try {
            Optional<Integer> jobAnnouncementId = daoJobAnnouncement.getJobAnnouncementId(
                    jobApplication.getJobAnnouncement().getJobTitle(),
                    jobApplication.getJobAnnouncement().getRecruiter().getName()
            );

            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new RuntimeException());

            String sql = "INSERT INTO JOBAPPLICATION (ApplicationDate, UsernameStudent, Status, CoverLetter, JobAnnouncementID) VALUES (?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setDate(1, Date.valueOf(jobApplication.getApplicationDate()));
                stmt.setString(2, jobApplication.getStudent().getUsername());
                stmt.setString(3, jobApplication.getStatus().toString());
                stmt.setString(4, jobApplication.getCoverLetter());
                stmt.setInt(5, jobAnnouncementIdValue);

                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while saving job application", e);
        }
    }





    @Override
    public Optional<JobApplication> getJobApplication(Student student, JobAnnouncement jobAnnouncement) {
        try {
            // Recupera l'ID dell'annuncio di lavoro
            Optional<Integer> jobAnnouncementId = daoJobAnnouncement.getJobAnnouncementId(
                    jobAnnouncement.getJobTitle(),
                    jobAnnouncement.getRecruiter().getName()
            );

            // Gestisci il caso in cui l'Optional è vuoto, lanciando un'eccezione
            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new RuntimeException());

            // SQL per selezionare i dati della JobApplication
            String sql = "SELECT ApplicationDate, Status, CoverLetter FROM JOBAPPLICATION WHERE UsernameStudent = ? AND JobAnnouncementID = ?";

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                // Imposta i parametri della query
                stmt.setString(1, student.getUsername());
                stmt.setInt(2, jobAnnouncementIdValue);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {  // Verifica se ci sono risultati
                        // Legge i dati dal ResultSet
                        LocalDate applicationDate = rs.getDate("ApplicationDate").toLocalDate();
                        Status status = Status.valueOf(rs.getString("Status").toUpperCase());
                        String coverLetter = rs.getString("CoverLetter");

                        // Crea il JobApplication e lo restituisce come Optional
                        JobApplication jobApplication = new JobApplication(applicationDate, student, status, coverLetter, jobAnnouncement);
                        return Optional.of(jobApplication);
                    }
                }
            }
        } catch (SQLException | RuntimeException e) {
            // Gestione delle eccezioni: stampa stack trace o log
            e.printStackTrace();
        }

        return Optional.empty();  // Restituisce Optional vuoto se non trovata una corrispondenza o errore
    }


    @Override
    public boolean updateJobApplication(JobApplication jobApplication) {
        try {
            // Recupera l'ID dell'annuncio di lavoro
            Optional<Integer> jobAnnouncementId = daoJobAnnouncement.getJobAnnouncementId(
                    jobApplication.getJobAnnouncement().getJobTitle(),
                    jobApplication.getJobAnnouncement().getRecruiter().getName());

            // Gestisci il caso in cui l'Optional è vuoto, lanciando un'eccezione
            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new RuntimeException("Job Announcement not found"));

            // SQL per aggiornare i dati della JobApplication
            String sql = "UPDATE JOBAPPLICATION SET Status = ?, CoverLetter = ? WHERE UsernameStudent = ? AND JobAnnouncementID = ?;";

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                // Imposta i parametri della query
                stmt.setString(1, jobApplication.getStatus().toString());  // Status come stringa
                stmt.setString(2, jobApplication.getCoverLetter());
                stmt.setString(3, jobApplication.getStudent().getUsername());
                stmt.setInt(4, jobAnnouncementIdValue);  // ID dell'annuncio di lavoro

                stmt.executeUpdate();
                return true;

            }
        } catch (SQLException | RuntimeException e) {
            // Gestione delle eccezioni: stampa stack trace o log
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public void deleteJobApplication(JobApplication jobApplication) {
        try {
            // Recupera l'ID dell'annuncio di lavoro
            Optional<Integer> jobAnnouncementId = daoJobAnnouncement.getJobAnnouncementId(
                    jobApplication.getJobAnnouncement().getJobTitle(),
                    jobApplication.getJobAnnouncement().getRecruiter().getName());

            // Gestisci il caso in cui l'Optional è vuoto, lanciando un'eccezione
            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new RuntimeException("Job Announcement not found"));

            // SQL per eliminare la JobApplication
            String sql = "DELETE FROM JOBAPPLICATION WHERE UsernameStudent = ? AND JobAnnouncementID = ?;";

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                // Imposta i parametri della query
                stmt.setString(1, jobApplication.getStudent().getUsername());  // Username dello studente
                stmt.setInt(2, jobAnnouncementIdValue);  // ID dell'annuncio di lavoro

            }
        } catch (SQLException | RuntimeException e) {
            // Gestione delle eccezioni: stampa stack trace o log
            e.printStackTrace();
        }
    }


    @Override
    public boolean jobApplicationExists(Student student, JobAnnouncement jobAnnouncement) {
        try {
            // Recupera l'ID dell'annuncio di lavoro
            Optional<Integer> jobAnnouncementId = daoJobAnnouncement.getJobAnnouncementId(
                    jobAnnouncement.getJobTitle(),
                    jobAnnouncement.getRecruiter().getName());

            // Gestisci il caso in cui l'Optional è vuoto, lanciando un'eccezione
            int jobAnnouncementIdValue = jobAnnouncementId.orElseThrow(() -> new RuntimeException("Job Announcement not found"));

            // SQL per verificare se la JobApplication esiste
            String sql = "SELECT COUNT(*) FROM JOBAPPLICATION WHERE UsernameStudent = ? AND JobAnnouncementID = ?;";

            try (Connection conn = DatabaseConfig.getInstance().getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                // Imposta i parametri della query
                stmt.setString(1, student.getUsername());  // Username dello studente
                stmt.setInt(2, jobAnnouncementIdValue);  // ID dell'annuncio di lavoro

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Se la query restituisce un valore maggiore di 0, significa che la JobApplication esiste
                        return rs.getInt(1) > 0;
                    }
                }

            }
        } catch (SQLException | RuntimeException e) {
            // Gestione delle eccezioni: stampa stack trace o log
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<JobApplication> getAllJobApplications(Student student) {

        List<JobApplication> jobApplications = new ArrayList<>();

        String sql = "SELECT ApplicationDate, Status, CoverLetter , JobAnnouncementID FROM JOBAPPLICATION WHERE UsernameStudent = ?;";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Imposta il parametro della query
            stmt.setString(1, student.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate applicationDate = rs.getDate("ApplicationDate").toLocalDate();
                    Status status = Status.valueOf(rs.getString("Status").toUpperCase());
                    String coverLetter = rs.getString("CoverLetter");
                    Integer jobAnnouncemntId = rs.getInt("JobAnnouncementID");

                    // Ottieni il JobAnnouncement e controlla se è presente
                    Optional<JobAnnouncement> jobAnnouncementOPT = daoJobAnnouncement.getJobAnnouncementById(jobAnnouncemntId);

                    if (jobAnnouncementOPT.isPresent()) {
                        JobAnnouncement jobAnnouncement = jobAnnouncementOPT.get();
                        // Creazione della JobApplication
                        JobApplication jobApplication = new JobApplication(applicationDate, student, status, coverLetter, jobAnnouncement);

                        // Aggiungi alla lista
                        jobApplications.add(jobApplication);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobApplications;
    }

    @Override
    public List<JobApplication> getJobApplicationsByAnnouncement(JobAnnouncement jobAnnouncement) {

        List<JobApplication> jobApplications = new ArrayList<>();
        Optional<Integer> jobAnnouncementIdOPT = daoJobAnnouncement.getJobAnnouncementId( jobAnnouncement.getJobTitle() , jobAnnouncement.getRecruiter().getUsername() ); // Ottieni l'ID dell'annuncio di lavoro
        Integer jobAnnouncementId = jobAnnouncementIdOPT.get() ;

        String sql = "SELECT ApplicationDate, Status, CoverLetter, UsernameStudent FROM JOBAPPLICATION WHERE JobAnnouncementID = ?;";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Imposta il parametro della query
            stmt.setInt(1, jobAnnouncementId);

            try (ResultSet rs = stmt.executeQuery()) {
                // Itera su tutte le righe del ResultSet
                while (rs.next()) {
                    // Per ogni riga, recupera i dati della JobApplication
                    LocalDate applicationDate = rs.getDate("ApplicationDate").toLocalDate();
                    Status status = Status.valueOf(rs.getString("Status").toUpperCase());
                    String coverLetter = rs.getString("CoverLetter");
                    String usernameStudent = rs.getString("UsernameStudent");

                    // Recupera lo studente dal database usando lo username
                    Optional<Student> studentOPT = daoStudent.getStudentByUsername(usernameStudent);

                    if (studentOPT.isPresent()) {
                        Student student = studentOPT.get();
                        // Crea la JobApplication per questa riga
                        JobApplication jobApplication = new JobApplication(applicationDate, student, status, coverLetter, jobAnnouncement);
                        // Aggiungi alla lista di JobApplications
                        jobApplications.add(jobApplication);
                    } else {
                        // Gestisci il caso in cui lo studente non è trovato (facoltativo)
                        System.out.println("Student not found for Username: " + usernameStudent);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return jobApplications;  // Restituisce la lista di tutte le JobApplication
    }

}

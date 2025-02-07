package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.model.dao.abstractobjects.JobAnnouncementDao;
import org.example.togetjob.model.entity.JobAnnouncement;
import org.example.togetjob.model.entity.Recruiter;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class DataBaseJobAnnouncementDao implements JobAnnouncementDao {

    DataBaseRecruiterDao daoRecruiter = new DataBaseRecruiterDao();


    @Override
    public boolean saveJobAnnouncement(JobAnnouncement jobAnnouncement) {
        return false;
    }

    @Override
    public Optional<JobAnnouncement> getJobAnnouncement(String jobTitle, Recruiter recruiter) {
        return Optional.empty();
    }

    @Override
    public boolean updateJobAnnouncement(JobAnnouncement jobAnnouncement) {
        return false;
    }

    @Override
    public boolean deleteJobAnnouncement(JobAnnouncement jobAnnouncement) {
        return false;
    }

    @Override
    public boolean jobAnnouncementExists(String jobTitle, Recruiter recruiter) {
        return false;
    }

    @Override
    public List<JobAnnouncement> getAllJobAnnouncements(Recruiter recruiter) {
        return List.of();
    }

    @Override
    public List<JobAnnouncement> getAllJobAnnouncements() {
        return List.of();
    }

    @Override
    public Optional<Integer> getJobAnnouncementId(String jobTitle, String recruiterName) {
        String sql = "SELECT ID FROM JOBANNOUNCEMENT WHERE JobTitle = ? AND RecruiterName = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, jobTitle);
            stmt.setString(2, recruiterName);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Restituisci l'ID come Optional<Integer>
                    return Optional.of(rs.getInt("ID"));
                } else {
                    // Se non trovato, restituisci Optional.empty()
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            // Gestione dell'errore (ad esempio, log o rilancio dell'eccezione)
            e.printStackTrace();
            return Optional.empty();  // Restituisce un Optional vuoto in caso di errore
        }
    }

    @Override
    public Optional<JobAnnouncement> getJobAnnouncementById(Integer jobAnnouncementId) {
        String sql = "SELECT JobTitle, JobType, RoleJob, Location, WorkingHours, CompanyName, Salary, Description, isActive, RecruiterName FROM JOBANNOUNCEMENT WHERE ID = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Imposta il parametro dell'ID
            stmt.setInt(1, jobAnnouncementId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Estrai i dati dal ResultSet
                    String jobTitle = rs.getString("JobTitle");
                    String jobType = rs.getString("JobType");
                    String role = rs.getString("RoleJob");
                    String location = rs.getString("Location");
                    int workingHours = rs.getInt("WorkingHours");
                    String companyName = rs.getString("CompanyName");
                    double salary = rs.getDouble("Salary");
                    String description = rs.getString("Description");
                    boolean isActive = rs.getBoolean("isActive");
                    String recruiterName = rs.getString("RecruiterName");

                    // Recupera il recruiter in modo sicuro
                    Optional<Recruiter> recruiterOPT = daoRecruiter.getRecruiter(recruiterName);

                    if (recruiterOPT.isPresent()) {
                        Recruiter recruiter = recruiterOPT.get();
                        // Crea il JobAnnouncement e restituiscilo
                        JobAnnouncement jobAnnouncement = new JobAnnouncement(jobTitle, jobType, role, location, workingHours, companyName, salary, description, isActive, recruiter);
                        return Optional.of(jobAnnouncement);
                    } else {
                        // Se il recruiter non esiste, restituisci Optional.empty()
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Gestire l'errore come appropriato
        }

        return Optional.empty(); // Se non trovato
    }



}

package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.model.dao.abstractobjects.RecruiterDao;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.entity.Role;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataBaseRecruiterDao implements RecruiterDao {

    @Override
    public void saveRecruiter(Recruiter recruiter) {
        String sql = "INSERT INTO RECRUITER (Username, Name, Surname, EmailAddress, Password, Role, Companies) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, recruiter.getUsername());
            stmt.setString(2, recruiter.getName());
            stmt.setString(3, recruiter.getSurname());
            stmt.setString(4, recruiter.getEmailAddress());
            stmt.setString(5, recruiter.getPassword());
            stmt.setString(6, recruiter.getRole().name());

            // Convertiamo la lista di companies in una stringa separata da virgole
            stmt.setString(7, String.join(",", recruiter.getCompanies()));

            stmt.executeUpdate();  // Eseguiamo l'inserimento
        } catch (SQLException e) {
            e.printStackTrace();  // Gestione dell'errore
        }
    }


    @Override
    public Optional<Recruiter> getRecruiter(String username) {
        String sql = "SELECT * FROM RECRUITER WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Estrazione dei dati
                String name = rs.getString("Name");
                String surname = rs.getString("Surname");
                String emailAddress = rs.getString("EmailAddress");
                String password = rs.getString("Password");
                Role role = Role.valueOf(rs.getString("Role").toUpperCase());

                // Estrazione delle companies e trasformazione in lista
                String companiesString = rs.getString("Companies");
                List<String> companies = List.of(companiesString.split(","));

                // Creazione del recruiter
                Recruiter recruiter = new Recruiter(name, surname, username, emailAddress, password, role, companies);

                return Optional.of(recruiter);  // Ritorniamo il recruiter
            }

        } catch (SQLException e) {
            e.printStackTrace();  // Gestione dell'errore
        }

        return Optional.empty();  // Se non esiste un recruiter con quel username
    }


    @Override
    public List<Recruiter> getAllRecruiter() {
        String sql = "SELECT * FROM RECRUITER";
        List<Recruiter> recruiters = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                // Estrazione dei dati
                String username = rs.getString("Username");
                String name = rs.getString("Name");
                String surname = rs.getString("Surname");
                String emailAddress = rs.getString("EmailAddress");
                String password = rs.getString("Password");
                Role role = Role.valueOf(rs.getString("Role").toUpperCase());

                // Estrazione delle companies e trasformazione in lista
                String companiesString = rs.getString("Companies");
                List<String> companies = List.of(companiesString.split(","));

                // Creazione del recruiter
                Recruiter recruiter = new Recruiter(name, surname, username, emailAddress, password, role, companies);
                recruiters.add(recruiter); // Aggiungi alla lista
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Gestione dell'errore
        }

        return recruiters; // Ritorna la lista di recruiter
    }

    @Override
    public boolean updateRecruiter(Recruiter recruiter) {
        String sql = "UPDATE RECRUITER SET Name = ?, Surname = ?, EmailAddress = ?, Password = ?, Companies = ? WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Impostazione dei parametri della query
            stmt.setString(1, recruiter.getName());
            stmt.setString(2, recruiter.getSurname());
            stmt.setString(3, recruiter.getEmailAddress());
            stmt.setString(4, recruiter.getPassword());

            // Le aziende devono essere memorizzate come stringa separata da virgole
            String companies = String.join(",", recruiter.getCompanies());
            stmt.setString(5, companies);

            stmt.setString(6, recruiter.getUsername());

            // Esegui l'update e ritorna true se è stato modificato almeno un record
            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteRecruiter(String username) {
        String sql = "DELETE FROM RECRUITER WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Impostazione del parametro per il nome utente
            stmt.setString(1, username);

            // Esegui la delete e ritorna true se almeno una riga è stata eliminata
            int rowsDeleted = stmt.executeUpdate();
            return rowsDeleted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean recruiterExists(String username) {
        String sql = "SELECT COUNT(*) FROM RECRUITER WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Imposta il parametro username
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1); // Ottieni il numero di righe trovate
                    return count > 0; // Se count è maggiore di 0, l'utente esiste
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Se non ci sono risultati, il recruiter non esiste
    }

}

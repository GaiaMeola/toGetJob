package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.model.dao.abstractobjects.UserDao;
import org.example.togetjob.model.entity.Recruiter;
import org.example.togetjob.model.entity.Role;
import org.example.togetjob.model.entity.Student;
import org.example.togetjob.model.entity.User;
import java.sql.* ;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataBaseUserDao implements UserDao {

    @Override
    public boolean saveUser(User user) {
        String sql = "INSERT INTO USER (Username, Name, Surname, EmailAddress, Password, Role) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getSurname());
            stmt.setString(4, user.getEmailAddress());
            stmt.setString(5, user.getPassword());
            stmt.setString(6, user.getRole().name());

            return stmt.executeUpdate() > 0; // Ritorna true se almeno una riga è stata inserita
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Optional<User> getUser(String username) {
        String sql = "SELECT * FROM USER WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) { // Se esiste un risultato
                String name = rs.getString("Name");
                String surname = rs.getString("Surname");
                String emailAddress = rs.getString("EmailAddress");
                String password = rs.getString("Password");
                Role role = Role.valueOf(rs.getString("Role").toUpperCase()); // Converte la stringa in maiuscolo

                User user; // Dichiarazione della variabile

                if (role.equals(Role.STUDENT)) {
                    user = new Student(name, surname, username, emailAddress, password, role);
                } else { // Se non è STUDENT, deve essere RECRUITER
                    user = new Recruiter(name, surname, username, emailAddress, password, role);
                }

                return Optional.of(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    @Override
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM USER";
        List<User> users = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String username = rs.getString("Username");
                String name = rs.getString("Name");
                String surname = rs.getString("Surname");
                String emailAddress = rs.getString("EmailAddress");
                String password = rs.getString("Password");
                Role role = Role.valueOf(rs.getString("Role").toUpperCase()); // Converte la stringa ENUM in Role



                User user;
                if (role.equals(Role.STUDENT)) {
                    user = new Student(name, surname, username, emailAddress, password, role);
                } else { // Se non è STUDENT, deve essere RECRUITER
                    user = new Recruiter(name, surname, username, emailAddress, password, role);
                }

                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }


    @Override
    public boolean updateUser(User user) {
        String sql = "UPDATE USER SET Name = ?, Surname = ?, EmailAddress = ?, Password = ?, Role = ? WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getName());
            stmt.setString(2, user.getSurname());
            stmt.setString(3, user.getEmailAddress());
            stmt.setString(4, user.getPassword());
            stmt.setString(5, user.getRole().name()); // Converte Role in stringa ENUM
            stmt.setString(6, user.getUsername());

            int rowsUpdated = stmt.executeUpdate(); // Ritorna il numero di righe modificate
            return rowsUpdated > 0; // Se almeno una riga è stata aggiornata, ritorna true
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(String username) {
        String sql = "DELETE FROM USER WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            int rowsDeleted = stmt.executeUpdate(); // Ritorna il numero di righe eliminate
            return rowsDeleted > 0; // Se almeno una riga è stata eliminata, ritorna true
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean userExists(String username) {
        String sql = "SELECT COUNT(*) FROM USER WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1); // Ottieni il conteggio dei record trovati
                    return count > 0; // Se il conteggio è maggiore di 0, l'utente esiste
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Se non ci sono risultati, l'utente non esiste
    }

}

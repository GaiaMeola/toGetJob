package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.connection.DatabaseConfig;
import org.example.togetjob.model.dao.abstractobjects.StudentDao;
import org.example.togetjob.model.entity.Role;
import org.example.togetjob.model.entity.Student;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataBaseStudentDao implements StudentDao {

    @Override
    public void saveStudent(Student student) {
        String sql = "INSERT INTO STUDENT (Username, Name, Surname, EmailAddress, Password, Role, DateOfBirth, PhoneNumber, Degrees, CourseAttended, Certifications, WorkExperiences, Skills, Availability) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, student.getUsername());
            stmt.setString(2, student.getName());
            stmt.setString(3, student.getSurname());
            stmt.setString(4, student.getEmailAddress());
            stmt.setString(5, student.getPassword());
            stmt.setString(6, student.getRole().name()); // Ruolo (STUDENT)
            stmt.setDate(7, Date.valueOf(student.getDateOfBirth())); // Gestisci LocalDate
            stmt.setString(8, student.getPhoneNumber());
            stmt.setString(9, String.join(",", student.getDegrees())); // Converte lista in stringa separata da virgole
            stmt.setString(10, String.join(",", student.getCourseAttended()));
            stmt.setString(11, String.join(",", student.getCertifications()));
            stmt.setString(12, String.join(",", student.getWorkExperiences()));
            stmt.setString(13, String.join(",", student.getSkills()));
            stmt.setString(14, student.getAvailability());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public Optional<Student> getStudent(String username) {
        String sql = "SELECT * FROM STUDENT WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) { // Se esiste un risultato
                String name = rs.getString("Name");
                String surname = rs.getString("Surname");
                String emailAddress = rs.getString("EmailAddress");
                String password = rs.getString("Password");

                // Recupere le informazioni aggiuntive per lo Student
                LocalDate dateOfBirth = rs.getDate("DateOfBirth").toLocalDate();
                String phoneNumber = rs.getString("PhoneNumber");
                List<String> degrees = List.of(rs.getString("Degrees").split(","));
                List<String> courseAttended = List.of(rs.getString("CourseAttended").split(","));
                List<String> certifications = List.of(rs.getString("Certifications").split(","));
                List<String> workExperiences = List.of(rs.getString("WorkExperiences").split(","));
                List<String> skills = List.of(rs.getString("Skills").split(","));
                String availability = rs.getString("Availability");

                // Creazione dell'oggetto Student
                Student student = new Student(name, surname, username, emailAddress, password, Role.STUDENT,
                        dateOfBirth, phoneNumber, degrees, courseAttended, certifications,
                        workExperiences, skills, availability, null); // Impostiamo `null` per `jobApplications`

                return Optional.of(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Student> getAllStudents() {
        String sql = "SELECT * FROM STUDENT WHERE Role = 'STUDENT'"; // Filtra per gli utenti di tipo 'STUDENT'
        List<Student> students = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) { // Itera attraverso i risultati
                String username = rs.getString("Username");
                String name = rs.getString("Name");
                String surname = rs.getString("Surname");
                String emailAddress = rs.getString("EmailAddress");
                String password = rs.getString("Password");

                // Recupera i dati aggiuntivi per lo Student
                LocalDate dateOfBirth = rs.getDate("DateOfBirth").toLocalDate();
                String phoneNumber = rs.getString("PhoneNumber");
                List<String> degrees = List.of(rs.getString("Degrees").split(","));
                List<String> courseAttended = List.of(rs.getString("CourseAttended").split(","));
                List<String> certifications = List.of(rs.getString("Certifications").split(","));
                List<String> workExperiences = List.of(rs.getString("WorkExperiences").split(","));
                List<String> skills = List.of(rs.getString("Skills").split(","));
                String availability = rs.getString("Availability");

                // Crea un oggetto Student e aggiungilo alla lista
                Student student = new Student(name, surname, username, emailAddress, password, Role.STUDENT,
                        dateOfBirth, phoneNumber, degrees, courseAttended, certifications,
                        workExperiences, skills, availability, null); // Imposta `null` per jobApplications
                students.add(student); // Aggiungi alla lista
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students; // Ritorna la lista degli studenti
    }


    @Override
    public boolean updateStudent(Student student) {
        String sql = "UPDATE STUDENT SET Name = ?, Surname = ?, EmailAddress = ?, Password = ?, " +
                "DateOfBirth = ?, PhoneNumber = ?, Degrees = ?, CourseAttended = ?, Certifications = ?, " +
                "WorkExperiences = ?, Skills = ?, Availability = ? WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Impostiamo i parametri per la query
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getSurname());
            stmt.setString(3, student.getEmailAddress());
            stmt.setString(4, student.getPassword());
            stmt.setDate(5, Date.valueOf(student.getDateOfBirth())); // Convertiamo LocalDate in java.sql.Date
            stmt.setString(6, student.getPhoneNumber());
            stmt.setString(7, String.join(",", student.getDegrees())); // Converte la lista in una stringa separata da virgola
            stmt.setString(8, String.join(",", student.getCourseAttended()));
            stmt.setString(9, String.join(",", student.getCertifications()));
            stmt.setString(10, String.join(",", student.getWorkExperiences()));
            stmt.setString(11, String.join(",", student.getSkills()));
            stmt.setString(12, student.getAvailability());
            stmt.setString(13, student.getUsername()); // La chiave primaria è Username

            int rowsUpdated = stmt.executeUpdate(); // Eseguiamo la query

            return rowsUpdated > 0; // Se almeno una riga è stata aggiornata, ritorna true
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // In caso di errore o nessuna riga aggiornata
    }


    @Override
    public boolean deleteStudent(String username) {
        String sql = "DELETE FROM STUDENT WHERE Username = ? AND Role = 'STUDENT'";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Impostiamo il parametro username
            stmt.setString(1, username);

            int rowsDeleted = stmt.executeUpdate(); // Eseguiamo la query di eliminazione

            return rowsDeleted > 0; // Se almeno una riga è stata eliminata, ritorna true
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Se si verifica un errore o nessuna riga è eliminata, ritorna false
    }


    @Override
    public boolean studentExists(String username) {
        String sql = "SELECT COUNT(*) FROM STUDENT WHERE Username = ? AND Role = 'STUDENT'";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Impostiamo il parametro username
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1); // Ottieni il conteggio dei record trovati
                    return count > 0; // Se il conteggio è maggiore di 0, lo studente esiste
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Se non ci sono risultati, lo studente non esiste
    }

    public Optional<Student> getStudentByUsername(String username){

        return getStudent(username) ;

    }

}

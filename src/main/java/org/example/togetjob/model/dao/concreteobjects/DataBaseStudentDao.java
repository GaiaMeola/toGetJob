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
            stmt.setString(6, student.getRole().name()); // Role (STUDENT)
            stmt.setDate(7, Date.valueOf(student.getDateOfBirth())); // Handling LocalDate
            stmt.setString(8, student.getPhoneNumber());
            stmt.setString(9, String.join(",", student.getDegrees())); // Converts list to comma-separated string
            stmt.setString(10, String.join(",", student.getCourseAttended()));
            stmt.setString(11, String.join(",", student.getCertifications()));
            stmt.setString(12, String.join(",", student.getWorkExperiences()));
            stmt.setString(13, String.join(",", student.getSkills()));
            stmt.setString(14, student.getAvailability());

            stmt.executeUpdate(); // Executes the insertion into the database

        } catch (SQLException e) {
            // Log or handle the exception based on your needs
        }
    }

    @Override
    public Optional<Student> getStudent(String username) {
        String sql = "SELECT Username, Name, Surname, EmailAddress, Password, DateOfBirth, PhoneNumber, Degrees, CourseAttended, Certifications, WorkExperiences, Skills, Availability "
                + "FROM STUDENT WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) { // If a result exists
                String name = rs.getString("Name");
                String surname = rs.getString("Surname");
                String emailAddress = rs.getString("EmailAddress");
                String password = rs.getString("Password");

                // Retrieve additional information for the Student
                LocalDate dateOfBirth = rs.getDate("DateOfBirth").toLocalDate();
                String phoneNumber = rs.getString("PhoneNumber");
                List<String> degrees = List.of(rs.getString("Degrees").split(","));
                List<String> courseAttended = List.of(rs.getString("CourseAttended").split(","));
                List<String> certifications = List.of(rs.getString("Certifications").split(","));
                List<String> workExperiences = List.of(rs.getString("WorkExperiences").split(","));
                List<String> skills = List.of(rs.getString("Skills").split(","));
                String availability = rs.getString("Availability");

                // Create the Student object
                Student student = new Student(name, surname, username, emailAddress, password, Role.STUDENT,
                        dateOfBirth, phoneNumber, degrees, courseAttended, certifications,
                        workExperiences, skills, availability, null); // Set `null` for `jobApplications`

                return Optional.of(student);
            }
        } catch (SQLException e) {
            // Log or handle the exception based on your needs
        }
        return Optional.empty(); // Return empty if no student is found
    }

    @Override
    public List<Student> getAllStudents() {
        String sql = "SELECT Username, Name, Surname, EmailAddress, Password, DateOfBirth, PhoneNumber, Degrees, CourseAttended, Certifications, WorkExperiences, Skills, Availability "
                + "FROM STUDENT WHERE Role = 'STUDENT'"; // Filter for 'STUDENT' role
        List<Student> students = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) { // Iterate through the results
                String username = rs.getString("Username");
                String name = rs.getString("Name");
                String surname = rs.getString("Surname");
                String emailAddress = rs.getString("EmailAddress");
                String password = rs.getString("Password");

                // Retrieve additional data for Student
                LocalDate dateOfBirth = rs.getDate("DateOfBirth").toLocalDate();
                String phoneNumber = rs.getString("PhoneNumber");
                List<String> degrees = List.of(rs.getString("Degrees").split(","));
                List<String> courseAttended = List.of(rs.getString("CourseAttended").split(","));
                List<String> certifications = List.of(rs.getString("Certifications").split(","));
                List<String> workExperiences = List.of(rs.getString("WorkExperiences").split(","));
                List<String> skills = List.of(rs.getString("Skills").split(","));
                String availability = rs.getString("Availability");

                // Create a Student object and add it to the list
                Student student = new Student(name, surname, username, emailAddress, password, Role.STUDENT,
                        dateOfBirth, phoneNumber, degrees, courseAttended, certifications,
                        workExperiences, skills, availability, null); // Set `null` for jobApplications
                students.add(student); // Add to list
            }
        } catch (SQLException e) {
            // Log or handle the exception based on your needs
        }

        return students; // Return list of students
    }

    @Override
    public boolean updateStudent(Student student) {
        String sql = "UPDATE STUDENT SET Name = ?, Surname = ?, EmailAddress = ?, Password = ?, " +
                "DateOfBirth = ?, PhoneNumber = ?, Degrees = ?, CourseAttended = ?, Certifications = ?, " +
                "WorkExperiences = ?, Skills = ?, Availability = ? WHERE Username = ?";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set parameters for the query
            stmt.setString(1, student.getName());
            stmt.setString(2, student.getSurname());
            stmt.setString(3, student.getEmailAddress());
            stmt.setString(4, student.getPassword());
            stmt.setDate(5, Date.valueOf(student.getDateOfBirth())); // Convert LocalDate to java.sql.Date
            stmt.setString(6, student.getPhoneNumber());
            stmt.setString(7, String.join(",", student.getDegrees())); // Convert list to comma-separated string
            stmt.setString(8, String.join(",", student.getCourseAttended()));
            stmt.setString(9, String.join(",", student.getCertifications()));
            stmt.setString(10, String.join(",", student.getWorkExperiences()));
            stmt.setString(11, String.join(",", student.getSkills()));
            stmt.setString(12, student.getAvailability());
            stmt.setString(13, student.getUsername()); // Username as primary key

            int rowsUpdated = stmt.executeUpdate(); // Execute the update query

            return rowsUpdated > 0; // Return true if at least one row was updated
        } catch (SQLException e) {
            // Log or handle the exception based on your needs
        }

        return false; // Return false if no rows were updated or an error occurred
    }

    @Override
    public boolean deleteStudent(String username) {
        String sql = "DELETE FROM STUDENT WHERE Username = ? AND Role = 'STUDENT'";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the username parameter
            stmt.setString(1, username);

            int rowsDeleted = stmt.executeUpdate(); // Execute the delete query

            return rowsDeleted > 0; // Return true if at least one row was deleted
        } catch (SQLException e) {
            // Log or handle the exception based on your needs
        }
        return false; // Return false if no rows were deleted or an error occurred
    }

    @Override
    public boolean studentExists(String username) {
        String sql = "SELECT COUNT(*) FROM STUDENT WHERE Username = ? AND Role = 'STUDENT'";

        try (Connection conn = DatabaseConfig.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Set the username parameter
            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt(1); // Get the count of matching records
                    return count > 0; // Return true if count is greater than 0
                }
            }
        } catch (SQLException e) {
            // Log or handle the exception based on your needs
        }

        return false; // Return false if no matching records are found
    }
}

package org.example.togetjob.dao.concreteobjects;

import org.example.togetjob.dao.abstractobjects.StudentDao;
import org.example.togetjob.model.entity.Student;
import org.example.togetjob.model.entity.Role;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

public class FileSystemStudentDao implements StudentDao {
    private static final String PATH_NAME = "src/main/resources/files_txt/Student.txt";

    @Override
    public void saveStudent(Student student) {
        if (studentExists(student.obtainUsername())) {
            return;  // The student already exists
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME, true))) {
            writer.write(student.obtainName() + ";" + student.obtainSurname() + ";" + student.obtainUsername() + ";" +
                    student.obtainEmailAddress() + ";" + student.obtainPassword() + ";" + student.obtainRole() + ";" +
                    student.obtainDateOfBirth() + ";" +
                    student.obtainPhoneNumber() + ";" +
                    student.obtainDegrees() + ";" +
                    student.obtainCoursesAttended() + ";" +
                    student.obtainCertifications() + ";" +
                    student.obtainWorkExperiences() + ";" +
                    student.obtainSkills() + ";" +
                    student.obtainAvailability());
            writer.newLine();
        } catch (IOException | IllegalArgumentException | DateTimeParseException e) {
            // Handle errors during file writing
        }
    }

    @Override
    public Optional<Student> getStudent(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 14 && data[2].trim().equals(username)) {  // Ensure there are at least 14 elements
                    // Call the new method to build the student object
                    Student student = buildStudentFromData(data);
                    return Optional.of(student);
                }
            }
        } catch (IOException | IllegalArgumentException | DateTimeParseException e) {
            // Handle errors during file reading or data parsing
        }
        return Optional.empty();  // Return empty if student not found
    }

    @Override
    public List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 14) {  // Ensure there are at least 14 elements
                    // Call the new method to build the student object
                    Student student = buildStudentFromData(data);
                    students.add(student);
                }
            }
        } catch (IOException | IllegalArgumentException | DateTimeParseException e) {
            // Handle errors during file reading or data parsing
        }
        return students;  // Return the list of students
    }

    // New method to build a Student object from data
    private Student buildStudentFromData(String[] data) {
        Role role = Role.valueOf(data[5].trim());
        LocalDate dateOfBirth = LocalDate.parse(data[6].trim());
        String phoneNumber = data[7].trim();
        List<String> degrees = Arrays.asList(data[8].split(","));
        List<String> courseAttended = Arrays.asList(data[9].split(","));
        List<String> certifications = Arrays.asList(data[10].split(","));
        List<String> workExperiences = Arrays.asList(data[11].split(","));
        List<String> skills = Arrays.asList(data[12].split(","));
        String availability = data[13].trim();

        // Return the built Student object
        Student student = new Student(
                data[0].trim(),  // name
                data[1].trim(),  // surname
                data[2].trim(),  // username
                data[3].trim(),  // emailAddress
                data[4].trim(),  // password
                role,            // role
                dateOfBirth      // date of birth
        );

        student.setPhoneNumber(phoneNumber);            // phone number
        student.setDegrees(degrees);                    // degrees
        student.setCoursesAttended(courseAttended);     // courses attended
        student.setCertifications(certifications);      // certifications
        student.setWorkExperiences(workExperiences);    // work experiences
        student.setSkills(skills);                      // skills
        student.setAvailability(availability);          // availability
        student.setJobApplications(new ArrayList<>());    // jobApplications, empty for now

        return student;
    }

    @Override
    public boolean updateStudent(Student student) {
        if (!studentExists(student.obtainUsername())) {
            return false;  // The student does not exist
        }

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 6 && data[2].trim().equals(student.obtainUsername())) {
                    // Replace the line with the updated student data
                    line = student.obtainName() + ";" + student.obtainSurname() + ";" + student.obtainUsername() + ";" +
                            student.obtainEmailAddress() + ";" + student.obtainPassword() + ";" + student.obtainRole() + ";" +
                            student.obtainDateOfBirth() + ";" + student.obtainPhoneNumber() + ";" +
                            String.join(",", student.obtainDegrees()) + ";" +
                            String.join(",", student.obtainCoursesAttended()) + ";" +
                            String.join(",", student.obtainCertifications()) + ";" +
                            String.join(",", student.obtainWorkExperiences()) + ";" +
                            String.join(",", student.obtainSkills()) + ";" +
                            student.obtainAvailability();
                }
                lines.add(line);  // Add the line to the buffer
            }
        } catch (IOException | IllegalArgumentException | DateTimeParseException e) {
            // Handle errors during file reading or data parsing
            return false;
        }

        // Write the updated lines to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME))) {
            for (String updatedLine : lines) {
                writer.write(updatedLine);
                writer.newLine();  // Add a new line
            }
        } catch (IOException e) {
            // Handle errors during file writing
            return false;
        }

        return true;  // Update was successful
    }

    @Override
    public boolean deleteStudent(String username) {
        if (!studentExists(username)) {
            return false;  // The student does not exist
        }

        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 6 && !data[2].trim().equals(username)) {
                    // Add only the lines that do not correspond to the student to be deleted
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            // Handle errors during file reading
            return false;
        }

        // Rewrite the file without the deleted student
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PATH_NAME))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();  // Add a new line
            }
            return true;
        } catch (IOException e) {
            // Handle errors during file writing
            return false;
        }
    }

    @Override
    public boolean studentExists(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PATH_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length >= 5 && data[2].trim().equals(username)) {
                    return true;  // Student found
                }
            }
        } catch (IOException e) {
            // Handle errors during file reading
        }
        return false;  // Student not found
    }
}

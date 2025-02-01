package org.example.togetjob.model.dao.concreteobjects;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.togetjob.model.dao.abstractobjects.StudentDao;
import org.example.togetjob.model.entity.Student;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileSystemStudentDao implements StudentDao {

    private static final String PATH_NAME = "/Users/enrico_talone/IdeaProjects/toGetJob/src/main/resources/org/example/togetjob/files_json/Student.json";
    //private static final String PATH_NAME = "files_json/Student.json" ;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean saveStudent(Student student) {
        try {
            List<Student> students = getAllStudents();

            if (students.stream().anyMatch(s -> s.getUsername().equals(student.getUsername()))) {
                System.out.println("The student: " + student.getUsername() + " already exists.");
                return false;
            }

            students.add(student);
            objectMapper.writeValue(new File(PATH_NAME), students);
            System.out.println("The student: " + student.getUsername() + " has been successfully saved in the File System");
            return true;

        } catch (IOException e) {
            System.out.println("The student: " + student.getUsername() + " cannot be saved in the File System");
            System.out.println(e.getMessage());
            return false;
        }
    }


    @Override
    public Optional<Student> getStudent(String username) {
        return getAllStudents().stream().filter(student -> student.getUsername().equals(username)).findFirst();
    }


    @Override
    public List<Student> getAllStudents() {
        try {
            List<Student> students = new ArrayList<>(objectMapper.readValue(new File(PATH_NAME), new TypeReference<List<Student>>() {}));

            if (students.isEmpty()) {
                return new ArrayList<>();
            }

            return students;

        } catch (IOException e) {
            System.out.println("Students cannot be retrieved from File System");
            System.out.println(e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean updateStudent(Student student) {
        try {
            List<Student> students = getAllStudents();
            boolean found = false;

            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getUsername().equals(student.getUsername())) {
                    students.set(i, student); // Update student data
                    found = true;
                    break;
                }
            }

            if (!found) {
                System.out.println("The student: " + student.getUsername() + " doesn't exist.");
                return false;
            }

            objectMapper.writeValue(new File(PATH_NAME), students);
            System.out.println("The student: " + student.getUsername() + " has been successfully updated.");
            return true;

        } catch (IOException e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
            return false;
        }
    }


    @Override
    public boolean deleteStudent(String username) {
        try {
            List<Student> students = getAllStudents();
            boolean removed = students.removeIf(student -> student.getUsername().equals(username));
            if (!removed) {
                System.out.println("The student: " + username + " doesn't exist.");
                return false;
            }

            objectMapper.writeValue(new File(PATH_NAME), students);
            System.out.println("The student: " + username + " has been successfully deleted.");
            return true;

        } catch (IOException e) {
            System.out.println("The student: " + username + " cannot be deleted");
            System.out.println(e.getMessage());
            return false;
        }
    }


    @Override
    public boolean studentExists(String username) {
        try {
            List<Student> students = objectMapper.readValue(new File(PATH_NAME), new TypeReference<List<Student>>() {});

            if (students.isEmpty()) {
                System.out.println("There aren't any students in the File System");
                return false;
            }

            boolean presence = students.stream().anyMatch(student -> student.getUsername().equals(username));

            if (presence) {
                System.out.println("The student: " + username + " exists");
                return true;
            }

            System.out.println("The student: " + username + " doesn't exist");
            return false;

        } catch (IOException e) {
            System.out.println("The student: " + username + " cannot be searched");
            System.out.println(e.getMessage());
            return false;
        }
    }

}

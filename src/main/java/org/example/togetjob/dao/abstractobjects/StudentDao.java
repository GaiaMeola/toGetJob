package org.example.togetjob.dao.abstractobjects;

import org.example.togetjob.model.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDao {

    void saveStudent(Student student);
    Optional<Student> getStudent(String username);
    List<Student> getAllStudents();
}

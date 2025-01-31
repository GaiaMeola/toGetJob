package it.model.dao.abstractobjects;

import it.model.entity.Student;

import java.util.List;
import java.util.Optional;

public interface StudentDao {

    boolean saveStudent(Student student);
    Optional<Student> getStudent(String username);
    List<Student> getAllStudents();
    boolean updateStudent(Student student);
    boolean deleteStudent(String username);
    boolean studentExists(String username);
}

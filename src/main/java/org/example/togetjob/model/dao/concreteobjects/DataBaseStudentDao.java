package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.model.dao.abstractobjects.StudentDao;
import org.example.togetjob.model.entity.Student;

import java.util.List;
import java.util.Optional;

public class DataBaseStudentDao implements StudentDao {

    @Override
    public void saveStudent(Student student) {
    }

    @Override
    public Optional<Student> getStudent(String username) {
        return Optional.empty();
    }

    @Override
    public List<Student> getAllStudents() {
        return List.of();
    }

    @Override
    public boolean updateStudent(Student student) {
        return false;
    }

    @Override
    public boolean deleteStudent(String username) {
        return false;
    }

    @Override
    public boolean studentExists(String username) {
        return false;
    }
}

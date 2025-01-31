package it.model.dao.concreteobjects;

import it.model.dao.abstractobjects.StudentDao;
import it.model.entity.Student;

import java.util.List;
import java.util.Optional;

public class FileSystemStudentDao implements StudentDao {

    @Override
    public boolean saveStudent(Student student) {
        return false;
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

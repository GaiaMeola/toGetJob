package org.example.togetjob.model.dao.concreteobjects;

import org.example.togetjob.model.dao.abstractobjects.StudentDao;
import org.example.togetjob.model.entity.Student;

import java.util.*;

public class InMemoryStudentDao implements StudentDao {

    private static final Map<String, Student> students = new HashMap<>();

    @Override
    public void saveStudent(Student student) {
        if(students.containsKey(student.getUsername())){
            return;
        }
        students.put(student.getUsername(), student);
    }

    @Override
    public Optional<Student> getStudent(String username) {
        return Optional.ofNullable(students.get(username));
    }

    @Override
    public List<Student> getAllStudents() {
        return new ArrayList<>(students.values());
    }

    @Override
    public boolean updateStudent(Student student) {
        if(!students.containsKey(student.getUsername())){
            return false;
        }
        students.put(student.getUsername(),student);
        return true;
    }

    @Override
    public boolean deleteStudent(String username) {
        return students.remove(username) != null;
    }

    @Override
    public boolean studentExists(String username) {
        return students.containsKey(username);
    }
}

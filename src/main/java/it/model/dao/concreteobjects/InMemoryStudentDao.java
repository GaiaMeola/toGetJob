package it.model.dao.concreteobjects;

import it.model.dao.abstractobjects.StudentDao;
import it.model.entity.Student;

import java.util.*;

public class InMemoryStudentDao implements StudentDao {

    private static Map<String, Student> students = new HashMap<>();

    @Override
    public boolean saveStudent(Student student) {
        if(students.containsKey(student.getUsername())){
            return false;
        }
        students.put(student.getUsername(), student);
        return true;
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

package it.model.dao.concreteobjects;

import it.model.dao.abstractobjects.StudentDao;
import it.model.entity.Student;

public class InMemoryStudentDao implements StudentDao {
    @Override
    public void createStudent(Student student) {
    }

    @Override
    public Student getStudent(int id) {
        return null;
    }
}

package it.model.dao.abstractobjects;

import it.model.entity.Student;

public interface StudentDao {

    void createStudent(Student student);
    Student getStudent(int id);
}

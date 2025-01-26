package it.model.dao.abstractobjects;

import it.model.entity.Person;

public interface PersonDao {

    void createPerson(Person person);
    Person getPerson(int id);
}

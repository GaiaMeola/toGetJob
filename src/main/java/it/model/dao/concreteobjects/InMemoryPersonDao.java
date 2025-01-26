package it.model.dao.concreteobjects;

import it.model.dao.abstractobjects.PersonDao;
import it.model.entity.Person;

public class InMemoryPersonDao implements PersonDao {
    @Override
    public void createPerson(Person person) {
    }

    @Override
    public Person getPerson(int id) {
        return null;
    }
}

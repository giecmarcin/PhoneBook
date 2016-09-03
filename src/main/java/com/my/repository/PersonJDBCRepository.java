package com.my.repository;

import com.my.model.Contact;
import com.my.model.Person;

import java.util.List;
import java.util.Map;

/**
 * Created by Marcin on 03.09.2016.
 */
public interface PersonJDBCRepository {
    Person savePerson(Person person);
    Person findPersonById(int id);
    List<Person> findPersonByNameAndLastName(String firstname, String lastname);
    Person saveNewPersonWithNewContacts(Person person, List<Contact> contacts);
    Person findPersonByIdWithAllContacts(int id);
    void saveAlotOfPersonWithNewContactsInTransaction(Map<Person, List<Contact>> personWithContacts);
}

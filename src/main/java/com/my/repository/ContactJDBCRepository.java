package com.my.repository;

import com.my.model.Contact;
import com.my.model.Person;

import java.util.List;

/**
 * Created by Marcin on 03.09.2016.
 */
public interface ContactJDBCRepository {
    boolean isConnected();
    Contact saveContact(Contact contact);
    Contact findContactById(int id);
    List<Contact> findAllContactsByPerson(Person person);
}

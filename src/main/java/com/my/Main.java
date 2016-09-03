package com.my;


import com.my.model.Contact;
import com.my.model.Person;
import com.my.repository.*;

import java.util.*;


/**
 * Created by Marcin on 01.09.2016.
 */

public class Main {

    public static void main(String[] args) {
        final SettingsDb settingsDb = new SettingsDb("jdbc:postgresql://localhost:5432/phonebook", "postgres", "spring@92");
        ContactJDBCRepository contactJDBCRepository = new ContactJDBCRepositoryImpl(settingsDb);
        PersonJDBCRepository personJDBCRepository = new PersonJDBCRepositoryImpl(settingsDb);

        List<Person> people = new ArrayList<>();
        for(int i=0; i<4; i++){
            String personFirstname = UUID.randomUUID().toString();
            String personLastname = UUID.randomUUID().toString();
            people.add(new Person(personFirstname,personLastname));
        }
        for(Person p : people){
            for(int i=0;i<3; i++){
                String type = UUID.randomUUID().toString();
                String value = UUID.randomUUID().toString();
                List<Contact> contacts = new ArrayList<>();
                contacts.add(new Contact(type,value));
                p.getContacts().addAll(contacts);
            }
        }
        Map<Person, List<Contact>> temp = new HashMap<>();
        for(Person p: people){
            System.out.println(p.getFirstName() + " "+ p.getLastname());
            temp.put(p, p.getContacts());
        }
        personJDBCRepository.saveAlotOfPersonWithNewContactsInTransaction(temp);
    }
}

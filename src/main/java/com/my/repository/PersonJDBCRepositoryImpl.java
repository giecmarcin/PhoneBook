package com.my.repository;

import com.my.model.Contact;
import com.my.model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by Marcin on 03.09.2016.
 */
public class PersonJDBCRepositoryImpl implements PersonJDBCRepository {
    private SettingsDb settingsDb;
    private ContactJDBCRepositoryImpl contactJDBCRepository;
    private final Logger LOGGER = Logger.getLogger(PersonJDBCRepositoryImpl.class.getName());

    public PersonJDBCRepositoryImpl(SettingsDb settingsDb) {
        this.settingsDb = settingsDb;
        contactJDBCRepository = new ContactJDBCRepositoryImpl(settingsDb);
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            LOGGER.info(e.getMessage());
        }
    }

    /**
     * This method save person to database.
     * @param person
     * @return Saved object from database without contacts.
     */
    public Person savePerson(Person person) {
        Person personFromDb = null;
        try (Connection con = DriverManager.getConnection(settingsDb.getUrlToDb(), settingsDb.getUser(), settingsDb.getPassowrd());
        ) {
            String query = "INSERT INTO phonebook.people(firstname, lastname) Values(?,?)";
            PreparedStatement preparedStatement = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, person.getFirstName());
            preparedStatement.setString(2, person.getLastname());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                personFromDb = findPersonById(resultSet.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return personFromDb;
    }

    /**
     * This method allows you to find
     * the object by id.
     * @param id - id of Person.
     * @return - founded object from database or null.
     */
    public Person findPersonById(int id) {
        Person person = null;
        try (Connection con = DriverManager.getConnection(settingsDb.getUrlToDb(), settingsDb.getUser(), settingsDb.getPassowrd())
        ) {
            String query = "SELECT * FROM phonebook.people WHERE id_person=?";

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                person = new Person(resultSet.getInt("id_person"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname"));
            }
            return person;
        } catch (SQLException e) {
            e.printStackTrace();
            return person;
        }
    }

    /**
     * This method allows you to find
     * object by name and surname without contacts.
     * @param firstname - name of person.
     * @param lastname - surname of person.
     * @return - The found people with a specific name and surname from database.
     */
    public List<Person> findPersonByNameAndLastName(String firstname, String lastname) {
        List<Person> people = new ArrayList<>();
        try (Connection con = DriverManager.getConnection(settingsDb.getUrlToDb(), settingsDb.getUser(), settingsDb.getPassowrd())
        ) {
            String query = "SELECT * FROM phonebook.people WHERE firstname=? and lastname=?";

            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, firstname);
            preparedStatement.setString(2, lastname);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                people.add(new Person(resultSet.getInt("id_person"),
                        resultSet.getString("firstname"),
                        resultSet.getString("lastname")));
            }
            return people;
        } catch (SQLException e) {
            e.printStackTrace();
            return people;
        }
    }

    /**
     * This method allows you save new person
     * and contacts for this person.
     * @param person
     * @param contacts
     * @return Saved object from database without contacts.
     */
    public Person saveNewPersonWithNewContacts(Person person, List<Contact> contacts) {
        Person personFromDb = null;
        try (Connection con = DriverManager.getConnection(settingsDb.getUrlToDb(), settingsDb.getUser(), settingsDb.getPassowrd());
        ) {
            con.setAutoCommit(false);
            personFromDb = savePerson(person);
            for (Contact c : contacts) {
                c.setIdPerson(personFromDb.getId());
                contactJDBCRepository.saveContact(c);
            }
            return personFromDb;
        } catch (SQLException e) {
            e.printStackTrace();
            return personFromDb;
        }
    }

    /**
     * This method allows you find person by id with
     * all contacts.
     * @param id Id Of Person.
     * @return Person with all contacts.
     */
    @Override
    public Person findPersonByIdWithAllContacts(int id) {
        Person person = findPersonById(id);
        if(person!=null){
            List<Contact> contacts = contactJDBCRepository.findAllContactsByPerson(person);
            person.getContacts().addAll(contacts);
        }else{
            LOGGER.info("Not Found");
        }
        return person;
    }

    @Override
    public void saveAlotOfPersonWithNewContactsInTransaction(Map<Person, List<Contact>> personWithContacts) {
        Connection con = null;
        try{
            con = DriverManager.getConnection(settingsDb.getUrlToDb(), settingsDb.getUser(), settingsDb.getPassowrd());
            con.setAutoCommit(false);
            String insertPersonQuery = "INSERT INTO phonebook.people(firstname, lastname) Values(?,?)";
            String insertContactQuery = "INSERT INTO phonebook.contacts(_type, _value, id_person) Values(?,?,?)";
            Iterator entries = personWithContacts.entrySet().iterator();
            while(entries.hasNext()){
                Map.Entry thisEntry = (Map.Entry) entries.next();
                Person key = (Person) thisEntry.getKey();
                List<Contact> value = (List<Contact>) thisEntry.getValue();

                PreparedStatement preparedStatement = con.prepareStatement(insertPersonQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                preparedStatement.setString(1, key.getFirstName());
                preparedStatement.setString(2, key.getLastname());
                preparedStatement.executeUpdate();
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                resultSet.next();
                for(Contact c: value){
                    PreparedStatement preparedStatementC = con.prepareStatement(insertContactQuery, PreparedStatement.RETURN_GENERATED_KEYS);
                    preparedStatementC.setString(1, c.getType());
                    preparedStatementC.setString(2, c.getValue());
                    preparedStatementC.setInt(3, resultSet.getInt(1));
                    preparedStatementC.executeUpdate();
                    ResultSet resultSetC = preparedStatementC.getGeneratedKeys();
                }
                con.setAutoCommit(true);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}

package com.my.repository;

import com.my.Main;
import com.my.model.Contact;
import com.my.model.Person;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marcin on 03.09.2016.
 */
public class ContactJDBCRepositoryImpl implements ContactJDBCRepository {
    private SettingsDb settingsDb;
    private Connection connection;

    public ContactJDBCRepositoryImpl(SettingsDb settingsDb) {
        this.settingsDb = settingsDb;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(settingsDb.getUrlToDb(), settingsDb.getUser(), settingsDb.getPassowrd());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    /**
     * Checks the database connection.
     * @return true - if connected, false - no connection
     */
    public boolean isConnected() {
        boolean  isConnected = false;
        String query = "SELECT(1) FROM phonebook.contacts";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(true);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(!resultSet.wasNull()){
                isConnected = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  isConnected;
    }

    /**
     * This method save contact to database.
     * @param contact
     * @return Saved object from database.
     */
    public Contact saveContact(Contact contact) {
        Contact contactFromDb = null;
        String query = "INSERT INTO phonebook.contacts(_type, _value, id_person) Values(?,?,?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(true);
            preparedStatement.setString(1, contact.getType());
            preparedStatement.setString(2, contact.getValue());
            preparedStatement.setInt(3, contact.getIdPerson());
            preparedStatement.executeUpdate();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            if (resultSet.next()) {
                contactFromDb = findContactById(resultSet.getInt(1));
            }
            return contactFromDb;
        } catch (SQLException e) {
            e.printStackTrace();
            return contactFromDb;
        }
    }

    /**
     * This method allows you to find
     * the object by id.
     * @param id Id of contact.
     * @return Founded object from database or null.
     */
    public Contact findContactById(int id){
        Contact contact = null;
        String query = "SELECT * FROM phonebook.contacts WHERE _id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            connection.setAutoCommit(true);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                contact = new Contact(resultSet.getInt("_id"),
                        resultSet.getString("_type"),
                        resultSet.getString("_value"),
                        resultSet.getInt("id_person"));
            }
            return  contact;
        } catch (SQLException e) {
            e.printStackTrace();
            return contact;
        }
    }

    /**
     * Returns a list of all contacts for a specific person
     * or empty list.
     * @param person The person to whom we want to find contacts.
     * @return List of all contacts.
     */
    public List<Contact> findAllContactsByPerson(Person person){
        List<Contact> contacts = new ArrayList<>();
        String query = "SELECT * FROM phonebook.contacts WHERE id_person=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, person.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                contacts.add( new Contact(resultSet.getInt("_id"),
                        resultSet.getString("_type"),
                        resultSet.getString("_value"),
                        resultSet.getInt("id_person")));
            }
            return  contacts;
        } catch (SQLException e) {
            e.printStackTrace();
            return contacts;
        }
    }
}

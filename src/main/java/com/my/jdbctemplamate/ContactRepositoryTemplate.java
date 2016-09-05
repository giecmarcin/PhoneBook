package com.my.jdbctemplamate;

import com.my.model.Contact;
import com.my.model.Person;
import com.my.repository.ContactJDBCRepository;
import com.my.repository.SettingsDb;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by mgiec on 9/5/2016.
 */
public class ContactRepositoryTemplate implements ContactJDBCRepository {
    private SingleConnectionDataSource singleConnectionDataSource;
    private JdbcTemplate jdbcTemplate;

    public  ContactRepositoryTemplate(SettingsDb settingsDb){
        singleConnectionDataSource = new SingleConnectionDataSource(settingsDb.getUrlToDb(),settingsDb.getUser(),settingsDb.getPassowrd(), false);
        jdbcTemplate = new JdbcTemplate(singleConnectionDataSource);
    }


    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public Contact saveContact(Contact contact) {
        String query = "INSERT INTO phonebook.contacts(_type, _value, id_person) Values(?,?,?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps =
                                connection.prepareStatement(query, new String[] {"_id"});
                        ps.setString(1, contact.getType());
                        ps.setString(2, contact.getValue());
                        ps.setInt(3, contact.getIdPerson());
                        return ps;
                    }
                },
                keyHolder);
        Contact contactFromDb = null;
        contactFromDb = findContactById(Integer.parseInt(keyHolder.getKey().toString()));
        return  contactFromDb;
    }

    @Override
    public Contact findContactById(int id) {
        String query = "SELECT * FROM phonebook.contacts WHERE _id=?";
        Contact contact = (Contact) jdbcTemplate.queryForObject(
                query, new Object[] {id}, new ContactRowMapper());
        return  contact;

    }

    @Override
    public List<Contact> findAllContactsByPerson(Person person) {
        return null;
    }
}

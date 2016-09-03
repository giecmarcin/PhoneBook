package com.my.jdbctemplamate;

import com.my.model.Contact;
import com.my.model.Person;
import com.my.repository.PersonJDBCRepository;
import com.my.repository.SettingsDb;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 * Created by mgiec on 9/2/2016.
 */

public class PersonRepositoryTemplate implements PersonJDBCRepository {
    private SingleConnectionDataSource singleConnectionDataSource;
    private JdbcTemplate jdbcTemplate;


    public PersonRepositoryTemplate(SettingsDb settingsDb) {
        singleConnectionDataSource = new SingleConnectionDataSource(settingsDb.getUrlToDb(),settingsDb.getUser(),settingsDb.getPassowrd(), false);
        jdbcTemplate = new JdbcTemplate(singleConnectionDataSource);
    }

    @Override
    public Person savePerson(Person person) {
        String query = "INSERT INTO phonebook.people(firstname, lastname) Values(?,?)";
        KeyHolder holder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection)
                    throws SQLException {
                PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, person.getFirstName());
                ps.setString(2, person.getLastname());
                return ps;
            }
        }, holder);
        int newPersonId = holder.getKey().intValue();
        Person personFromDb = null;
        personFromDb = findPersonById(newPersonId);
        return personFromDb;
    }

    @Override
    public Person findPersonById(int id) {
        String query = "INSERT INTO phonebook.people(firstname, lastname) Values(?,?)";
        Person person = this.jdbcTemplate.queryForObject(
                "select first_name, last_name from t_actor where id = ?",
                new Object[]{1212L},
                new RowMapper<Person>() {
                    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Person person1 = new Person();
                        person1.setId(rs.getInt("id_person"));
                        person1.setFirstName(rs.getString("firstname"));
                        person1.setLastname(rs.getString("lastname"));
                        return person1;
                    }
                });
        return  person;
    }

    @Override
    public List<Person> findPersonByNameAndLastName(String firstname, String lastname) {
        return null;
    }

    @Override
    public Person saveNewPersonWithNewContacts(Person person, List<Contact> contacts) {
        return null;
    }

    @Override
    public Person findPersonByIdWithAllContacts(int id) {
        return null;
    }

    @Override
    public void saveAlotOfPersonWithNewContactsInTransaction(Map<Person, List<Contact>> personWithContacts) {

    }

//    public List<Person> findAll(){
//        String sqlSelect = "SELECT * FROM phonebook.people";
//        List<Person> people = jdbcTemplate.query(sqlSelect, new RowMapper<Person>() {
//
//            public Person mapRow(ResultSet result, int rowNum) throws SQLException {
//                Person person = new Person();
//                person.setId(result.getInt("id_person"));
//                person.setFirstName(result.getString("firstname"));
//                person.setLastname(result.getString("lastname"));
//               return person;
//            }
//        });
//        return people;
//    }
}

package com.my.jdbctemplamate;

import com.my.model.Person;
import com.my.repository.SettingsDb;
import org.postgresql.Driver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by mgiec on 9/2/2016.
 */

public class PersonRepository {

    private SimpleDriverDataSource dataSource;
    private SingleConnectionDataSource singleConnectionDataSource;
    private JdbcTemplate jdbcTemplate;

    public PersonRepository(SettingsDb settingsDb) {
//        dataSource = new SimpleDriverDataSource();
//        dataSource.setDriver(new Driver());
//        dataSource.setUrl("jdbc:postgresql://localhost:5432/phonebook");
//        dataSource.setUsername("postgres");
//        dataSource.setPassword("spring@92");
        singleConnectionDataSource = new SingleConnectionDataSource(settingsDb.getUrlToDb(),settingsDb.getUser(),settingsDb.getPassowrd(), false);
        jdbcTemplate = new JdbcTemplate(singleConnectionDataSource);

    }

    public List<Person> findAll(){
        String sqlSelect = "SELECT * FROM phonebook.people";
        List<Person> people = jdbcTemplate.query(sqlSelect, new RowMapper<Person>() {

            public Person mapRow(ResultSet result, int rowNum) throws SQLException {
                Person person = new Person();
                person.setId(result.getInt("id_person"));
                person.setFirstName(result.getString("firstname"));
                person.setLastname(result.getString("lastname"));
               return person;
            }
        });
        return people;
    }
}

package com.my.jdbctemplamate;

import com.my.model.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Marcin on 03.09.2016.
 */
public class PersonRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Person person = new Person();
        person.setId(resultSet.getInt("id_person"));
        person.setFirstName(resultSet.getString("firstname"));
        person.setLastname(resultSet.getString("lastname"));
        return person;
    }
}

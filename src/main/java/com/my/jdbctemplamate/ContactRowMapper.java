package com.my.jdbctemplamate;

import com.my.model.Contact;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by mgiec on 9/5/2016.
 */
public class ContactRowMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Contact contact = new Contact();
        contact.setId(resultSet.getInt("_id"));
        contact.setType(resultSet.getString("_type"));
        contact.setValue(resultSet.getString("_value"));
        contact.setIdPerson(resultSet.getInt("id_person"));
        return contact;
    }
}

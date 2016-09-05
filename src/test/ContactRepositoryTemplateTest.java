import com.my.jdbctemplamate.ContactRepositoryTemplate;
import com.my.model.Contact;
import com.my.model.Person;
import com.my.repository.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by mgiec on 9/5/2016.
 */
public class ContactRepositoryTemplateTest {

    final SettingsDb settingsDb = new SettingsDb("jdbc:postgresql://localhost:5432/phonebook", "postgres", "spring@92");
    final PersonJDBCRepository personJDBCRepository = new PersonJDBCRepositoryImpl(settingsDb);
    final ContactJDBCRepository contactJDBCRepository = new ContactRepositoryTemplate(settingsDb);

    @Test
    public void testSaveContact(){
        String type = UUID.randomUUID().toString();
        String value = UUID.randomUUID().toString();

        String personFirstname = UUID.randomUUID().toString();
        String personLastname = UUID.randomUUID().toString();
        Person person = new Person(personFirstname,personLastname);
        Person personFromDb = personJDBCRepository.savePerson(person);

        Contact contact = new Contact(type,value,personFromDb.getId());
        Contact contactFromDb = contactJDBCRepository.saveContact(contact);
        Assert.assertEquals(contact, contactFromDb);
    }

    @Test
    public void testFindContactById(){
        String type = UUID.randomUUID().toString();
        String value = UUID.randomUUID().toString();

        String personFirstname = UUID.randomUUID().toString();
        String personLastname = UUID.randomUUID().toString();
        Person person = new Person(personFirstname,personLastname);
        Person personFromDb = personJDBCRepository.savePerson(person);

        Contact contact = new Contact(type,value,personFromDb.getId());
        Contact contactFromDb = contactJDBCRepository.saveContact(contact);
        Contact contactFounded = contactJDBCRepository.findContactById(contactFromDb.getId());
        Assert.assertEquals(contact,contactFounded);
    }
}

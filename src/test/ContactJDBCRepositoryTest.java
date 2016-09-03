import com.my.model.Contact;
import com.my.model.Person;
import com.my.repository.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.hamcrest.CoreMatchers.*;

/**
 * Created by Marcin on 03.09.2016.
 */
public class ContactJDBCRepositoryTest {
    final SettingsDb settingsDb = new SettingsDb("jdbc:postgresql://localhost:5432/phonebook", "postgres", "spring@92");
    final PersonJDBCRepository personJDBCRepository = new PersonJDBCRepositoryImpl(settingsDb);
    final ContactJDBCRepository contactJDBCRepository = new ContactJDBCRepositoryImpl(settingsDb);

    //a. Połaczenie do bazy danych i test czy połączenie działa (SELECT 1 lub odpowiednik)
    @Test
    public void testConnection(){
        Assert.assertTrue("Ok", contactJDBCRepository.isConnected());
    }

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

    @Test
    public void testFindAllContactsByPerson(){
        String personFirstname = UUID.randomUUID().toString();
        String personLastname = UUID.randomUUID().toString();
        Person person = new Person(personFirstname,personLastname);
        Person personFromDb = personJDBCRepository.savePerson(person);

        List<Contact> contacts = new ArrayList<>();
        for(int i=0; i<4; i++){
            String type = UUID.randomUUID().toString();
            String value = UUID.randomUUID().toString();
            Contact c = new Contact(type,value);
            c.setIdPerson(personFromDb.getId());
            contacts.add(c);
            contactJDBCRepository.saveContact(c);
        }
        List<Contact> contactsFromDb = contactJDBCRepository.findAllContactsByPerson(personFromDb);
        Assert.assertThat(contacts,is(contactsFromDb));
    }
}
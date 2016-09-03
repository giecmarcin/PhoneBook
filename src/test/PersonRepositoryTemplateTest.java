import com.my.jdbctemplamate.PersonRepositoryTemplate;
import com.my.model.Person;
import com.my.repository.PersonJDBCRepository;
import com.my.repository.PersonJDBCRepositoryImpl;
import com.my.repository.SettingsDb;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by Marcin on 03.09.2016.
 */
public class PersonRepositoryTemplateTest {
    final SettingsDb settingsDb = new SettingsDb("jdbc:postgresql://localhost:5432/phonebook", "postgres", "spring@92");
    final PersonJDBCRepository personJDBCRepository = new PersonRepositoryTemplate(settingsDb);

    @Test
    public void testSavePerson(){
        String personFirstname = UUID.randomUUID().toString();
        String personLastname = UUID.randomUUID().toString();
        Person person = new Person(personFirstname,personLastname);
        Person personFromDb = personJDBCRepository.savePerson(person);

        System.out.println("Program: " + person.getFirstName() + " "  + personLastname );
        System.out.println("Baza   : " + personFromDb.getFirstName() + " "  + personFromDb.getLastname() );
        //Assert.assertEquals(person,personFromDb);
    }
}

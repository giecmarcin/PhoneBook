import com.my.model.Contact;
import com.my.model.Person;
import com.my.repository.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.hamcrest.CoreMatchers.*;

/**
 * Created by Marcin on 03.09.2016.
 */
public class PersonJDBCReposiotryTest {

    final SettingsDb settingsDb = new SettingsDb("jdbc:postgresql://localhost:5432/phonebook", "postgres", "spring@92");
    final PersonJDBCRepository personJDBCRepository = new PersonJDBCRepositoryImpl(settingsDb);
    final ContactJDBCRepository contactJDBCRepository = new ContactJDBCRepositoryImpl(settingsDb);

    @Test
    public void testSavePerson(){
        String personFirstname = UUID.randomUUID().toString();
        String personLastname = UUID.randomUUID().toString();
        Person person = new Person(personFirstname,personLastname);
        Person personFromDb = personJDBCRepository.savePerson(person);
        Assert.assertEquals(person,personFromDb);
    }

    @Test
    public void findPersonById(){
        String personFirstname = UUID.randomUUID().toString();
        String personLastname = UUID.randomUUID().toString();
        Person person = new Person(personFirstname,personLastname);
        Person personFromDb = personJDBCRepository.savePerson(person);

        Person personFounded = personJDBCRepository.findPersonById(personFromDb.getId());
        Assert.assertEquals(person, personFounded);
    }

    //c. Przeszukanie bazy danych pod kątem osób o określonym imieniu i nazwisku (z użyciem PreparedStatement) i ich wyświetlenie
    @Test
    public void testFindPersonByNameAndLastName(){
        String personFirstname = UUID.randomUUID().toString();
        String personLastname = UUID.randomUUID().toString();
        Person person = new Person(personFirstname,personLastname);
        Person personFromDb = personJDBCRepository.savePerson(person);
        List<Person> lOfPeople= personJDBCRepository.findPersonByNameAndLastName(personFirstname, personLastname);
        Person personFounded = lOfPeople.get(0);
        for(Person p : lOfPeople){
            System.out.println("Id: " + p.getId() + ". Name: " + p.getLastname() + " Lastname: " + p.getLastname());
        }
        Assert.assertEquals(person, personFounded);
    }

    //b. Stworzenie przykładowej osoby z kontaktami i zapisanie jej w bazie danych
    @Test
    public void saveNewPersonWithNewContacts(){
        String personFirstname = UUID.randomUUID().toString();
        String personLastname = UUID.randomUUID().toString();
        Person person = new Person(personFirstname,personLastname);

        List<Contact> contacts = new ArrayList<>();
        for(int i=0; i<3; i++){
            String type = UUID.randomUUID().toString();
            String value = UUID.randomUUID().toString();
            contacts.add(new Contact(type,value));
        }

        Person personFromDb = personJDBCRepository.saveNewPersonWithNewContacts(person, contacts);
        List<Contact> contactsFromDb = contactJDBCRepository.findAllContactsByPerson(personFromDb);
        Assert.assertEquals(person, personFromDb);
        Assert.assertThat(contacts, is(contactsFromDb));
    }

    @Test
    public void testFindPersonByIdWithAllContacts(){
        String personFirstname = UUID.randomUUID().toString();
        String personLastname = UUID.randomUUID().toString();
        Person person = new Person(personFirstname,personLastname);
        Person personFromDb = personJDBCRepository.savePerson(person);
        List<Contact> contacts = new ArrayList<>();
        for(int i=0; i<3; i++){
            String type = UUID.randomUUID().toString();
            String value = UUID.randomUUID().toString();
            Contact c = new Contact(type, value, personFromDb.getId());
            contactJDBCRepository.saveContact(c);
            contacts.add(c);
        }

        Person personFromDbWithAllContacts = personJDBCRepository.findPersonByIdWithAllContacts(personFromDb.getId());
        List<Contact> contactsFromDb = personFromDbWithAllContacts.getContacts();

        for(int i=0;i<contactsFromDb.size();i++){
            Assert.assertEquals(contacts.get(i), contactsFromDb.get(i));
        }
    }

    //d. Stworzenie transakcji w trakcie której zostanie stworzone kilka osób z kontaktami a następnie transakcja ta zostanie wycofana
    @Test
    public void testSaveAlotOfPersonWithNewContactsInTransaction(){
        List<Person> people = new ArrayList<>();
        //Create a list Of People
        for(int i=0; i<4; i++){
            String personFirstname = UUID.randomUUID().toString();
            String personLastname = UUID.randomUUID().toString();
            people.add(new Person(personFirstname,personLastname));
        }
        //Create contacts for people
        for(Person p : people){
            for(int i=0;i<3; i++){
                String type = UUID.randomUUID().toString();
                String value = UUID.randomUUID().toString();
                List<Contact> contacts = new ArrayList<>();
                contacts.add(new Contact(type,value));
                p.getContacts().addAll(contacts);
            }
        }
        Map<Person, List<Contact>> temp = new HashMap<>();
        for(Person p: people){
            System.out.println(p.getFirstName() + " "+ p.getLastname());
            temp.put(p, p.getContacts());
        }

        personJDBCRepository.saveAlotOfPersonWithNewContactsInTransaction(temp);
        for(Person p: people){
            List<Person> lOfPerson = personJDBCRepository.findPersonByNameAndLastName(p.getFirstName(), p.getLastname());
            Person tempPerson = lOfPerson.get(0);
            System.out.println("P: " + p.getId() + " " + p.getFirstName() + " " + p.getLastname());
            System.out.println();
            System.out.println("L: " + lOfPerson.get(0).getId() + " " + lOfPerson.get(0).getFirstName() + " " + lOfPerson.get(0).getLastname());
            //Assert.assertEquals(p,tempPerson);
        }
    }
}

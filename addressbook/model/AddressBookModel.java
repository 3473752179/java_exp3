package addressbook.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AddressBookModel {
    private final List<Person> persons = new ArrayList<>();

    public void addPerson(Person person) {
        persons.add(person);
    }

    public void removePerson(int index) {
        if (index >= 0 && index < persons.size()) {
            persons.remove(index);
        }
    }

    public Person getPerson(int index) {
        return persons.get(index);
    }

    public int size() {
        return persons.size();
    }

    public List<Person> getPersons() {
        return Collections.unmodifiableList(persons);
    }
}

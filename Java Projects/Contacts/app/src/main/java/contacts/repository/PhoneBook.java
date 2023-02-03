package contacts.repository;

import contacts.contact.Contact;

import java.util.ArrayList;
import java.util.List;


public class PhoneBook {

    private final List<Contact> contacts;

    public PhoneBook() {
        contacts = new ArrayList<>();
    }

    public Contact getContact(int index) {
        return contacts.get(index);
    }

    public void removeContact(Contact contact) {
        contacts.remove(contact);
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void removeContact(int index) {
        contacts.remove(index);
    }

    public List<Contact> getContacts() {
        return contacts;
    }

    public boolean isEmpty() {
        return contacts.isEmpty();
    }

    public int size() {
        return contacts.size();
    }
}

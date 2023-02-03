package contacts.factory;

import contacts.contact.Contact;

public interface ContactFactory {
    Contact createContact(String type);
}

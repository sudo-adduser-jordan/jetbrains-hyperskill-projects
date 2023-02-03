package contacts.factory;

import contacts.contact.Contact;
import contacts.contact.Organization;
import contacts.contact.Person;

public class ContactCreation implements ContactFactory {
    @Override
    public Contact createContact(String type) {
        switch (type) {
            case "person":
                return new Person();
            case "organization":
                return new Organization();
            default:
                System.out.println("wrong input");
                return null;
        }
    }
}

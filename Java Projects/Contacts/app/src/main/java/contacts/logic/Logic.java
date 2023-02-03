package contacts.logic;

import contacts.contact.Contact;
import contacts.factory.ContactCreation;
import contacts.factory.ContactFactory;
import contacts.repository.FileRepository;
import contacts.repository.PhoneBook;

import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Logic {
    private final FileRepository fileRepository;
    private final PhoneBook phoneBook;
    Scanner scanner;

    public Logic(String[] path) {
        fileRepository = new FileRepository(path);
        scanner = new Scanner(System.in);
        this.phoneBook = fileRepository.deserializePhoneBook();
    }
    public void serialize() {
        fileRepository.serializePhoneBook(phoneBook);
    }
    public void addAction() {
        System.out.print("Enter the type (person, organization): ");
        String type = scanner.nextLine();

        ContactFactory contactCreation = new ContactCreation();
        Contact contact = contactCreation.createContact(type);

        phoneBook.addContact(contact);
        System.out.println("The record added.");
    }

    public void removeAction() {
        if (phoneBook.isEmpty()) {
            System.out.println("No records to remove!");
            return;
        }

        System.out.print("Select a record: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;
        phoneBook.removeContact(index);
        System.out.println("The record removed!");
    }

    public void editAction() {
        if (phoneBook.isEmpty()) {
            System.out.println("No records to edit!");
            return;
        }
        listContacts();

        System.out.print("Select a record: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;
        Contact contact = phoneBook.getContact(index);

        logicEdit(contact);
        System.out.println("The record updated!");
    }

    public void countAction() {
        System.out.printf("The Phone Book has %d records.\n", phoneBook.size());
    }
    public void searchAction() {
        while (true) {
            System.out.print("Enter search query: ");
            System.out.println(listQueriedContacts());

            System.out.print("[search] Enter action ([number], back, again): ");
            String command = scanner.nextLine();
            switch (command) {
                case "again":
                    break;
                case "back":
                    return;
                default:
                    if (command.matches("\\d+")) {
                        Contact contact = phoneBook.getContact(Integer.parseInt(command) - 1);
                        System.out.println(contact + "\n");
                        record(contact);
                        return;
                    }
            }
        }
    }
    public void infoAction() {
        if (phoneBook.isEmpty()) {
            return;
        }
        listContacts();

        System.out.print("Enter index to show info: ");
        int index = Integer.parseInt(scanner.nextLine()) - 1;

        Contact contact = phoneBook.getContact(index);
        System.out.println(contact);

    }

    private void logicEdit(Contact contact) {
        try {
            contact.edit();
        } catch (InvocationTargetException | NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void listContacts() {
        for (int i = 0; i < phoneBook.size(); i++) {
            System.out.println((i + 1) + ". " + phoneBook.getContact(i).getFullName());
        }
    }

    public void listAction() {
        listContacts();
        System.out.println();
        System.out.print("[list] Enter action ([number], back): ");
        String action = scanner.nextLine();

        if (action.matches("\\d+")) {
            Contact contact = phoneBook.getContact(Integer.parseInt(action) - 1);
            System.out.println(contact + "\n");
            this.record(contact);
        }

    }
    public void record(Contact contact) {
        while (true) {
            System.out.print("[record] Enter action (edit, delete, menu): ");
            String action = scanner.nextLine();

            switch (action) {
                case "edit":
                    logicEdit(contact);
                    System.out.println(contact + "\n");
                    break;
                case "delete":
                    phoneBook.removeContact(contact);
                    break;
                case "menu":
                    return;
                default:
                    System.out.println("wrong input");
            }
        }

    }
    private String listQueriedContacts() {
        int count = 0;
        String searchQuery = scanner.nextLine();
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < phoneBook.size(); i++) {
            if (phoneBook.getContact(i).getFullName().toUpperCase().contains(searchQuery.toUpperCase())) {
                str.append((i + 1)).append(". ").append(phoneBook.getContact(i).getFullName()).append("\n");
                count++;
            } else if (phoneBook.getContact(i).getNumber().toUpperCase().contains(searchQuery.toUpperCase())) {
                str.append((i + 1)).append(". ").append(phoneBook.getContact(i).getFullName()).append("\n");
                count++;
            }

        }
        System.out.printf("Found %d results: \n", count);
        return str.toString();
    }

}

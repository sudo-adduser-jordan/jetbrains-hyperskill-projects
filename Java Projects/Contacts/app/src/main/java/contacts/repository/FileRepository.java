package contacts.repository;

import java.io.*;

public class FileRepository {
    private final String filePath;

    public FileRepository(String[] filePath) {
        this.filePath = filePath.length == 1 ? filePath[0] : null;
    }

    public void serializePhoneBook(PhoneBook phoneBook) {
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(phoneBook);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Something went wrong serialization");
        }
    }

    public PhoneBook deserializePhoneBook() {
        if (filePath == null) {
            return new PhoneBook();
        }
        System.out.println("open " + filePath + "\n");
        File file = new File(filePath);
        if (file.length() == 0) {
            return new PhoneBook();
        }
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedInputStream bis = new BufferedInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(bis);
            PhoneBook phoneBook = (PhoneBook) ois.readObject();
            ois.close();
            return phoneBook;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return new PhoneBook();
    }
}
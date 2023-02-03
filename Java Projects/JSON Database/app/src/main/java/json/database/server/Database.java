package json.database.server;


import com.google.gson.Gson;

import java.io.FileReader;
import java.io.FileWriter;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Database {

    String fileName;
    ReadWriteLock lock;
    Lock readLock, writeLock;

     //The object's constructor with the creation of the database file and
     //creating the necessary locks
    public Database(String fileName) {
        this.fileName = fileName;

        lock = new ReentrantReadWriteLock();
        readLock = lock.readLock();
        writeLock = lock.writeLock();

        try {
            writeLock.lock();

            FileWriter writer = new FileWriter(fileName);
            writer.write("{}");
            writer.close();

            writeLock.unlock();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

     //Read all data from file of database, set data into database and write all data to file
     //@param key - String, key of the database cell where the data should be placed
     //@param value - String, data to be placed
     //@return - boolean, result of operation
    public boolean setData(String key, String value) {
        boolean result = false;
        try {
            writeLock.lock();

            FileReader reader = new FileReader(fileName);
            Map<String, String> storage = new Gson().fromJson(reader, Map.class);
            reader.close();

            storage.put(key, value);
            FileWriter writer = new FileWriter(fileName);
            writer.write(new Gson().toJson(storage));
            writer.close();

            writeLock.unlock();

            result = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

     //Getting data from the database file
     //@param key - String, key of the database cell
     //@return - String, receiving data
    public String getData(String key) {
        String result = null;
        try {
            readLock.lock();

            FileReader reader = new FileReader(fileName);
            Map<String, String> storage = new Gson().fromJson(reader, Map.class);
            reader.close();

            result = storage.get(key);

            readLock.unlock();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

     //Delete data from database file
     //@param key - String, key of the database cell
     //@return - boolean result of operation
    public boolean deleteData(String key) {
        boolean result = false;
        try {
            writeLock.lock();

            FileReader reader = new FileReader(fileName);
            Map<String, String> storage = new Gson().fromJson(reader, Map.class);
            reader.close();

            if (storage.containsKey(key)) {
                storage.remove(key);
                result = true;
            }

            FileWriter writer = new FileWriter(fileName);
            writer.write(new Gson().toJson(storage));
            writer.close();

            writeLock.unlock();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
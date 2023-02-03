package file.server.server;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FileServer {
    private static final String DATA_FILENAME = "_FileServerData.ser";
    private final Path rootDir;

    private ConcurrentMap<String, Integer> fileToIdMap;
    private ConcurrentMap<Integer, String> idToFileMap;
    private AtomicInteger nextID;

    public FileServer(Path rootDir) {
        this.rootDir = rootDir;
        try {
            Files.createDirectories(rootDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initializeFilesMap();
    }

    private synchronized void initializeFilesMap() {
        Path dataFile = rootDir.resolve(DATA_FILENAME);
        if (!Files.exists(dataFile)) {
            nextID = new AtomicInteger(1);
            fileToIdMap = new ConcurrentHashMap<>();
            idToFileMap = new ConcurrentHashMap<>();
            return;
        }

        try (   InputStream inputStream = Files.newInputStream(dataFile);
                ObjectInput objectInput = new ObjectInputStream(inputStream)
        ) {
            nextID = new AtomicInteger(objectInput.readInt());
            fileToIdMap = (ConcurrentMap<String, Integer>) objectInput.readObject();
            idToFileMap = (ConcurrentMap<Integer, String>) objectInput.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    void stop() {
        Path dataFile = rootDir.resolve(DATA_FILENAME);

        try (   OutputStream outputStream = Files.newOutputStream(dataFile);
                ObjectOutput objectOutput = new ObjectOutputStream(outputStream)
        ) {
            synchronized (this) {
                objectOutput.writeInt(nextID.get());
                objectOutput.writeObject(fileToIdMap);
                objectOutput.writeObject(idToFileMap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int put(String filename, byte[] fileContentBinary) {
        Integer oldId;
        int newId;
        synchronized (this) {
            newId = nextID.getAndIncrement();
            if (filename.isBlank()) {
                filename = "unnamed_file_" + newId;
            }
            oldId = fileToIdMap.putIfAbsent(filename, newId);
            idToFileMap.putIfAbsent(newId, filename);
        }
        if (oldId != null) {
            return -1;
        }

        Path file = rootDir.resolve(filename);
        try {
            Files.write(file, fileContentBinary);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newId;
    }

    public boolean delete(String filename) {
        Integer id;
        synchronized (this) {
            id = fileToIdMap.remove(filename);
            idToFileMap.remove(id);
        }
        if (id == null) {
            return false;
        }

        Path file = rootDir.resolve(filename);
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean delete(int id) {
        String filename;
        synchronized (this) {
            filename = idToFileMap.remove(id);
            fileToIdMap.remove(filename);
        }
        if (filename == null) {
            return false;
        }

        Path file = rootDir.resolve(filename);
        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public byte[] get(String filename) {
        Integer id = fileToIdMap.get(filename);
        if (id == null) {
            return null;
        }

        Path file = rootDir.resolve(filename);
        try {
            return Files.readAllBytes(file);
        } catch (IOException e) {
            e.printStackTrace();
            return "Файл был когда-то корректно добавлен (есть в Map), но потом его физически удалили с диска"
                    .getBytes(StandardCharsets.UTF_8);
        }
    }

    public byte[] get(int id) {
        String filename = idToFileMap.get(id);
        if (filename == null) {
            return null;
        }

        Path file = rootDir.resolve(filename);
        try {
            return Files.readAllBytes(file);
        } catch (IOException e) {
            e.printStackTrace();
            return "Файл был когда-то корректно добавлен (есть в Map), но потом его физически удалили с диска"
                    .getBytes(StandardCharsets.UTF_8);
        }
    }

}

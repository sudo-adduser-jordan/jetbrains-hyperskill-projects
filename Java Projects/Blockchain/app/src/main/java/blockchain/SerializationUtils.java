package blockchain;

import java.io.*;
import java.util.List;

class SerializationUtils {

    public static void serialize(List<Block> blockchain, String fileName) throws IOException {
        FileOutputStream fos = new FileOutputStream(fileName);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(blockchain);
        oos.close();
    }


    public static List<Block> deserialize(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(fileName);
        BufferedInputStream bis = new BufferedInputStream(fis);
        ObjectInputStream ois = new ObjectInputStream(bis);
        Object obj = ois.readObject();
        ois.close();
        return (List<Block>) obj;
    }
}
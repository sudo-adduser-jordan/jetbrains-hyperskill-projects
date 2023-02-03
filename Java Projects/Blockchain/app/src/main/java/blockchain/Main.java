package blockchain;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {
    public static final String FILENAME = "newdata.txt";
    public static int count = 0;
    public static int msgCount = 0;
    static Message message = Message.getInstance();
    static List<List<String>> msgList = message.getMsgList();

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Database database = new Database();
        File filename = new File(FILENAME);
        if (ifBlockchainExists(filename)) {
            loadBlockchain(database, FILENAME);
            if (!validationFunction(database)) {
                System.out.println("Blockchain is invalid!");
                System.exit(0);
            }
        }

        for (int i = 0; i < 15; i++) {
            int miner = i + 1;
            Thread thread = new Thread(() -> {
                try {
                    generatedBlocks(database, count, miner);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            });

            thread.start();
            thread.join();

        }
    }

    private static boolean ifBlockchainExists(File filename) {
        return filename.length() != 0;
    }

    public static void generatedBlocks(Database database, int start, int miner) throws IOException {
        String hashOfThePreviousBlock = "0";
        String msg;
        if (database.getPreviousBlock() != null) {
            hashOfThePreviousBlock = database.getPreviousBlock().getHashOfTheBlock();
        }

        if (msgCount < msgList.size()) {
            msg = String.join("\n", msgList.get(msgCount));
            msgCount++;
        } else {
            msg = "no messages";
        }
        Block block = new Block(database.getBlockchain().size() + 1, hashOfThePreviousBlock, start, miner, msg);
        if (database.getBlockchain().size() > 0) {
            if (validationOFBlock(database, block, start)) {
                database.addBlock(block);
                saveBlockchain(database, FILENAME);
                System.out.println(block);
                setCount(block);
                System.out.println();

            } else {
                System.out.println("Block is invalid");
            }

        } else {
            database.addBlock(block);
            saveBlockchain(database, FILENAME);
            System.out.println(block);
            setCount(block);
            System.out.println();

        }
        System.out.println();
    }


    public static void setCount(Block block) {
        if (block.getGeneratingFor() < -1) {
            count++;
            System.out.printf("N was increased to %d", count);
        } else if (block.getGeneratingFor() < 60) {

            System.out.println("N stays the same");
        } else {
            count--;
            System.out.printf("N was decreased by %d", count);

        }
    }


    public static boolean validationFunction(Database database) {
        boolean flag = true;
        for (int i = 1; i < database.getBlockchain().size(); i++) {
            if (!database.getBlockchain().get(i).getPreviousHash().equals(database.getBlockchain().get(i - 1).getHashOfTheBlock())) {
                flag = false;
                break;
            }
        }
        return flag;
    }


    public static boolean validationOFBlock(Database database, Block block, int start) {
        String zeros = "0".repeat(start);
        return database.getBlockchain().get(database.getBlockchain().size() - 1).getHashOfTheBlock()
                .equals(block.getPreviousHash())
                && block.getHashOfTheBlock().substring(0, start).equals(zeros);
    }

    public static void saveBlockchain(Database database, String filename) throws IOException {
        SerializationUtils.serialize(database.getBlockchain(), filename);
    }

    public static void loadBlockchain(Database database, String filename) throws IOException, ClassNotFoundException {
        database.setBlockchain(SerializationUtils.deserialize(filename));
    }
}

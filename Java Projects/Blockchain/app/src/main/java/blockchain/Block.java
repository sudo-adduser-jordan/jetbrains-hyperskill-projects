package blockchain;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class Block implements Serializable {
    private long id;
    private long timeStamp;
    private String previousHash;
    Random random = new Random();
    private long magicNumber;
    private String hashOfTheBlock;
    private long generatingFor;
    private int miner;
    private String msg;


    public long getGeneratingFor() {
        return generatingFor;
    }


    public Block(long id, String hashOfThePreviousBlock, int start, int miner, String msg) {
        this.id = id;
        this.timeStamp = new Date().getTime();
        this.previousHash = hashOfThePreviousBlock;
        this.miner = miner;
        this.msg = msg;

        String zeros = "0".repeat(start);

        do {
            magicNumber = random.nextLong();
            this.hashOfTheBlock = calculateBlockHash();
        } while (!hashOfTheBlock.substring(0, start).equals(zeros));

        generatingFor = TimeUnit.MILLISECONDS.toSeconds(new Date().getTime() - timeStamp);

    }

    private String calculateBlockHash() {
        String dataToHash = id
                + previousHash
                + magicNumber
                + msg
                + timeStamp;

        return StringUtil.applySha256(dataToHash);
    }

    public long getId() {
        return id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public String getHashOfTheBlock() {
        return hashOfTheBlock;
    }

    public void setHashOfTheBlock(String hashOfTheBlock) {
        this.hashOfTheBlock = hashOfTheBlock;
    }

    @Override
    public String toString() {
        return "Block:" + '\n' +
                "Created by: miner" + miner + '\n' +
                "miner" + miner + " gets 100 VC"+'\n' +
                "Id: " + id + '\n' +
                "Timestamp: " + timeStamp + '\n' +
                "Magic number: " + magicNumber + '\n' +
                "Hash of the previous block:" + '\n' + previousHash + '\n' +
                "Hash of the block:" + '\n' + hashOfTheBlock + '\n' +
                "Block data:" + '\n' + msg + '\n' +
                "Block was generating for " + generatingFor + " seconds";
    }
}

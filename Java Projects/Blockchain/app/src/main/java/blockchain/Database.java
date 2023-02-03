package blockchain;

import java.util.ArrayList;
import java.util.List;

public class Database {
    volatile List<Block> blockchain = new ArrayList<>();

    public void addBlock(Block block) {
        blockchain.add(block);
    }

    public Database() {
    }

    public Block getPreviousBlock() {
        if (!blockchain.isEmpty()) {
            return blockchain.get(blockchain.size() - 1);
        } else {
            return null;
        }
    }


    public List<Block> getBlockchain() {
        return blockchain;
    }

    public void setBlockchain(List<Block> blockchain) {
        this.blockchain = blockchain;
    }
}

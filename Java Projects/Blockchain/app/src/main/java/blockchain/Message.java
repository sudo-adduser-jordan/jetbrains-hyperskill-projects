package blockchain;

import java.util.List;

public class Message {

    private static final Message instance = new Message();

    public static Message getInstance() {
        return instance;
    }

    private Message() {
    }

    private final List<String> client1 = List.of("No transactions");
    private final List<String> client2 = List.of("miner1sent 30 VC to miner1","miner1 sent 30 VC to miner2","miner9 sent 30 VC to Nick");
    private final List<String> client3 = List.of("miner2 sent 10 VC to Bob"
            , "miner4 sent 10 VC to Alice"
            , "Nick sent 1 VC to ShoesShop"
            , "Nick sent 2 VC to FastFood"
            , "Nick sent 15 VC to CarShop"
            , "miner3 sent 90 VC to CarShop");

    private final List<String> client4 = List.of("CarShop sent 10 VC to Worker1", "CarShop sent 10 VC to Worker2");
    private final List<String> client5 = List.of("CarShop sent 10 VC to Worker3", "CarShop sent 30 VC to Director1");

    private final List<String> client6 = List.of("CarShop sent 45 VC to CarPartsShop");
    private final List<String> client7 = List.of("CarShop sent 45 VC to CarPartsShop");
    private final List<String> client8 = List.of("CarShop sent 45 VC to CarPartsShop"
            , "Bob sent 5 VC to GamingShop"
            , "miner7 sent 90 VC to CarShop");
    private final List<String> client9 = List.of("Alice sent 5 VC to BeautyShop"

            , "Mia sent 90 VC to CarShop");
    private final List<String> client10 = List.of("Mia sent 10 VC to Bob"

            , "Nick sent 1 VC to ShoesShop"
            , "Nick sent 2 VC to FastFood"
            , "Nick sent 15 VC to CarShop"
            , "Bob sent 10 VC to CarShop");
    private final List<String> client11 = List.of("miner10 sent 10 VC to Bob"
            , "miner11 sent 10 VC to Alice"
            , "Nick sent 1 VC to ShoesShop"
            , "Nick sent 2 VC to FastFood"
            , "Nick sent 15 VC to CarShop"
            , "miner5 sent 90 VC to CarShop");
    private final List<String> client12 = List.of("miner2 sent 10 VC to Bob"
            , "miner4 sent 10 VC to Alice"
            , "Nick sent 1 VC to ShoesShop"
            , "Nick sent 2 VC to FastFood"
            , "Nick sent 15 VC to CarShop"
            , "miner5 sent 10 VC to CarShop");
    private final List<String> client13 = List.of("miner9 sent 10 VC to Bob"
            , "miner1 sent 10 VC to Alice"
            , "Nick sent 1 VC to ShoesShop"
            , "Nick sent 2 VC to FastFood"
            , "Nick sent 15 VC to CarShop"
            , "miner3 sent 15 VC to CarShop");

    private final List<String> client14 = List.of("miner4 sent 10 VC to Bob"
            , "miner2 sent 10 VC to Alice"
            , "Nick sent 1 VC to ShoesShop"
            , "Nick sent 2 VC to FastFood"
            , "Nick sent 15 VC to CarShop"
            , "miner1 sent 5 VC to CarShop");
    private final List<String> client15 = List.of("miner3 sent 10 VC to Bob"
            , "miner2 sent 10 VC to Alice"
            , "Nick sent 1 VC to ShoesShop"
            , "Nick sent 2 VC to FastFood"
            , "Nick sent 15 VC to CarShop"
            , "miner5 sent 10 VC to CarShop");



    private final List<List<String>> msgList = List.of(client1, client2, client3, client4, client5,
            client6,client7,client8,client9,client10,client11,client12,client13,client14,client15);


    public List<List<String>> getMsgList() {
        return msgList;
    }
}

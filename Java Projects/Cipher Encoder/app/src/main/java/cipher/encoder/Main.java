package cipher.encoder;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Menu();
    }

    public static void Menu() {
        //scan user input
        Scanner scannerMenu = new Scanner(System.in);
        System.out.println("Please input operation (encode/decode/exit):");

        while (scannerMenu.hasNext()) {
            String menuSelection = scannerMenu.nextLine();
            switch (menuSelection) {
                case "encode" -> Encode();
                case "decode" -> Decode();
                case "exit" -> Exit();
                default -> System.out.println("There is no '" + menuSelection + "' operation");
            }
            System.out.println();
            System.out.println("Please input operation (encode/decode/exit):");
        }
    }

    public static void Encode() {
        System.out.println("Input string: ");
        Scanner scannerEncode = new Scanner(System.in);
        String stringA = scannerEncode.nextLine();
        System.out.println("Encoded string:");

        //Loop over string to convert and print binary character results
        int i;
        StringBuilder binaryValue = new StringBuilder();
        for (i = 0; i < stringA.length(); i++) {
            String charInBinary = Integer.toBinaryString(stringA.charAt(i));
            //Fill binary string with zeroes to get 7 bit.
            charInBinary = "0000000".substring(charInBinary.length()) + charInBinary;
            binaryValue.append(charInBinary);
        }

        //Convert binary string to "Chuck Norris Code".
        char lastChar = ' ';
        StringBuilder codedMessage = new StringBuilder();
        String[] encodedBits = new String[]{" 00 0", " 0 0"};
        for (i = 0; i < binaryValue.length(); i++) {
            if (binaryValue.charAt(i) != lastChar) {
                lastChar = binaryValue.charAt(i);
                codedMessage.append(encodedBits[lastChar - '0']);
            } else {
                codedMessage.append("0");
            }
        }

        //Print encoded string.
        System.out.println(codedMessage.substring(1));
    }

    public static void Decode() {

        System.out.println("Input encoded string:");

        //scan user input
        Scanner userInput = new Scanner(System.in);
        String stringA = userInput.nextLine();

        //check if string only contains 0
        boolean onlyZero = stringA.contains("0") && stringA.contains(" ");

        if (!onlyZero) {
            System.out.println("Encoded string is not valid.");
        } else {
            //convert user input from unary to binary
            Scanner scanner = new Scanner(stringA);
            StringBuilder stringB = new StringBuilder();
            while (scanner.hasNext()) {
                String block = scanner.next().trim();
                if (block.equals("0")) {
                    String stringOfOnes = scanner.next().replace("0", "1");
                    stringB.append(stringOfOnes);
                } else if (block.equals("00")) {
                    String stringOfZeros = scanner.next();
                    stringB.append(stringOfZeros);
                }
            }
            scanner.close();

            //check if binary string is valid
            if (stringB.length() % 7 != 0) {
                System.out.println("Encoded string is not valid.");
            } else {

                //add spaces every n index
                int i = 7;
                int n = 8;
                while (i < stringB.length()) {
                    stringB.insert(i, " ");
                    i = i + n;
                }

                //scan integers and convert from binary into char
                Scanner scanBinary = new Scanner(String.valueOf(stringB));
                StringBuilder stringDecoded = new StringBuilder();
                while (scanBinary.hasNext()) {
                    String binaryBlock = scanBinary.next();
                    int binaryToCharacter = Integer.parseInt(binaryBlock, 2);
                    stringDecoded.append((char) binaryToCharacter);
                }
                scanBinary.close();

                //print result
                System.out.println("Decoded string: ");
                System.out.println(stringDecoded);
            }
        }
    }

    public static void Exit () {
            System.out.println("Bye!");
            System.exit(0);
    }

}


package encryption.decryption;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

//entry point
    public class Main {
        private static String mode = "enc";
        private static String in = null;
        private static String out = null;
        private static int key = 0;
        private static String data = "";
        private static String alg = null;
        private static Strategy strategy;

        public static void main(String[] args) {
            //get argument variables
            for (int i = 0; i < args.length; i++) {
                switch (args[i]) {
                    case "-mode" -> mode = args[++i];
                    case "-key" -> key = Integer.parseInt(args[++i]);
                    case "-data" -> data = args[++i];
                    case "-in" -> in = args[++i];
                    case "-out" -> out = args[++i];
                    case "-alg" -> alg = args[++i];
                }
            }
            checkAlgorithm();
            checkOperation();
        }

        // init alg
        private static void checkAlgorithm() {
            if ("shift".equals(alg)) {
                strategy = new Shift();
            } else {
                strategy = new Unicode();
            }
        }

        // check data, print result
        private static void checkOperation() {
            if (!data.isEmpty() && out.isEmpty()) {
                System.out.println(strategy.doOperation(mode, data, key));
            } else if (!data.isEmpty()) {
                outputFile(strategy.doOperation(mode, data, key));
            }  else if (!in.isEmpty() && out.isEmpty()) {
                inputFile();
                System.out.println(strategy.doOperation(mode, in, key));
            } else if (!in.isEmpty()) {
                inputFile();
                outputFile(strategy.doOperation(mode, in, key));
            }
        }

        // get input file
        private static void inputFile() {
            try {
                in = Files.readString(Path.of(in));
            } catch (Exception e) {
                System.out.println("Error!" + e);
                System.exit(0);
            }
        }

        // put file
        private static void outputFile(String data) {
            try {
                File file = new File(out);
                FileWriter writer = new FileWriter(file);
                writer.write(data);
                writer.close();
            } catch (Exception e) {
                System.out.println("Error!" + e);
                System.exit(0);
            }
        }

    }
package sorting.tool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Main {
    private static String dataType = null;
    private static String sortingType = "natural";
    private static String inputFile = null;
    private static String outputFile = null;

    public static void main(final String[] args) throws FileNotFoundException {
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-sortingType")) {
                    sortingType = i < args.length - 1 ? args[i + 1] : null;
                    if (sortingType == null) {
                        throw new IllegalArgumentException("No sorting type defined!");
                    }
                }
                if (args[i].equals("-dataType")) {
                    dataType = i < args.length - 1 ? args[i + 1] : null;
                    if (dataType == null) {
                        throw new IllegalArgumentException("No data type defined!");
                    }
                }
                if (args[i].equals("-inputFile")) {
                    inputFile = args[i + 1];
                }
                if (args[i].equals("-outputFile")) {
                    outputFile = args[i + 1];
                }
            }
            if (dataType == null) {
                throw new IllegalArgumentException("No data type defined!");
            }
        } catch (IllegalArgumentException e) {
            if (outputFile != null) {
                System.out.println("here");
                try {
                    File file = new File(outputFile);
                    FileOutputStream fos = new FileOutputStream(file);
                    PrintStream ps = new PrintStream(fos);
                    System.setOut(ps);
                    System.out.println(e.getMessage());
                    return;
                } catch (FileNotFoundException ex) {
                    System.out.println("file not found");
                    return;
                }
            } else {
                System.out.println(e.getMessage());
                return;
            }
        }
        switch (dataType) {
            case "long" -> SortLong.run(sortingType, inputFile);
            case "line" -> SortLine.run(sortingType, inputFile);
            default -> SortWord.run(sortingType, inputFile);
        }
    }
}


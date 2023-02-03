package sorting.tool;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class SortWord {
    static List<String> words = new ArrayList<>();

    public static void run(String mode , String input) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        if (input != null) {
            System.setIn(new FileInputStream(input));
        }
        while (scanner.hasNext()) {
            String word = scanner.next();
            words.add(word);
        }
        if (mode.equals("natural")) sortWordNaturally();
        else sortWordByCount();
    }

    private static void sortWordNaturally() {
        words.sort(Comparator.comparing(String::length));
        System.out.printf("Total words: %d.\n", words.size());
        System.out.print("Sorted data: ");
        for (String word : words) {
            System.out.print(word + " ");
        }
    }

    private static void sortWordByCount() {
        Collections.sort(words);
        words.sort(Comparator.comparingInt(s -> Collections.frequency(words, s)));
        Set<String> lines = new LinkedHashSet<>(words);
        long times;
        double percentage;
        System.out.printf("Total words: %d.\n", words.size());
        for (String word : lines) {
            times = Collections.frequency(words, word);
            percentage = (double) (times) / words.size() * 100;
            System.out.printf("%s: %d time(s), %d.", word, times, Math.round(percentage));
            System.out.print("%\n");

        }
    }
}

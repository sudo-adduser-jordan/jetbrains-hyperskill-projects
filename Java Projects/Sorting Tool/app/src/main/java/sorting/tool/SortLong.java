package sorting.tool;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

public class SortLong {
    static List<Long> numbers = new ArrayList<>();

    public static void run(String mode, String input) throws FileNotFoundException {
        Scanner scanner = new Scanner(System.in);
        if (input != null) {
            System.setIn(new FileInputStream(input));
        }

        while (scanner.hasNextLong()) {
            long number = scanner.nextLong();
            numbers.add(number);
        }
        if (mode.equals("natural")) sortLongNaturally();
        else sortLongByCount();
    }

    public static void sortLongNaturally() {
        Collections.sort(numbers);
        System.out.printf("Total numbers: %d.\n", numbers.size());
        System.out.print("Sorted data: ");
        for (long number : numbers) {
            System.out.print(number + " ");
        }

    }

    public static void sortLongByCount() {
        System.out.printf("Total numbers: %d.\n", numbers.size());
        Collections.sort(numbers);
        numbers.sort(Comparator.comparingInt(n -> Collections.frequency(numbers, n)));
        Set<Long> set = new LinkedHashSet<>(numbers);

        long times;
        double percentage;
        for (long number : set) {
            times = Collections.frequency(numbers, number);
            percentage = (double) (times) / numbers.size() * 100;
            System.out.printf("%s: %d time(s), %d", number, times, Math.round(percentage));
            System.out.print("%\n");
        }

    }


}

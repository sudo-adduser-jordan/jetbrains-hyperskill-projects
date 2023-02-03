package file.type.analyzer.algorithms;

import file.type.analyzer.tools.InputParameters;

import java.util.ArrayList;
import java.util.List;

public class KMP implements SearchStrategy {
    public boolean applyAlgorithm(byte[] text, String pattern) {
        return KMPSearch(text, pattern).size() > 0;
    }

    private static List<Integer> KMPSearch(byte[] text, String pattern) {
        int[] prefixFunc = prefixFunction(pattern);
        ArrayList<Integer> occurrences = new ArrayList<>();
        byte[] patternBytes = pattern.getBytes();
        int j = 0;

        for (int i = 0; i < text.length; i++) {
            while (j > 0 && text[i] != patternBytes[j]) {
                j = prefixFunc[j - 1];
            }

            if (text[i] == patternBytes[j]) {
                j += 1;
            }

            if (j == pattern.length()) {
                occurrences.add(i - j + 1);
                j = prefixFunc[j - 1];
            }
        }

        return occurrences;
    }

    private static int[] prefixFunction(String str) {
        int[] prefixFunc = new int[str.length()];

        for (int i = 1; i < str.length(); i++) {
            int j = prefixFunc[i - 1];

            while (j > 0 && str.charAt(i) != str.charAt(j)) {
                j = prefixFunc[j - 1];
            }

            if (str.charAt(i) == str.charAt(j)) {
                j += 1;
            }

            prefixFunc[i] = j;
        }

        return prefixFunc;
    }
}

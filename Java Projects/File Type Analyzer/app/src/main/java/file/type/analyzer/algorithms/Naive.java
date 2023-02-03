package file.type.analyzer.algorithms;

import file.type.analyzer.tools.InputParameters;

public class Naive implements SearchStrategy {
    public boolean applyAlgorithm(byte[] text, String pattern) {
        boolean result = false;

        for (int i = 0; i < text.length; i++) {
            if (text[i] == pattern.charAt(0)) {
                result = true;

                for (int j = 1; j < pattern.length(); j++) {
                    if (text[i + j] != pattern.charAt(j)) {
                        result = false;
                        break;
                    }
                }

                if (result) {
                    break;
                }
            }
        }

        return result;
    }
}

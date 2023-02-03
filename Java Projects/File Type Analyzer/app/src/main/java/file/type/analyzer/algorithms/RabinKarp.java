package file.type.analyzer.algorithms;

public class RabinKarp implements SearchStrategy {
    public boolean applyAlgorithm(byte[] text, String pattern) {
        return rabinKarpSearch(new String(text), pattern);
    }

    private boolean rabinKarpSearch(String text, String pattern) {
        int a = 117;
        long m = 173_961_102_589_771L;

        if (pattern.length() > text.length()) {
            return false;
        }

        long patternHash = 0;
        long currSubstrHash = 0;
        long pow = 1;

        for (int i = 0; i < pattern.length(); i++) {
            patternHash += (long) pattern.charAt(i) * pow;
            patternHash %= m;

            currSubstrHash += (long) text.charAt(text.length() - pattern.length() + i) * pow;
            currSubstrHash %= m;

            if (i != pattern.length() - 1) {
                pow = pow * a % m;
            }
        }

        for (int i = text.length(); i >= pattern.length(); i--) {
            if (patternHash == currSubstrHash) {
                for (int j = 0; j < pattern.length(); j++) {
                    if (text.charAt(i - pattern.length() + j) != pattern.charAt(j)) {
                        break;
                    }
                }

                return true;
            }

            if (i > pattern.length()) {
                currSubstrHash = (currSubstrHash - text.charAt(i - 1) * pow % m + m) * a % m;
                currSubstrHash = (currSubstrHash + text.charAt(i - pattern.length() - 1)) % m;
            }
        }

        return false;
    }
}

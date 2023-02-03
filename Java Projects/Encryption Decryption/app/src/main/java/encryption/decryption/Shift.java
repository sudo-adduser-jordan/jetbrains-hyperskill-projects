package encryption.decryption;


public class Shift implements Strategy {

    @Override
    public String doOperation(String mode, String data, int key) {
        char[] charArray = data.toCharArray();

        if ("enc".equals(mode)) {
            for (int i = 0; i < charArray.length; i++) {
                if (charArray[i] < 97 || charArray[i] > 122)
                    continue;
                if (charArray[i] >= 122 - key) {
                    charArray[i] = (char) (charArray[i] + key - 26);
                } else {
                    charArray[i] = (char) (charArray[i] + key);
                }
            }
            for (int i = 0; i < charArray.length; i++) {
                if (charArray[i] < 65 || charArray[i] > 90)
                    continue;
                if (charArray[i] >= 90 - key) {
                    charArray[i] = (char) (charArray[i] + key - 26);
                } else {
                    charArray[i] = (char) (charArray[i] + key);
                }
            }

        } else {
            for (int i = 0; i < charArray.length; i++) {
                if (charArray[i] < 97 || charArray[i] > 122)
                    continue;
                if (charArray[i] < 97 + key) {
                    charArray[i] = (char) (charArray[i] - key + 26);
                } else {
                    charArray[i] = (char) (charArray[i] - key);
                }
            }
            for (int i = 0; i < charArray.length; i++) {
                if (charArray[i] < 65 || charArray[i] > 90)
                    continue;
                if (charArray[i] < 65 + key) {
                    charArray[i] = (char) (charArray[i] - key + 26);
                } else {
                    charArray[i] = (char) (charArray[i] - key);
                }
            }
        }
        return String.valueOf(charArray);
    }
}
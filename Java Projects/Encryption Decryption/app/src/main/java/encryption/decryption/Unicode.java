package encryption.decryption;

// strategy pattern for unicode alg
public class Unicode implements Strategy {

    @Override
    public String doOperation(String mode, String data, int key) {
        char[] charArray = data.toCharArray();

        if ("enc".equals(mode)) {
            for (int i = 0; i < charArray.length; i++) {
                charArray[i] = (char) (charArray[i] + key);
            }

        } else {
            for (int i = 0; i < charArray.length; i++) {
                charArray[i] = (char) (charArray[i] - key);
            }
        }
        return String.valueOf(charArray);
    }
}
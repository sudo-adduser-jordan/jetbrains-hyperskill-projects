package encryption.decryption;

public interface Strategy {
    public String doOperation(String mode, String data, int key);
}
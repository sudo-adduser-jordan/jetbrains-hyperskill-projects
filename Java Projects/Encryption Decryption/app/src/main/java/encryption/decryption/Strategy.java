package encryption.decryption;

// strategy pattern for encrytion or decryption
public interface Strategy {
    public String doOperation(String mode, String data, int key);
}
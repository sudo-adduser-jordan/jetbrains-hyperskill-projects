package banking.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor
public class Account {
    private int id;
    private String accountNumber;
    private String pin;
    private int balance;
}

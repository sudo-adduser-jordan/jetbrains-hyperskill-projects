package antifraud.response;

import antifraud.model.TransactionStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TransactionResponse {
    private TransactionStatus result;
}
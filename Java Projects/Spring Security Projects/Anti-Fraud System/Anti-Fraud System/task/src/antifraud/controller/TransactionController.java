package antifraud.controller;

import antifraud.model.TransactionStatus;
import antifraud.request.TransactionRequest;

import antifraud.response.TransactionResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TransactionController {


    @PostMapping("/api/antifraud/transaction")
    public ResponseEntity<TransactionResponse> transaction(@RequestBody TransactionRequest transactionRequest) {
        long amount = transactionRequest.getAmount();
        if (amount <= 0) {
            return ResponseEntity.badRequest().build();
        }

        TransactionResponse response = new TransactionResponse();
        if (amount <= 200) {
            response.setResult(TransactionStatus.ALLOWED);
        } else if (amount <= 1500) {
            response.setResult(TransactionStatus.MANUAL_PROCESSING);
        } else {
            response.setResult(TransactionStatus.PROHIBITED);
        }

        return ResponseEntity.ok(response);
    }
}

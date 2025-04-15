package com.lordbucket.bucketbank.controller.api;

import com.lordbucket.bucketbank.dto.TransactionDTO;
import com.lordbucket.bucketbank.dto.requests.PoSRequest;
import com.lordbucket.bucketbank.dto.requests.TransferRequest;
import com.lordbucket.bucketbank.model.transaction.PoSTransaction;
import com.lordbucket.bucketbank.model.transaction.TransferTransaction;
import com.lordbucket.bucketbank.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/t")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // id is for sender
    @PostMapping("/transfer")
    public ResponseEntity<TransactionDTO> createTransfer(TransferRequest transferRequest) {
        TransferTransaction transaction = transactionService.transfer(transferRequest.getSenderAccountId(), transferRequest.getReceiverAccountId(), transferRequest.getAmount(), transferRequest.getNote());
        return ResponseEntity.ok(new TransactionDTO(transaction));
    }

    // id is here for receiver
    @PostMapping("/pos")
    public ResponseEntity<TransactionDTO> createPoSTransfer(PoSRequest posRequest) {
        PoSTransaction transaction = transactionService.transfer(posRequest.getSenderAccountId(), posRequest.getMerchantAccountId(), posRequest.getAmount());
        return ResponseEntity.ok(new TransactionDTO(transaction));
    }
}

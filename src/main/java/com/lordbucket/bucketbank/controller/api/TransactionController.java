package com.lordbucket.bucketbank.controller.api;

import com.lordbucket.bucketbank.dto.TransactionDTO;
import com.lordbucket.bucketbank.dto.requests.NCPRequest;
import com.lordbucket.bucketbank.dto.requests.PoSRequest;
import com.lordbucket.bucketbank.dto.requests.TransferRequest;
import com.lordbucket.bucketbank.model.transaction.NCPTransaction;
import com.lordbucket.bucketbank.model.transaction.PoSTransaction;
import com.lordbucket.bucketbank.model.transaction.Transaction;
import com.lordbucket.bucketbank.model.transaction.TransferTransaction;
import com.lordbucket.bucketbank.repository.TransactionRepository;
import com.lordbucket.bucketbank.service.TransactionService;
import com.lordbucket.bucketbank.util.exceptions.TransactionNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/t")
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionRepository transactionRepository;

    public TransactionController(TransactionService transactionService, TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.transactionRepository = transactionRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransaction(@PathVariable int id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(TransactionNotFoundException::new);
        return ResponseEntity.ok(new TransactionDTO(transaction));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionDTO> createTransfer(@RequestBody TransferRequest transferRequest) {
        System.out.println(transferRequest.toString());
        TransferTransaction transaction = transactionService.transfer(transferRequest.getSenderAccountId(), transferRequest.getReceiverAccountId(), transferRequest.getAmount(), transferRequest.getNote());
        return ResponseEntity.ok(new TransactionDTO(transaction));
    }

    @PostMapping("/pos")
    public ResponseEntity<TransactionDTO> createPoSTransfer(@RequestBody PoSRequest posRequest) {
        PoSTransaction transaction = transactionService.transfer(posRequest.getSenderAccountId(), posRequest.getMerchantAccountId(), posRequest.getAmount());
        return ResponseEntity.ok(new TransactionDTO(transaction));
    }

    @PostMapping("/ncp")
    public ResponseEntity<TransactionDTO> createNCPTransaction(@RequestBody NCPRequest ncpRequest) {
        NCPTransaction transaction = transactionService.transfer(ncpRequest.getSenderAccountId(), ncpRequest.getMerchantAccountId(), ncpRequest.getCvc(), ncpRequest.getAmount());
        return ResponseEntity.ok(new TransactionDTO(transaction));
    }
}

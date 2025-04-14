package com.lordbucket.bucketbank.controller.api;

import com.lordbucket.bucketbank.dto.AccountDTO;
import com.lordbucket.bucketbank.dto.AccountSummaryDTO;
import com.lordbucket.bucketbank.model.Account;
import com.lordbucket.bucketbank.model.transaction.TransferTransaction;
import com.lordbucket.bucketbank.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    AccountService accountService;
    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccount(@PathVariable int id) {
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(new AccountDTO(account));
    }

    @GetMapping("/summary/{id}")
    public ResponseEntity<AccountSummaryDTO> getAccountSummary(@PathVariable int id) {
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(new AccountSummaryDTO(account));
    }

    @GetMapping(params = {"cardNumber"})
    public ResponseEntity<AccountDTO> getAccountByCardNumber(@RequestParam String cardNumber) {
        Account account = accountService.getAccountByCardNumber(cardNumber);
        return ResponseEntity.ok(new AccountDTO(account));
    }

    @GetMapping(value = "/summary", params = "cardNumber")
    public ResponseEntity<AccountSummaryDTO> getAccountSummaryByCardNumber(@RequestParam String cardNumber) {
        Account account = accountService.getAccountByCardNumber(cardNumber);
        return ResponseEntity.ok(new AccountSummaryDTO(account));
    }
}

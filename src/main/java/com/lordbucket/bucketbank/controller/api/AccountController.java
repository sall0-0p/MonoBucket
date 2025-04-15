package com.lordbucket.bucketbank.controller.api;

import com.lordbucket.bucketbank.dto.AccountDTO;
import com.lordbucket.bucketbank.dto.AccountSummaryDTO;
import com.lordbucket.bucketbank.dto.requests.*;
import com.lordbucket.bucketbank.middleware.annotation.RoleRequirement;
import com.lordbucket.bucketbank.model.Account;
import com.lordbucket.bucketbank.service.AccountService;
import com.lordbucket.bucketbank.util.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/a")
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

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestParam CreateAccountRequest createAccountRequest) {
        Account account = accountService.createAccount(createAccountRequest.getUserId());
        return ResponseEntity.ok(new AccountDTO(account));
    }

    @RoleRequirement(Role.ADMINISTRATOR)
    @PostMapping("/{id}/suspend")
    public ResponseEntity<AccountDTO> suspendAccount(@PathVariable int id) {
        Account account = accountService.suspend(id);
        return ResponseEntity.ok(new AccountDTO(account));
    }

    @PostMapping("/{id}/reinstate")
    public ResponseEntity<AccountDTO> reinstateAccount(@PathVariable int id) {
        Account account = accountService.reinstate(id);
        return ResponseEntity.ok(new AccountDTO(account));
    }

    @PatchMapping("/{id}/rename")
    public ResponseEntity<AccountDTO> renameAccount(@PathVariable int id, @RequestBody RenameAccountRequest renameAccountRequest) {
        Account account = accountService.rename(id, renameAccountRequest.getDisplayName());
        return ResponseEntity.ok(new AccountDTO(account));
    }

    @PostMapping("/{id}/access")
    public ResponseEntity<AccountDTO> giveAccess(@PathVariable int id, @RequestBody GrantAccessRequest grantAccessRequest) {
        Account account = accountService.giveUserAccessToAccount(grantAccessRequest.getUserId(), id);
        return ResponseEntity.ok(new AccountDTO(account));
    }

    @DeleteMapping("/{id}/access")
    public ResponseEntity<AccountDTO> removeAccess(@PathVariable int id, @RequestBody RemoveAccessRequest removeAccessRequest) {
        Account account = accountService.removeUserAccessToAccount(removeAccessRequest.getUserId(), id);
        return ResponseEntity.ok(new AccountDTO(account));
    }

    @PostMapping("/{id}/transfer-ownership")
    public ResponseEntity<AccountDTO> transferOwnership(@PathVariable int id, @RequestBody TransferOwnershipRequest transferOwnershipRequest) {
        Account account = accountService.transferOwnershipOfAccount(id, transferOwnershipRequest.getUserId());
        return ResponseEntity.ok(new AccountDTO(account));
    }

    @PatchMapping("/{id}/merchant")
    public ResponseEntity<AccountDTO> merchantOwnership(@PathVariable int id, @RequestBody ChangeMerchantStatus changeMerchantStatus) {
        Account account = accountService.changeMerchantStatus(id, changeMerchantStatus.isMerchant());
        return ResponseEntity.ok(new AccountDTO(account));
    }
}

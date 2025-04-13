package com.lordbucket.bucketbank.service;

import com.lordbucket.bucketbank.model.Account;
import com.lordbucket.bucketbank.model.User;
import com.lordbucket.bucketbank.model.transaction.DepositTransaction;
import com.lordbucket.bucketbank.model.transaction.TransferTransaction;
import com.lordbucket.bucketbank.model.transaction.WithdrawalTransaction;
import com.lordbucket.bucketbank.repository.AccountRepository;
import com.lordbucket.bucketbank.repository.TransactionRepository;
import com.lordbucket.bucketbank.repository.UserRepository;
import com.lordbucket.bucketbank.util.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AccountServiceTests {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private User user1;
    private User user2;

    @BeforeEach
    public void setup() {
        // Create two users for testing purposes.
        user1 = new User();
        user1.setUsername("accountUser1");
        user1.setPinHash("hash1");
        user1 = userRepository.save(user1);

        user2 = new User();
        user2.setUsername("accountUser2");
        user2.setPinHash("hash2");
        user2 = userRepository.save(user2);
    }

    @Test
    public void testCreateAccount() throws Exception {
        Account account = accountService.createAccount(user1.getId());
        assertNotNull(account);
        assertEquals(user1.getId(), account.getOwner().getId());
    }

    @Test
    public void testDeposit() throws Exception {
        Account account = accountService.createAccount(user1.getId());
        account.setBalance(BigDecimal.valueOf(1000));
        account.setAuthorizedUsers(new HashSet<>());
        account = accountRepository.save(account);

        BigDecimal depositAmount = BigDecimal.valueOf(250);
        Account updated = accountService.deposit(account.getId(), depositAmount);
        assertEquals(BigDecimal.valueOf(1250), updated.getBalance());

        Account finalAccount = account;
        Exception ex = assertThrows(InvalidAmountException.class,
                () -> accountService.deposit(finalAccount.getId(), BigDecimal.valueOf(-50)));
        assertTrue(ex.getMessage().contains("Deposited amount has to be positive."));
    }

    @Test
    public void testWithdraw() throws Exception {
        Account account = accountService.createAccount(user1.getId());
        account.setBalance(BigDecimal.valueOf(1000));
        account.setAuthorizedUsers(new HashSet<>());
        account = accountRepository.save(account);

        BigDecimal withdrawAmount = BigDecimal.valueOf(300);
        Account updated = accountService.withdraw(account.getId(), withdrawAmount);
        assertEquals(BigDecimal.valueOf(700), updated.getBalance());

        Account finalAccount = account;
        Exception ex = assertThrows(InvalidAmountException.class,
                () -> accountService.withdraw(finalAccount.getId(), BigDecimal.valueOf(-100)));
        assertTrue(ex.getMessage().contains("Withdrawn amount has to be positive."));
    }

    @Test
    public void testTransfer() throws Exception {
        // Create fresh accounts for sender and receiver.
        Account sender = accountService.createAccount(user1.getId());
        sender.setBalance(BigDecimal.valueOf(1000));
        sender.setAuthorizedUsers(new HashSet<>());
        sender = accountRepository.save(sender);

        Account receiver = accountService.createAccount(user2.getId());
        receiver.setBalance(BigDecimal.valueOf(500));
        receiver.setAuthorizedUsers(new HashSet<>());
        receiver = accountRepository.save(receiver);

        BigDecimal transferAmount = BigDecimal.valueOf(200);
        accountService.transfer(sender.getId(), receiver.getId(), transferAmount, "Test transfer");
        Account updatedSender = accountRepository.findById(sender.getId()).orElseThrow();
        Account updatedReceiver = accountRepository.findById(receiver.getId()).orElseThrow();

        assertEquals(BigDecimal.valueOf(800), updatedSender.getBalance());
        assertEquals(BigDecimal.valueOf(700), updatedReceiver.getBalance());

        Account finalSender = sender;
        Account finalReceiver = receiver;
        Exception ex = assertThrows(InvalidAmountException.class,
                () -> accountService.transfer(finalSender.getId(), finalReceiver.getId(), BigDecimal.valueOf(-50), "Invalid transfer"));
        assertTrue(ex.getMessage().contains("Transfer amount has to be positive."));
    }

    @Test
    public void testSuspendAndReinstate() throws Exception {
        Account account = accountService.createAccount(user1.getId());
        account = accountRepository.save(account);

        Account suspended = accountService.suspend(account.getId());
        assertTrue(suspended.isSuspended());
        Account reinstated = accountService.reinstate(account.getId());
        assertFalse(reinstated.isSuspended());
    }

    @Test
    public void testRename() throws Exception {
        Account account = accountService.createAccount(user1.getId());
        account = accountRepository.save(account);
        String newDisplayName = "New Primary Account";

        Account renamed = accountService.rename(account.getId(), newDisplayName);
        assertEquals(newDisplayName, renamed.getDisplayName());
    }

    @Test
    public void testGiveAndRemoveUserAccess() throws Exception {
        Account account = accountService.createAccount(user1.getId());
        account.setAuthorizedUsers(new HashSet<>());
        account = accountRepository.save(account);

        Account updated = accountService.giveUserAccessToAccount(user2.getId(), account.getId());
        assertTrue(updated.getAuthorizedUsers().contains(user2));

        updated = accountService.removeUserAccessToAccount(user2.getId(), account.getId());
        assertFalse(updated.getAuthorizedUsers().contains(user2));
    }

    @Test
    public void testTransferOwnership() throws Exception {
        Account account = accountService.createAccount(user1.getId());
        account.setAuthorizedUsers(new HashSet<>());
        account = accountRepository.save(account);

        // Give user2 access first; then transfer ownership.
        accountService.giveUserAccessToAccount(user2.getId(), account.getId());
        Account updated = accountService.transferOwnershipOfAccount(user1.getId(), account.getId(), user2.getId());
        assertEquals(user2.getId(), updated.getOwner().getId());
        assertFalse(updated.getAuthorizedUsers().contains(user2));
    }
}

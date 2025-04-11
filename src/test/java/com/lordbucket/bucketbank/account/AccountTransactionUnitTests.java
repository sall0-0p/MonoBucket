package com.lordbucket.bucketbank.account;

import com.lordbucket.bucketbank.model.Account;
import com.lordbucket.bucketbank.model.User;
import com.lordbucket.bucketbank.repository.AccountRepository;
import com.lordbucket.bucketbank.repository.UserRepository;
import com.lordbucket.bucketbank.service.AccountService;
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
public class AccountTransactionUnitTests {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    private User user1;
    private User user2;

    @BeforeEach
    public void setup() {
        // Create two users
        user1 = new User();
        user1.setUsername("user1");
        user1.setPinHash("hash1");
        user1 = userRepository.save(user1);

        user2 = new User();
        user2.setUsername("user2");
        user2.setPinHash("hash2");
        user2 = userRepository.save(user2);
    }

    @Test
    public void testDepositWithSeparateAccount() {
        // Create a fresh account for deposit
        Account account = accountService.createAccount(user1.getId());
        account.setBalance(BigDecimal.valueOf(1000));
        account = accountRepository.save(account);

        BigDecimal depositAmount = BigDecimal.valueOf(250);
        Account updated = accountService.deposit(account.getId(), depositAmount);

        assertEquals(BigDecimal.valueOf(1250), updated.getBalance());
    }

    @Test
    public void testWithdrawWithSeparateAccount() {
        // Create a fresh account for withdraw test
        Account account = accountService.createAccount(user1.getId());
        account.setBalance(BigDecimal.valueOf(1000));
        account = accountRepository.save(account);

        BigDecimal withdrawAmount = BigDecimal.valueOf(300);
        Account updated = accountService.withdraw(account.getId(), withdrawAmount);

        assertEquals(BigDecimal.valueOf(700), updated.getBalance());
    }

    @Test
    public void testTransferWithSeparateAccounts() {
        // Create a separate sender account for transfer test
        Account sender = accountService.createAccount(user1.getId());
        sender.setBalance(BigDecimal.valueOf(1000));
        sender = accountRepository.save(sender);

        // Create a separate receiver account for transfer test
        Account receiver = accountService.createAccount(user2.getId());
        receiver.setBalance(BigDecimal.valueOf(500));
        receiver = accountRepository.save(receiver);

        BigDecimal transferAmount = BigDecimal.valueOf(200);

        accountService.transfer(sender.getId(), receiver.getId(), transferAmount, "Test transfer");

        Account updatedSender = accountRepository.findById(sender.getId()).orElseThrow();
        Account updatedReceiver = accountRepository.findById(receiver.getId()).orElseThrow();

        assertEquals(BigDecimal.valueOf(800), updatedSender.getBalance());
        assertEquals(BigDecimal.valueOf(700), updatedReceiver.getBalance());
    }
}

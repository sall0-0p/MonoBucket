package com.lordbucket.bucketbank.service;

import com.lordbucket.bucketbank.model.Account;
import com.lordbucket.bucketbank.model.User;
import com.lordbucket.bucketbank.model.transaction.DepositTransaction;
import com.lordbucket.bucketbank.model.transaction.TransferTransaction;
import com.lordbucket.bucketbank.model.transaction.WithdrawalTransaction;
import com.lordbucket.bucketbank.model.transaction.Transaction;
import com.lordbucket.bucketbank.model.transaction.RefundTransaction;
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
public class TransactionServiceTests {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;
    private Account depositAccount;
    private Account withdrawalAccount;
    private Account transferSender;
    private Account transferReceiver;

    @BeforeEach
    public void setup() {
        // Create two users for transaction tests
        user1 = new User();
        user1.setUsername("txnUser1");
        user1.setPinHash("hash1");
        user1 = userRepository.save(user1);

        user2 = new User();
        user2.setUsername("txnUser2");
        user2.setPinHash("hash2");
        user2 = userRepository.save(user2);

        // Create separate accounts for deposit and withdrawal tests
        depositAccount = accountService.createAccount(user1.getId());
        depositAccount.setAuthorizedUsers(new HashSet<>());
        depositAccount.setBalance(BigDecimal.valueOf(1000));
        depositAccount = accountRepository.save(depositAccount);

        withdrawalAccount = accountService.createAccount(user1.getId());
        withdrawalAccount.setAuthorizedUsers(new HashSet<>());
        withdrawalAccount.setBalance(BigDecimal.valueOf(1000));
        withdrawalAccount = accountRepository.save(withdrawalAccount);

        // Create accounts for transfer test
        transferSender = accountService.createAccount(user1.getId());
        transferSender.setAuthorizedUsers(new HashSet<>());
        transferSender.setBalance(BigDecimal.valueOf(1000));
        transferSender = accountRepository.save(transferSender);

        transferReceiver = accountService.createAccount(user2.getId());
        transferReceiver.setAuthorizedUsers(new HashSet<>());
        transferReceiver.setBalance(BigDecimal.valueOf(500));
        transferReceiver = accountRepository.save(transferReceiver);
    }

    @Test
    public void testRefundDeposit() throws Exception {
        BigDecimal depositAmount = BigDecimal.valueOf(200);
        Account updatedAcc = accountService.deposit(depositAccount.getId(), depositAmount).getAccount();
        // Retrieve the latest deposit transaction.
        DepositTransaction depositTxn = transactionRepository.findAll()
                .stream()
                .filter(tx -> tx instanceof DepositTransaction)
                .map(tx -> (DepositTransaction) tx)
                .filter(tx -> tx.getAccount().getId() == depositAccount.getId() && tx.getAmount().compareTo(depositAmount) == 0)
                .findFirst()
                .orElseThrow(() -> new TransactionNotFoundException("Deposit txn not found"));

        BigDecimal balanceAfterDeposit = updatedAcc.getBalance();

        transactionService.refundTransaction(depositTxn.getId(), "Refund deposit test");
        Account postRefund = accountRepository.findById(depositAccount.getId()).orElseThrow();
        // Refund for deposit subtracts the deposit amount from balance.
        assertEquals(balanceAfterDeposit.subtract(depositAmount), postRefund.getBalance());

        // Verify that the deposit transaction is marked refunded.
        Transaction original = transactionRepository.findById(depositTxn.getId()).orElseThrow();
        assertTrue(original.isRefunded());
    }

    @Test
    public void testRefundWithdrawal() throws Exception {
        BigDecimal withdrawAmount = BigDecimal.valueOf(300);
        Account updatedAcc = accountService.withdraw(withdrawalAccount.getId(), withdrawAmount).getAccount();
        WithdrawalTransaction withdrawalTxn = transactionRepository.findAll()
                .stream()
                .filter(tx -> tx instanceof WithdrawalTransaction)
                .map(tx -> (WithdrawalTransaction) tx)
                .filter(tx -> tx.getAccount().getId() == withdrawalAccount.getId() && tx.getAmount().compareTo(withdrawAmount) == 0)
                .findFirst()
                .orElseThrow(() -> new TransactionNotFoundException("Withdrawal txn not found"));

        BigDecimal balanceAfterWithdraw = updatedAcc.getBalance();

        transactionService.refundTransaction(withdrawalTxn.getId(), "Refund withdrawal test");
        Account postRefund = accountRepository.findById(withdrawalAccount.getId()).orElseThrow();
        // Refund for withdrawal adds back the withdrawn amount.
        assertEquals(balanceAfterWithdraw.add(withdrawAmount), postRefund.getBalance());

        Transaction original = transactionRepository.findById(withdrawalTxn.getId()).orElseThrow();
        assertTrue(original.isRefunded());
    }

    @Test
    public void testRefundTransfer() throws Exception {
        BigDecimal transferAmount = BigDecimal.valueOf(150);
        // Perform a transfer from transferSender to transferReceiver.
        transactionService.transfer(transferSender.getId(), transferReceiver.getId(), transferAmount, "Test transfer");
        TransferTransaction transferTxn = transactionRepository.findAll()
                .stream()
                .filter(tx -> tx instanceof TransferTransaction)
                .map(tx -> (TransferTransaction) tx)
                .filter(tx -> tx.getSender().getId() == transferSender.getId() &&
                        tx.getReceiver().getId() == transferReceiver.getId() &&
                        tx.getAmount().compareTo(transferAmount) == 0)
                .findFirst()
                .orElseThrow(() -> new TransactionNotFoundException("Transfer txn not found"));

        BigDecimal senderBalanceBeforeRefund = transferSender.getBalance();
        BigDecimal receiverBalanceBeforeRefund = transferReceiver.getBalance();

        transactionService.refundTransaction(transferTxn.getId(), "Refund transfer test");

        Account senderAfterRefund = accountRepository.findById(transferSender.getId()).orElseThrow();
        Account receiverAfterRefund = accountRepository.findById(transferReceiver.getId()).orElseThrow();

        // For a transfer refund:
        // Sender receives refund (balance increases), receiver is debited.
        assertEquals(senderBalanceBeforeRefund.add(transferAmount), senderAfterRefund.getBalance());
        assertEquals(receiverBalanceBeforeRefund.subtract(transferAmount), receiverAfterRefund.getBalance());

        Transaction original = transactionRepository.findById(transferTxn.getId()).orElseThrow();
        assertTrue(original.isRefunded());
    }
}

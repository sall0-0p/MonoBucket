package com.lordbucket.bucketbank.service;

import com.lordbucket.bucketbank.model.Account;
import com.lordbucket.bucketbank.model.transaction.*;
import com.lordbucket.bucketbank.repository.AccountRepository;
import com.lordbucket.bucketbank.repository.TransactionRepository;
import com.lordbucket.bucketbank.util.exceptions.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void refundTransaction(int transactionId, String reason)
            throws TransactionNotFoundException, TransactionAlreadyRefundedException, AccountSuspendedException {
        Transaction transaction = findTransactionById(transactionId);

        if (transaction.isRefunded()) {
            throw new TransactionAlreadyRefundedException();
        }

        BigDecimal amount = transaction.getAmount();
        switch (transaction) {
            case DepositTransaction depositTransaction -> refundDeposit(reason, transaction, amount);
            case WithdrawalTransaction withdrawalTransaction -> refundWithdrawal(reason, transaction, amount);
            case TransferTransaction transferTransaction -> refundTransfer(reason, transaction, amount);
            default -> throw new InvalidOperationException("This type of Transaction is not refundable");
        }
    }

    private void refundTransfer(String reason, Transaction transaction, BigDecimal amount) {
        Account sender = ((TransferTransaction) transaction).getSender();
        Account receiver = ((TransferTransaction) transaction).getReceiver();

        if (sender.isSuspended()) {
            throw new AccountSuspendedException("Sender account is suspended.");
        }

        if (receiver.isSuspended()) {
            throw new AccountSuspendedException("Receiver account is suspended.");
        }

        if (receiver.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Receiver of a transaction has not enough money to refund it.");
        }

        RefundTransaction refund = new RefundTransaction();
        refund.setAmount(amount);
        refund.setOriginalTransaction(transaction);
        refund.setRefundReason(reason);

        transaction.setRefunded(true);

        sender.setBalance(sender.getBalance().add(amount));
        receiver.setBalance(receiver.getBalance().subtract(amount));

        transactionRepository.save(refund);
        transactionRepository.save(transaction);
        accountRepository.save(sender);
        accountRepository.save(receiver);
    }

    private void refundWithdrawal(String reason, Transaction transaction, BigDecimal amount) {
        Account account = ((WithdrawalTransaction) transaction).getAccount();

        if (account.isSuspended()) {
            throw new AccountSuspendedException();
        }

        RefundTransaction refund = new RefundTransaction();
        refund.setAmount(amount);
        refund.setOriginalTransaction(transaction);
        refund.setRefundReason(reason);

        transaction.setRefunded(true);

        account.setBalance(account.getBalance().add(amount));
        transactionRepository.save(refund);
        transactionRepository.save(transaction);
        accountRepository.save(account);
    }

    private void refundDeposit(String reason, Transaction transaction, BigDecimal amount) {
        Account account = ((DepositTransaction) transaction).getAccount();

        if (account.isSuspended()) {
            throw new AccountSuspendedException();
        }

        RefundTransaction refund = new RefundTransaction();
        refund.setAmount(amount);
        refund.setOriginalTransaction(transaction);
        refund.setRefundReason(reason);

        transaction.setRefunded(true);

        account.setBalance(account.getBalance().subtract(amount));
        transactionRepository.save(refund);
        transactionRepository.save(transaction);
        accountRepository.save(account);
    }

    private Transaction findTransactionById(int transactionId) throws TransactionNotFoundException {
        return transactionRepository.findById(transactionId)
                .orElseThrow(TransactionNotFoundException::new);
    }
}

package com.lordbucket.bucketbank.service;

import com.lordbucket.bucketbank.model.Account;
import com.lordbucket.bucketbank.model.User;
import com.lordbucket.bucketbank.model.transaction.DepositTransaction;
import com.lordbucket.bucketbank.model.transaction.TransferTransaction;
import com.lordbucket.bucketbank.model.transaction.WithdrawalTransaction;
import com.lordbucket.bucketbank.repository.AccountRepository;
import com.lordbucket.bucketbank.repository.TransactionRepository;
import com.lordbucket.bucketbank.repository.UserRepository;
import com.lordbucket.bucketbank.util.CardNumberUtil;
import com.lordbucket.bucketbank.util.exceptions.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public AccountService(UserRepository userRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public Account createAccount(int userId) throws UserNotFoundException {
        User owner = getUserById(userId);

        Account account = new Account();
        account.setOwner(owner);

//        account = accountRepository.save(account);
//        account.setCardNumber(CardNumberUtil.generateCardNumber(account.getId()));

        return accountRepository.save(account);
    }

    @Transactional
    public DepositTransaction deposit(int accountId, BigDecimal amount)
            throws InvalidAmountException, AccountSuspendedException {
        Account account = getAccountById(accountId);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Deposited amount has to be positive.");
        }

        if (amount.scale() > 2) {
            throw new InvalidAmountException("The precision of amounts in operations is limited by 1 cent.");
        }

        if (account.isSuspended()) {
            throw new AccountSuspendedException();
        }

        account.setBalance(account.getBalance().add(amount));
        // TODO: Add a Transaction log.
        DepositTransaction transaction = new DepositTransaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);

        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    @Transactional
    public WithdrawalTransaction withdraw(int accountId, BigDecimal amount) throws InvalidAmountException {
        Account account = getAccountById(accountId);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidAmountException("Withdrawn amount has to be positive.");
        }

        if (amount.scale() > 2) {
            throw new InvalidAmountException("The precision of amounts in operations is limited by 1 cent.");
        }

        account.setBalance(account.getBalance().subtract(amount));
        // TODO: Add a Transaction log.
        WithdrawalTransaction transaction = new WithdrawalTransaction();
        transaction.setAccount(account);
        transaction.setAmount(amount);

        accountRepository.save(account);
        return transactionRepository.save(transaction);
    }

    public Account suspend(int accountId) throws AccountNotFoundException {
        Account account = getAccountById(accountId);

        account.setSuspended(true);

        return accountRepository.save(account);
    }

    public Account reinstate(int accountId) throws AccountNotFoundException {
        Account account = getAccountById(accountId);
        account.setSuspended(false);

        return accountRepository.save(account);
    }

    public Account rename(int accountId, String newDisplayName)
            throws AccountNotFoundException, AccountSuspendedException {
        Account account = getAccountById(accountId);

        if (account.isSuspended()) {
            throw new AccountSuspendedException();
        }

        account.setDisplayName(newDisplayName);

        return accountRepository.save(account);
    }

    public boolean isUserAuthorised(int userId, int accountId)
            throws AccountNotFoundException, UserNotFoundException {
        User user = getUserById(userId);
        Account account = getAccountById(accountId);

        return user.equals(account.getOwner())
                || account.getAuthorizedUsers().contains(user);
    }

    @Transactional
    public Account giveUserAccessToAccount(int userId, int accountId)
            throws AccountNotFoundException, UserNotFoundException, AccountSuspendedException {
        User user = getUserById(userId);
        Account account = getAccountById(accountId);

        if (account.isSuspended()) {
            throw new AccountSuspendedException();
        }

        account.getAuthorizedUsers().add(user);
        // TODO: Log access given

        return accountRepository.save(account);
    }

    @Transactional
    public Account removeUserAccessToAccount(int userId, int accountId)
            throws AccountNotFoundException, UserNotFoundException, AccountSuspendedException {
        User user = getUserById(userId);
        Account account = getAccountById(accountId);

        if (account.isSuspended()) {
            throw new AccountSuspendedException();
        }

        account.getAuthorizedUsers().remove(user);
        // TODO: Log access removed

        return accountRepository.save(account);
    }

    @Transactional
    public Account transferOwnershipOfAccount(int accountId, int newOwnerId)
            throws UserNotFoundException, AccountNotFoundException, AccountSuspendedException {
        Account account = getAccountById(accountId);
        User newOwner = getUserById(newOwnerId);

        if (account.isSuspended()) {
            throw new AccountSuspendedException();
        }

        account.setOwner(newOwner);
        if (account.getAuthorizedUsers() != null) {
            account.getAuthorizedUsers().remove(newOwner);
        }
        // TODO: Log ownership transfer

        return accountRepository.save(account);
    }

    public Account changeMerchantStatus(int accountId, boolean merchantStatus)
            throws AccountNotFoundException, AccountSuspendedException {
        Account account = getAccountById(accountId);

        if (account.isSuspended()) {
            throw new AccountSuspendedException();
        }

        account.setMerchant(merchantStatus);

        return accountRepository.save(account);
    }

    public Account getAccountById(int accountId) throws AccountNotFoundException {
        return accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);
    }

    public Account getAccountByCardNumber(String cardNumber) throws AccountNotFoundException {
        return accountRepository.findByCardNumber(cardNumber)
                .orElseThrow(AccountNotFoundException::new);
    }

    private User getUserById(int userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }
}
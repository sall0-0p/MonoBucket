package com.lordbucket.bucketbank.service;

import com.lordbucket.bucketbank.model.Account;
import com.lordbucket.bucketbank.model.User;
import com.lordbucket.bucketbank.repository.AccountRepository;
import com.lordbucket.bucketbank.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Autowired
    public AccountService(UserRepository userRepository, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountRepository = accountRepository;
    }

    public Account createAccount(int userId) throws IllegalArgumentException {
        User owner = getUserById(userId);

        Account account = new Account();
        account.setOwner(owner);

        return accountRepository.save(account);
    }

    @Transactional
    public Account deposit(int accountId, BigDecimal amount) throws IllegalArgumentException {
        Account account = getAccountById(accountId);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposited amount has to be positive.");
        }

        account.setBalance(account.getBalance().add(amount));
        // TODO: Add a Transaction log.

        return accountRepository.save(account);
    }

    @Transactional
    public Account withdraw(int accountId, BigDecimal amount) throws IllegalArgumentException {
        Account account = getAccountById(accountId);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawn amount has to be positive.");
        }

        account.setBalance(account.getBalance().subtract(amount));
        // TODO: Add a Transaction log.

        return accountRepository.save(account);
    }

    @Transactional
    public void transfer(int senderAccountId, int receiverAccountId, BigDecimal amount, String note) throws IllegalArgumentException {
        Account sender = getAccountById(senderAccountId);
        Account receiver = getAccountById(receiverAccountId);

        if (sender.getBalance().subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }

        sender.setBalance(sender.getBalance().subtract(amount));
        receiver.setBalance(receiver.getBalance().add(amount));
        // TODO: Add a Transaction Log.

        accountRepository.save(sender);
        accountRepository.save(receiver);
    }

    public Account suspend(int accountId) throws IllegalArgumentException {
        Account account = getAccountById(accountId);

        account.setSuspended(true);

        return accountRepository.save(account);
    }

    public Account reinstate(int accountId) throws IllegalArgumentException {
        Account account = getAccountById(accountId);
        account.setSuspended(false);

        return accountRepository.save(account);
    }

    public Account rename(int accountId, String newDisplayName) throws IllegalArgumentException {
        Account account = getAccountById(accountId);
        account.setDisplayName(newDisplayName);

        return accountRepository.save(account);
    }

    public boolean isUserAuthorised(int userId, int accountId) throws IllegalArgumentException {
        User user = getUserById(userId);
        Account account = getAccountById(accountId);

        return user.equals(account.getOwner())
                || account.getAuthorizedUsers().contains(user);
    }

    @Transactional
    public Account giveUserAccessToAccount(int userId, int accountId) throws IllegalArgumentException {
        User user = getUserById(userId);
        Account account = getAccountById(accountId);

        account.getAuthorizedUsers().add(user);

        // TODO: Log access given

        return accountRepository.save(account);
    }

    @Transactional
    public Account removeUserAccessToAccount(int userId, int accountId) throws IllegalArgumentException {
        User user = getUserById(userId);
        Account account = getAccountById(accountId);

        account.getAuthorizedUsers().remove(user);
        // TODO: Log access removed

        return accountRepository.save(account);
    }

    @Transactional
    public Account transferOwnershipOfAccount(int currentOwnerId, int accountId, int newOwnerId) {
        Account account = getAccountById(accountId);
        User currentOwner = getUserById(currentOwnerId);
        User newOwner = getUserById(newOwnerId);

        account.setOwner(newOwner);
        if (account.getAuthorizedUsers() != null) {
            account.getAuthorizedUsers().remove(newOwner);
        }
        // TODO: Log ownership transfer

        return accountRepository.save(account);
    }

    public Account getAccountById(int accountId) throws IllegalArgumentException {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("No account found"));
    }

    private User getUserById(int userId) throws IllegalArgumentException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("No user found."));
    }
}
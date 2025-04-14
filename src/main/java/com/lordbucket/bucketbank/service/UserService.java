package com.lordbucket.bucketbank.service;

import com.lordbucket.bucketbank.model.Account;
import com.lordbucket.bucketbank.model.User;
import com.lordbucket.bucketbank.repository.AccountRepository;
import com.lordbucket.bucketbank.repository.UserRepository;
import com.lordbucket.bucketbank.util.HashUtil;
import com.lordbucket.bucketbank.util.PINUtil;
import com.lordbucket.bucketbank.util.exceptions.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final AccountService accountService;
    private final AccountRepository accountRepository;

    @Autowired
    public UserService(UserRepository userRepository, AccountService accountService, AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.accountService = accountService;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public User createUser(String username, String rawPIN) throws RuntimeException {
        User user = new User();
        user.setUsername(username);
        user.setPinHash(HashUtil.generateHash(rawPIN));

        User savedUser = userRepository.save(user);
        Account primaryAccount = accountService.createAccount(savedUser.getId());
        primaryAccount.setDisplayName(username + "`s primary account");
        savedUser.setPrimaryAccount(primaryAccount);

        return userRepository.save(savedUser);
    }

    public User getUserByUsername(String username) throws UserNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(UserNotFoundException::new);
    }

    public boolean authenticate(int userId, String rawPIN)
            throws HashingException, UserNotFoundException {
        User user = getUserById(userId);
        return HashUtil.validatePassword(rawPIN, user.getPinHash());
    }

    @Transactional
    public User changePIN(int userId, String rawPIN, String newRawPIN)
            throws FailedAuthenticationException, UserNotFoundException, HashingException, UserSuspendedException {
        User user = getUserById(userId);

        if (user.isSuspended()) {
            throw new UserSuspendedException();
        }

        if (!HashUtil.validatePassword(rawPIN, user.getPinHash())) {
            throw new FailedAuthenticationException();
        }

        user.setPinHash(HashUtil.generateHash(newRawPIN));
        return userRepository.save(user);
    }

    @Transactional
    public String resetPIN(int userId)
            throws UserNotFoundException, HashingException, UserSuspendedException {
        User user = getUserById(userId);

        if (user.isSuspended()) {
            throw new UserSuspendedException();
        }

        String newPIN = PINUtil.generatePIN();
        user.setPinHash(HashUtil.generateHash(newPIN));
        userRepository.save(user);

        return newPIN;
    }

    @Transactional
    public User changeUsername(int userId, String newUsername) throws UserSuspendedException {
        User user = getUserById(userId);

        if (user.isSuspended()) {
            throw new UserSuspendedException();
        }

        user.setUsername(newUsername);

        return userRepository.save(user);
    }

    @Transactional
    public User changePrimaryAccount(int userId, int newPrimaryAccountId)
            throws UserSuspendedException, AccountSuspendedException, UserNotFoundException, AccountNotFoundException, AccountOwnershipException {
        User user = getUserById(userId);
        Account account = getAccountById(newPrimaryAccountId);

        if (user.isSuspended()) {
            throw new UserSuspendedException();
        }

        if (account.isSuspended()) {
            throw new AccountSuspendedException();
        }

        if (!account.getOwner().equals(user)) {
            throw new AccountOwnershipException();
        }

        user.setPrimaryAccount(account);

        return userRepository.save(user);
    }
    
    public User suspend(int userId) throws UserNotFoundException {
        User user = getUserById(userId);

        user.setSuspended(true);

        return userRepository.save(user);
    }

    public User reinstate(int userId) throws UserNotFoundException {
        User user = getUserById(userId);

        user.setSuspended(false);

        return userRepository.save(user);
    }

    public Account getAccountById(int accountId) throws AccountNotFoundException {
        return accountRepository.findById(accountId)
                .orElseThrow(AccountNotFoundException::new);
    }

    public User getUserById(int userId) throws UserNotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }
}

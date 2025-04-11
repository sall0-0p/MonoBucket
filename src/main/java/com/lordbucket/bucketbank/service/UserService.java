package com.lordbucket.bucketbank.service;

import com.lordbucket.bucketbank.model.Account;
import com.lordbucket.bucketbank.model.User;
import com.lordbucket.bucketbank.repository.AccountRepository;
import com.lordbucket.bucketbank.repository.UserRepository;
import com.lordbucket.bucketbank.util.HashUtil;
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


}

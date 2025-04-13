package com.lordbucket.bucketbank.service;

import com.lordbucket.bucketbank.model.Account;
import com.lordbucket.bucketbank.model.User;
import com.lordbucket.bucketbank.repository.AccountRepository;
import com.lordbucket.bucketbank.repository.UserRepository;
import com.lordbucket.bucketbank.util.HashUtil;
import com.lordbucket.bucketbank.util.PINUtil;
import com.lordbucket.bucketbank.util.exceptions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceTests {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService; // needed for primary account creation

    private User user1;
    private User user2;

    @BeforeEach
    public void setup() {
        // Create two initial users if needed.
        user1 = new User();
        user1.setUsername("userService1");
        user1.setPinHash(HashUtil.generateHash("1234"));
        user1 = userRepository.save(user1);

        user2 = new User();
        user2.setUsername("userService2");
        user2.setPinHash(HashUtil.generateHash("5678"));
        user2 = userRepository.save(user2);
    }

    @Test
    public void testCreateUser() throws Exception {
        String username = "newUser";
        String rawPIN = "9876";
        User user = userService.createUser(username, rawPIN);
        assertNotNull(user);
        assertEquals(username, user.getUsername());
        assertNotNull(user.getPrimaryAccount());
    }

    @Test
    public void testFindUserById() throws Exception {
        User found = userService.findUserById(user1.getId());
        assertEquals(user1.getUsername(), found.getUsername());
    }

    @Test
    public void testFindUserByUsername() throws Exception {
        User found = userService.findUserByUsername(user1.getUsername());
        assertEquals(user1.getId(), found.getId());
    }

    @Test
    public void testAuthenticate() throws Exception {
        // Correct PIN
        assertTrue(userService.authenticate(user1.getId(), "1234"));
        // Incorrect PIN
        assertFalse(userService.authenticate(user1.getId(), "wrong"));
    }

    @Test
    public void testChangePIN() throws Exception {
        // Change PIN with valid old PIN
        User updated = userService.changePIN(user1.getId(), "1234", "4321");
        assertTrue(userService.authenticate(user1.getId(), "4321"));

        // Change PIN with incorrect old PIN
        Exception ex = assertThrows(FailedAuthenticationException.class, () ->
                userService.changePIN(user1.getId(), "wrongOld", "0000"));
        assertNotNull(ex);
    }

    @Test
    public void testResetPIN() throws Exception {
        String newPIN = userService.resetPIN(user1.getId());
        assertNotNull(newPIN);
        // Now, newPIN is the raw pin, so authentication should succeed with it.
        assertTrue(userService.authenticate(user1.getId(), newPIN));
    }

    @Test
    public void testChangeUsername() throws Exception {
        String newUsername = "updatedUser";
        User updated = userService.changeUsername(user1.getId(), newUsername);
        assertEquals(newUsername, updated.getUsername());
    }

    @Test
    public void testChangePrimaryAccount() throws Exception {
        // Create an additional account for user1
        Account secondaryAccount = accountService.createAccount(user1.getId());
        secondaryAccount.setDisplayName("Secondary Account");
        secondaryAccount = accountRepository.save(secondaryAccount);

        User updated = userService.changePrimaryAccount(user1.getId(), secondaryAccount.getId());
        assertEquals(secondaryAccount.getId(), updated.getPrimaryAccount().getId());
    }

    @Test
    public void testSuspendAndReinstateUser() throws Exception {
        User suspended = userService.suspend(user1.getId());
        assertTrue(suspended.isSuspended());

        User reinstated = userService.reinstate(user1.getId());
        assertFalse(reinstated.isSuspended());
    }
}

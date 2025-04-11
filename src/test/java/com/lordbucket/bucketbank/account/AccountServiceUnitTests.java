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
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AccountServiceUnitTests {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    private User user1;
    private User user2;
    private Account account1;
    private Account account2;

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

        // Create two accounts
        account1 = new Account();
        account1.setOwner(user1);
        // Ensure authorizedUsers is initialized
        account1.setAuthorizedUsers(new HashSet<>());
        account1.setBalance(BigDecimal.valueOf(1000));
        account1 = accountRepository.save(account1);

        account2 = new Account();
        account2.setOwner(user2);
        account2.setAuthorizedUsers(new HashSet<>());
        account2.setBalance(BigDecimal.valueOf(500));
        account2 = accountRepository.save(account2);
    }

    @Test
    void testCreateAccount() {
        Account newAccount = accountService.createAccount(user1.getId());
        assertNotNull(newAccount);
        assertEquals(user1.getId(), newAccount.getOwner().getId());
    }

    @Test
    void testSuspendAndReinstate() {
        Account suspended = accountService.suspend(account1.getId());
        assertTrue(suspended.isSuspended());
        Account reinstated = accountService.reinstate(account1.getId());
        assertFalse(reinstated.isSuspended());
    }

    @Test
    void testRename() {
        String newDisplayName = "NewName";
        Account renamed = accountService.rename(account1.getId(), newDisplayName);
        assertEquals(newDisplayName, renamed.getDisplayName());
    }

    @Test
    void testIsUserAuthorised() {
        // Owner (user1) is authorized by default
        assertTrue(accountService.isUserAuthorised(user1.getId(), account1.getId()));
        // Initially user2 is not authorized for account1
        assertFalse(accountService.isUserAuthorised(user2.getId(), account1.getId()));

        // Give user2 access
        accountService.giveUserAccessToAccount(user2.getId(), account1.getId());
        assertTrue(accountService.isUserAuthorised(user2.getId(), account1.getId()));

        // Remove user2 access
        accountService.removeUserAccessToAccount(user2.getId(), account1.getId());
        assertFalse(accountService.isUserAuthorised(user2.getId(), account1.getId()));
    }

    @Test
    void testGiveAndRemoveUserAccess() {
        Account updated = accountService.giveUserAccessToAccount(user2.getId(), account1.getId());
        assertTrue(updated.getAuthorizedUsers().contains(user2));
        updated = accountService.removeUserAccessToAccount(user2.getId(), account1.getId());
        assertFalse(updated.getAuthorizedUsers().contains(user2));
    }

    @Test
    void testTransferOwnershipOfAccount() {
        // Give account1 to user2 from user1
        // First, ensure user2 is in the ACL, then transfer ownership should remove user2 from ACL.
        accountService.giveUserAccessToAccount(user2.getId(), account1.getId());
        Account updated = accountService.transferOwnershipOfAccount(user1.getId(), account1.getId(), user2.getId());
        assertEquals(user2.getId(), updated.getOwner().getId());
        assertFalse(updated.getAuthorizedUsers().contains(user2));
    }
}

package com.lordbucket.bucketbank.account;

import com.lordbucket.bucketbank.dto.AccountDTO;
import com.lordbucket.bucketbank.model.Account;
import com.lordbucket.bucketbank.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class AccountDTOUnitTests {
    private User owner;
    private User authUser;
    private Set<User> authorizedUsers;
    private Account account;

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    @BeforeEach
    public void setup() throws Exception {
        owner = new User();
        setField(owner, "id", 1);
        setField(owner, "username", "testuser");

        authUser = new User();
        setField(authUser, "id", 2);
        setField(authUser, "username", "otheruser");

        authorizedUsers = new HashSet<>();
        authorizedUsers.add(authUser);

        account = new Account();
        setField(account, "id", 123);
        setField(account, "cardNumber", "44123450");
        setField(account, "owner", owner);
        account.setAuthorizedUsers(authorizedUsers);
        account.setBalance(new BigDecimal("1000.00"));
        account.setSuspended(false);
        setField(account, "createdTimestamp", new Date(1000000L));
        setField(account, "updatedTimestamp", new Date(2000000L));
    }

    @Test
    public void testIdMapping() {
        AccountDTO dto = new AccountDTO(account);
        assertEquals(123, dto.getId());
    }

    @Test
    public void testCardNumberMapping() {
        AccountDTO dto = new AccountDTO(account);
        assertEquals("44123450", dto.getCardNumber());
    }

    @Test
    public void testBalanceMapping() {
        AccountDTO dto = new AccountDTO(account);
        assertEquals(new BigDecimal("1000.00"), dto.getBalance());
    }

    @Test
    public void testOwnerMapping() {
        AccountDTO dto = new AccountDTO(account);
        assertNotNull(dto.getOwner());
        assertEquals("testuser", dto.getOwner().getUsername());
    }

    @Test
    public void testAuthorizedUsersMapping() {
        AccountDTO dto = new AccountDTO(account);
        assertNotNull(dto.getAuthorizedUsers());
        assertEquals(1, dto.getAuthorizedUsers().size());
    }

    @Test
    public void testTimestampsMapping() {
        AccountDTO dto = new AccountDTO(account);
        assertNotNull(dto.getCreatedTimestamp());
        assertNotNull(dto.getUpdatedTimestamp());
    }
}

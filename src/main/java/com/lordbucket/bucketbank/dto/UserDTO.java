package com.lordbucket.bucketbank.dto;

import com.lordbucket.bucketbank.model.User;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDTO {
    private final int id;
    private final String username;
    private final boolean suspended;
    private final AccountSummaryDTO primaryAccount;
    private final List<AccountSummaryDTO> accountsOwned;
    private final Set<AccountSummaryDTO> accessibleAccounts;
    private final Date createdTimestamp;
    private final Date updatedTimestamp;

    public UserDTO(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.suspended = user.isSuspended();
        this.primaryAccount = new AccountSummaryDTO(user.getPrimaryAccount());
        this.accountsOwned = user.getAccountsOwned()
                .stream()
                .map(AccountSummaryDTO::new)
                .collect(Collectors.toList());
        this.accessibleAccounts = user.getAccessibleAccounts()
                .stream()
                .map(AccountSummaryDTO::new)
                .collect(Collectors.toSet());
        this.createdTimestamp = user.getCreatedTimestamp();
        this.updatedTimestamp = user.getUpdateTimestamp();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public AccountSummaryDTO getPrimaryAccount() { return primaryAccount; }

    public List<AccountSummaryDTO> getAccountsOwned() {
        return accountsOwned;
    }

    public Set<AccountSummaryDTO> getAccessibleAccounts() {
        return accessibleAccounts;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public Date getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDTO userDTO = (UserDTO) o;
        return id == userDTO.id && suspended == userDTO.suspended && Objects.equals(username, userDTO.username) && accountsOwned.equals(userDTO.accountsOwned) && accessibleAccounts.equals(userDTO.accessibleAccounts) && Objects.equals(createdTimestamp, userDTO.createdTimestamp) && Objects.equals(updatedTimestamp, userDTO.updatedTimestamp);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", suspended=" + suspended +
                ", accountsOwned=" + accountsOwned +
                ", accessibleAccounts=" + accessibleAccounts +
                ", createdTimestamp=" + createdTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
                '}';
    }
}

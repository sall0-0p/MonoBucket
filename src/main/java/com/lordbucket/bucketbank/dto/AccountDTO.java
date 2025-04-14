package com.lordbucket.bucketbank.dto;

import com.lordbucket.bucketbank.model.Account;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {
    private final int id;
    private final String cardNumber;
    private final UserSummaryDTO owner;
    private final String cvc;
    private final String displayName;
    private final boolean merchant;
    private final Set<UserSummaryDTO> authorizedUsers;
    private final BigDecimal balance;
    private final boolean suspended;
    private final Date createdTimestamp;
    private final Date updatedTimestamp;

    public AccountDTO(Account account) {
        this.id = account.getId();
        this.cardNumber = account.getCardNumber();
        this.cvc = account.getCvc();
        this.owner = new UserSummaryDTO(account.getOwner());
        this.displayName = account.getDisplayName();
        this.merchant = account.isMerchant();
        this.authorizedUsers = account.getAuthorizedUsers()
                .stream()
                .map(UserSummaryDTO::new)  // Assuming UserSummaryDTO(User user) constructor exists
                .collect(Collectors.toSet());
        this.balance = account.getBalance();
        this.suspended = account.isSuspended();
        this.createdTimestamp = account.getCreatedTimestamp();
        this.updatedTimestamp = account.getUpdatedTimestamp();
    }

    public int getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public UserSummaryDTO getOwner() {
        return owner;
    }

    public String getDisplayName() { return displayName; }

    public Set<UserSummaryDTO> getAuthorizedUsers() {
        return authorizedUsers;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public Date getCreatedTimestamp() {
        return createdTimestamp;
    }

    public Date getUpdatedTimestamp() {
        return updatedTimestamp;
    }

    public boolean isMerchant() {
        return merchant;
    }

    public String getCvc() {
        return cvc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountDTO that = (AccountDTO) o;
        return id == that.id && suspended == that.suspended && Objects.equals(cardNumber, that.cardNumber) && owner.equals(that.owner) && authorizedUsers.equals(that.authorizedUsers) && Objects.equals(balance, that.balance) && Objects.equals(createdTimestamp, that.createdTimestamp) && Objects.equals(updatedTimestamp, that.updatedTimestamp);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", owner=" + owner +
                ", authorizedUsers=" + authorizedUsers +
                ", balance=" + balance +
                ", suspended=" + suspended +
                ", createdTimestamp=" + createdTimestamp +
                ", updatedTimestamp=" + updatedTimestamp +
                '}';
    }
}

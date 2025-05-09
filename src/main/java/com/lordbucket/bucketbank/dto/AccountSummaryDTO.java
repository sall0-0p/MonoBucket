package com.lordbucket.bucketbank.dto;

import com.lordbucket.bucketbank.model.Account;

import java.math.BigDecimal;
import java.util.Objects;

public class AccountSummaryDTO {
    private final int id;
    private final String cardNumber;
    private final UserSummaryDTO owner;
    private final String displayName;
    private final boolean merchant;
    private final boolean suspended;

    public AccountSummaryDTO(Account account) {
        this.id = account.getId();
        this.cardNumber = account.getCardNumber();
        this.owner = new UserSummaryDTO(account.getOwner());
        this.displayName = account.getDisplayName();
        this.merchant = account.isMerchant();
        this.suspended = account.isSuspended();
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

    public String getDisplayName() {
        return displayName;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public boolean isMerchant() {
        return merchant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AccountSummaryDTO that = (AccountSummaryDTO) o;
        return id == that.id && suspended == that.suspended && Objects.equals(cardNumber, that.cardNumber) && owner.equals(that.owner);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "AccountSummaryDTO{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", owner=" + owner +
                ", suspended=" + suspended +
                '}';
    }
}

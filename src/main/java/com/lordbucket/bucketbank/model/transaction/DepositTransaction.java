package com.lordbucket.bucketbank.model.transaction;

import com.lordbucket.bucketbank.model.Account;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@DiscriminatorValue("DEPOSIT")
public class DepositTransaction extends Transaction {
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    public DepositTransaction() {
        super();
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}

package com.lordbucket.bucketbank.model.transaction;

import com.lordbucket.bucketbank.model.Account;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("POS")
public class PoSTransaction extends Transaction {
    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "merchant_account_id")
    private Account merchant;

    public PoSTransaction() {
        super();
    }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public Account getMerchant() {
        return merchant;
    }

    public void setMerchant(Account merchant) {
        this.merchant = merchant;
    }
}

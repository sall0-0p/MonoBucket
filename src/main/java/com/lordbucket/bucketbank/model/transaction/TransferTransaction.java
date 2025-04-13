package com.lordbucket.bucketbank.model.transaction;

import com.lordbucket.bucketbank.model.Account;
import jakarta.persistence.*;

@Entity
@DiscriminatorValue("TRANSFER")
public class TransferTransaction extends Transaction {
    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private Account sender;

    @ManyToOne
    @JoinColumn(name = "receiver_account_id")
    private Account receiver;

    @Column
    private String note;

    public TransferTransaction() {
        super();
    }

    public Account getSender() {
        return sender;
    }

    public void setSender(Account sender) {
        this.sender = sender;
    }

    public Account getReceiver() {
        return receiver;
    }

    public void setReceiver(Account receiver) {
        this.receiver = receiver;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}

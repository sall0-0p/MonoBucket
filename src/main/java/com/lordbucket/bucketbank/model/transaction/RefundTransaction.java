package com.lordbucket.bucketbank.model.transaction;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("REFUND")
public class RefundTransaction extends Transaction {
    @OneToOne(optional = false)
    @JoinColumn(name = "transaction_id")
    private Transaction originalTransaction;

    @Column
    private String refundReason;

    public Transaction getOriginalTransaction() {
        return originalTransaction;
    }

    public void setOriginalTransaction(Transaction originalTransaction) {
        this.originalTransaction = originalTransaction;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }
}

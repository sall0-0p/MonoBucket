package com.lordbucket.bucketbank.dto;

import com.lordbucket.bucketbank.model.transaction.*;
import java.math.BigDecimal;
import java.util.Date;

public final class TransactionSummaryDTO {
    private final int id;
    private final BigDecimal amount;
    private final boolean refunded;
    private final Date timestamp;
    private final String type;
    private final Integer accountId;         // For Deposit/Withdrawal
    private final Integer senderAccountId;   // For Transfer
    private final Integer receiverAccountId; // For Transfer
    private final String note;
    private final Integer originalTransactionId; // For Refund

    public TransactionSummaryDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.refunded = transaction.isRefunded();
        this.timestamp = new Date(transaction.getTimestamp().getTime());

        if (transaction instanceof DepositTransaction deposit) {
            this.type = "DEPOSIT";
            this.accountId = deposit.getAccount().getId();
            this.senderAccountId = null;
            this.receiverAccountId = null;
            this.note = null;
            this.originalTransactionId = null;
        } else if (transaction instanceof WithdrawalTransaction withdrawal) {
            this.type = "WITHDRAWAL";
            this.accountId = withdrawal.getAccount().getId();
            this.senderAccountId = null;
            this.receiverAccountId = null;
            this.note = null;
            this.originalTransactionId = null;
        } else if (transaction instanceof TransferTransaction transfer) {
            this.type = "TRANSFER";
            this.senderAccountId = transfer.getSender().getId();
            this.receiverAccountId = transfer.getReceiver().getId();
            this.accountId = null;
            this.note = transfer.getNote();
            this.originalTransactionId = null;
        } else if (transaction instanceof RefundTransaction refund) {
            this.type = "REFUND";
            Transaction original = refund.getOriginalTransaction();
            if (original instanceof DepositTransaction depOrig) {
                this.accountId = depOrig.getAccount().getId();
                this.senderAccountId = null;
                this.receiverAccountId = null;
            } else if (original instanceof WithdrawalTransaction witOrig) {
                this.accountId = witOrig.getAccount().getId();
                this.senderAccountId = null;
                this.receiverAccountId = null;
            } else if (original instanceof TransferTransaction transOrig) {
                this.senderAccountId = transOrig.getSender().getId();
                this.receiverAccountId = transOrig.getReceiver().getId();
                this.accountId = null;
            } else {
                this.accountId = null;
                this.senderAccountId = null;
                this.receiverAccountId = null;
            }
            this.note = refund.getRefundReason();
            this.originalTransactionId = refund.getOriginalTransaction().getId();
        } else {
            this.type = "UNKNOWN";
            this.accountId = null;
            this.senderAccountId = null;
            this.receiverAccountId = null;
            this.note = null;
            this.originalTransactionId = null;
        }
    }

    public int getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public boolean isRefunded() { return refunded; }
    public Date getTimestamp() { return new Date(timestamp.getTime()); }
    public String getType() { return type; }
    public Integer getAccountId() { return accountId; }
    public Integer getSenderAccountId() { return senderAccountId; }
    public Integer getReceiverAccountId() { return receiverAccountId; }
    public String getNote() { return note; }
    public Integer getOriginalTransactionId() { return originalTransactionId; }
}

package com.lordbucket.bucketbank.dto;

import com.lordbucket.bucketbank.model.transaction.*;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionDTO {
    private final int id;
    private final BigDecimal amount;
    private final boolean refunded;
    private final Date timestamp;
    private final String type;

    // For deposit/withdrawal refunds
    private final AccountSummaryDTO account;
    // For transfers
    private final AccountSummaryDTO sender;
    private final AccountSummaryDTO receiver;
    private final AccountSummaryDTO merchant;
    // Additional details
    private final String note;
    // For refund transactions, reference the original transaction
    private final TransactionSummaryDTO originalTransaction;

    public TransactionDTO(Transaction transaction) {
        this.id = transaction.getId();
        this.amount = transaction.getAmount();
        this.refunded = transaction.isRefunded();
        this.timestamp = new Date(transaction.getTimestamp().getTime());

        if (transaction instanceof DepositTransaction deposit) {
            this.type = "DEPOSIT";
            this.account = new AccountSummaryDTO(deposit.getAccount());
            this.sender = null;
            this.receiver = null;
            this.merchant = null;
            this.note = null;
            this.originalTransaction = null;
        } else if (transaction instanceof WithdrawalTransaction withdrawal) {
            this.type = "WITHDRAWAL";
            this.account = new AccountSummaryDTO(withdrawal.getAccount());
            this.sender = null;
            this.receiver = null;
            this.merchant = null;
            this.note = null;
            this.originalTransaction = null;
        } else if (transaction instanceof TransferTransaction transfer) {
            this.type = "TRANSFER";
            this.sender = new AccountSummaryDTO(transfer.getSender());
            this.receiver = new AccountSummaryDTO(transfer.getReceiver());
            this.account = null;
            this.merchant = null;
            this.note = transfer.getNote();
            this.originalTransaction = null;
        } else if (transaction instanceof PoSTransaction pos) {
            this.type = "POS";
            this.sender = new AccountSummaryDTO(pos.getSender());
            this.receiver = null;
            this.merchant = new AccountSummaryDTO(pos.getMerchant());
            this.account = null;
            this.note = null;
            this.originalTransaction = null;
        } else if (transaction instanceof NCPTransaction ncp) {
            this.type = "NCP";
            this.sender = new AccountSummaryDTO(ncp.getSender());
            this.receiver = null;
            this.merchant = new AccountSummaryDTO(ncp.getMerchant());
            this.account = null;
            this.note = null;
            this.originalTransaction = null;
        } else if (transaction instanceof RefundTransaction refund) {
            this.type = "REFUND";
            Transaction original = refund.getOriginalTransaction();
            if (original instanceof DepositTransaction depOrig) {
                this.account = new AccountSummaryDTO(depOrig.getAccount());
                this.merchant = null;
                this.sender = null;
                this.receiver = null;
            } else if (original instanceof WithdrawalTransaction witOrig) {
                this.account = new AccountSummaryDTO(witOrig.getAccount());
                this.merchant = null;
                this.sender = null;
                this.receiver = null;
            } else if (original instanceof TransferTransaction transOrig) {
                this.sender = new AccountSummaryDTO(transOrig.getSender());
                this.receiver = new AccountSummaryDTO(transOrig.getReceiver());
                this.merchant = null;
                this.account = null;
            } else {
                this.account = null;
                this.sender = null;
                this.merchant = null;
                this.receiver = null;
            }
            this.note = refund.getRefundReason();
            this.originalTransaction = new TransactionSummaryDTO(original);
        } else {
            this.type = "UNKNOWN";
            this.account = null;
            this.sender = null;
            this.receiver = null;
            this.merchant = null;
            this.note = null;
            this.originalTransaction = null;
        }
    }

    public int getId() { return id; }
    public BigDecimal getAmount() { return amount; }
    public boolean isRefunded() { return refunded; }
    public Date getTimestamp() { return new Date(timestamp.getTime()); }
    public String getType() { return type; }
    public AccountSummaryDTO getAccount() { return account; }
    public AccountSummaryDTO getSender() { return sender; }
    public AccountSummaryDTO getReceiver() { return receiver; }
    public String getNote() { return note; }
    public TransactionSummaryDTO getOriginalTransaction() { return originalTransaction; }
}

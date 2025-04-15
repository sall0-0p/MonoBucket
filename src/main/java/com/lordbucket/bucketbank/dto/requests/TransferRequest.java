package com.lordbucket.bucketbank.dto.requests;

import java.math.BigDecimal;

public class TransferRequest {
    private int senderAccountId;
    private int receiverAccountId;
    private BigDecimal amount;
    private String note = "";

    public TransferRequest() {
    }

    public TransferRequest(int senderAccountId, int receiverAccountId, BigDecimal amount, String note) {
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.amount = amount;
        this.note = note;
    }

    public int getReceiverAccountId() {
        return receiverAccountId;
    }

    public void setReceiverAccountId(int receiverAccountId) {
        this.receiverAccountId = receiverAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(int senderAccountId) {
        this.senderAccountId = senderAccountId;
    }
}

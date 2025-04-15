package com.lordbucket.bucketbank.dto.requests;

import java.math.BigDecimal;

public class NCPRequest {
    private int senderAccountId;
    private int merchantAccountId;
    private String cvc;
    private BigDecimal amount;

    public NCPRequest() {
    }

    public NCPRequest(int senderAccountId, int merchantAccountId, String cvc, BigDecimal amount) {
        this.senderAccountId = senderAccountId;
        this.merchantAccountId = merchantAccountId;
        this.amount = amount;
        this.cvc = cvc;
    }

    public int getSenderAccountId() {
        return senderAccountId;
    }

    public void setSenderAccountId(int senderAccountId) {
        this.senderAccountId = senderAccountId;
    }

    public int getMerchantAccountId() {
        return merchantAccountId;
    }

    public void setMerchantAccountId(int merchantAccountId) {
        this.merchantAccountId = merchantAccountId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCvc() {
        return cvc;
    }

    public void setCvc(String cvc) {
        this.cvc = cvc;
    }
}

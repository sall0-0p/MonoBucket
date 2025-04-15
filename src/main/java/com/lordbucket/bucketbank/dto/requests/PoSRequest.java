package com.lordbucket.bucketbank.dto.requests;

import java.math.BigDecimal;

public class PoSRequest {
    private int senderAccountId;
    private int merchantAccountId;
    private BigDecimal amount;

    public PoSRequest() {
    }

    public PoSRequest(int senderAccountId, int merchantAccountId, BigDecimal amount) {
        this.senderAccountId = senderAccountId;
        this.merchantAccountId = merchantAccountId;
        this.amount = amount;
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
}

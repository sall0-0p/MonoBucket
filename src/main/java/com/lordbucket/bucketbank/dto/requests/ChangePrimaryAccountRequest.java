package com.lordbucket.bucketbank.dto.requests;

public class ChangePrimaryAccountRequest {
    private int accountId;

    public ChangePrimaryAccountRequest() {
    }

    public ChangePrimaryAccountRequest(int accountId) {
        this.accountId = accountId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }
}

package com.lordbucket.bucketbank.dto.requests;

public class TransferOwnershipRequest {
    private int userId;

    public TransferOwnershipRequest() {
    }

    public TransferOwnershipRequest(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

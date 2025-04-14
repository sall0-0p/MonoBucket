package com.lordbucket.bucketbank.dto.requests;

public class RemoveAccessRequest {
    private int userId;

    public RemoveAccessRequest() {}

    public RemoveAccessRequest(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

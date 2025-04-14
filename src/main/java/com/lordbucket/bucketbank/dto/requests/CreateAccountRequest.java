package com.lordbucket.bucketbank.dto.requests;

public class CreateAccountRequest {
    private int userId;

    public CreateAccountRequest() {
    }

    public CreateAccountRequest(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

package com.lordbucket.bucketbank.dto.requests;

public class GrantAccessRequest {
    private int userId;

    public GrantAccessRequest() {}

    public GrantAccessRequest(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

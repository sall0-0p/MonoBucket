package com.lordbucket.bucketbank.dto.requests;

public class CreateUserRequest {
    private String username;
    private String pin;

    public CreateUserRequest() {

    }

    public CreateUserRequest(String username, String pin) {
        this.username = username;
        this.pin = pin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}

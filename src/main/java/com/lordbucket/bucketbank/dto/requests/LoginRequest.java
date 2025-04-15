package com.lordbucket.bucketbank.dto.requests;

public class LoginRequest {
    private String username;
    private String pin;

    public LoginRequest() {
    }

    public LoginRequest(String username, String cardNumber, String pin) {
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

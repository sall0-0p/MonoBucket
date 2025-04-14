package com.lordbucket.bucketbank.dto.requests;

public class ChangePinRequest {
    private String oldPin;
    private String newPin;

    public ChangePinRequest() {
    }

    public ChangePinRequest(String oldPin, String newPin) {
        this.oldPin = oldPin;
        this.newPin = newPin;
    }

    public String getOldPin() {
        return oldPin;
    }

    public void setOldPin(String oldPin) {
        this.oldPin = oldPin;
    }

    public String getNewPin() {
        return newPin;
    }

    public void setNewPin(String newPin) {
        this.newPin = newPin;
    }
}

package com.lordbucket.bucketbank.dto.requests;

public class RenameAccountRequest {
    private String displayName;

    public RenameAccountRequest() {}

    public RenameAccountRequest(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}

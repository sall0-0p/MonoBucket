package com.lordbucket.bucketbank.dto.requests;

public class ChangeMerchantStatus {
    private boolean merchant;

    public ChangeMerchantStatus() {}

    public ChangeMerchantStatus(boolean merchant) {
        this.merchant = merchant;
    }

    public boolean isMerchant() {
        return merchant;
    }

    public void setMerchant(boolean merchant) {
        this.merchant = merchant;
    }
}

package com.lordbucket.bucketbank.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Merchant license missing for designated account")
public class MerchantLicenseMissingException extends RuntimeException {
    public MerchantLicenseMissingException() {
        super("Merchant license missing for designated account.");
    }

    public MerchantLicenseMissingException(String message) {
        super(message);
    }
}

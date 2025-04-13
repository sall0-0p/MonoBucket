package com.lordbucket.bucketbank.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "You are not authorised to perform this operation on this account.")
public class AccountOwnershipException extends RuntimeException {
    public AccountOwnershipException() {
        super("You are not authorised to perform this operation on this account.");
    }

    public AccountOwnershipException(String message) {
        super(message);
    }
}

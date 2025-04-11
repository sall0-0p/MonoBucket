package com.lordbucket.bucketbank.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Accounts accessed is suspended.")
public class AccountSuspendedException extends RuntimeException {
    public AccountSuspendedException() {
        super("Accounts accessed is suspended.");
    }

    public AccountSuspendedException(String message) {
        super(message);
    }
}

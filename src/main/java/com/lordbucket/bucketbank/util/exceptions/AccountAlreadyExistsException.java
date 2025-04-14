package com.lordbucket.bucketbank.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "Account already exists.")
public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException() {
        super("Account already exists.");
    }

    public AccountAlreadyExistsException(String message) {
        super(message);
    }
}

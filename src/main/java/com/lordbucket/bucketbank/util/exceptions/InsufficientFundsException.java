package com.lordbucket.bucketbank.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Account has insufficient funds to perform operation.")
public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException() {
        super("Account has insufficient funds to perform operation.");
    }

    public InsufficientFundsException(String message) {
        super(message);
    }
}

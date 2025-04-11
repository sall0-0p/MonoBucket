package com.lordbucket.bucketbank.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Invalid amount provided for operation.")
public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
        super("Invalid amount provided for operation.");
    }

    public InvalidAmountException(String message) {
        super(message);
    }
}

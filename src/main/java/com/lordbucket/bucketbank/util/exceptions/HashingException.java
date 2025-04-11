package com.lordbucket.bucketbank.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Something went wrong.")
public class HashingException extends RuntimeException {
    public HashingException() {
        super("Something went wrong.");
    }

    public HashingException(String message) {
        super(message);
    }
}

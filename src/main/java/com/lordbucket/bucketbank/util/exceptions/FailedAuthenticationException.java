package com.lordbucket.bucketbank.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNAUTHORIZED, reason = "Invalid credentials.")
public class FailedAuthenticationException extends RuntimeException {
    public FailedAuthenticationException() {
        super("Invalid credentials.");
    }

    public FailedAuthenticationException(String message) {
        super(message);
    }
 }

package com.lordbucket.bucketbank.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "User accessed is suspended.")
public class UserSuspendedException extends RuntimeException {
    public UserSuspendedException() {
        super("User accessed is suspended.");
    }

    public UserSuspendedException(String message) {
        super(message);
    }
}

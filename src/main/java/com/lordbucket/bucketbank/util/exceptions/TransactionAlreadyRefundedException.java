package com.lordbucket.bucketbank.util.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN, reason = "This transaction was already refunded.")
public class TransactionAlreadyRefundedException extends RuntimeException {
    public TransactionAlreadyRefundedException() {
        super("This transaction was already refunded.");
    }

    public TransactionAlreadyRefundedException(String message) {
        super(message);
    }
}

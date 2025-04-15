package com.lordbucket.bucketbank.util;

import com.lordbucket.bucketbank.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

// Global exception handler for all controller exceptions
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // This handler catches all exceptions; you can add specific ones too
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        // Log full stack trace to console
        logger.error("Unhandled exception occurred: + " + ex.getMessage());

        // Look for @ResponseStatus annotation on the exception class
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ResponseStatus responseStatus = ex.getClass().getAnnotation(ResponseStatus.class);
        if (responseStatus != null) {
            status = responseStatus.value();
        }

        // Return the error response with both status code and the exception message
        ErrorResponse errorResponse = new ErrorResponse(status.value(), ex.getMessage());
        return new ResponseEntity<>(errorResponse, status);
    }
}

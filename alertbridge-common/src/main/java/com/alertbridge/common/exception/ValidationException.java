package com.alertbridge.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when incoming request data fails business-level validation (maps to HTTP 400).
 */
public class ValidationException extends AlertBridgeException {

    public ValidationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public ValidationException(String field, String reason) {
        super(String.format("Validation failed for field '%s': %s", field, reason),
              HttpStatus.BAD_REQUEST);
    }
}


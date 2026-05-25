package com.alertbridge.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a requested resource cannot be found (maps to HTTP 404).
 */
public class ResourceNotFoundException extends AlertBridgeException {

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(
            String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue),
            HttpStatus.NOT_FOUND
        );
    }

    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}


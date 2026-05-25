package com.alertbridge.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when a requested resource cannot be found (maps to HTTP 404).
 */
public class ResourceNotFoundException extends AlertBridgeException {

  /**
   * Creates a 404 exception with a formatted message containing the resource details.
   *
   * @param resourceName the type of resource that was not found
   * @param fieldName    the lookup field name
   * @param fieldValue   the lookup field value
   */
  public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
    super(
        String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue),
        HttpStatus.NOT_FOUND
    );
  }

  /**
   * Creates a 404 exception with a custom message.
   *
   * @param message human-readable description of what was not found
   */
  public ResourceNotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}

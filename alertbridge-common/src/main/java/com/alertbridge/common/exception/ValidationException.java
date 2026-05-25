package com.alertbridge.common.exception;

import org.springframework.http.HttpStatus;

/**
 * Thrown when incoming request data fails business-level validation (maps to HTTP 400).
 */
public class ValidationException extends AlertBridgeException {

  /**
   * Creates a 400 exception with a custom message.
   *
   * @param message human-readable description of the validation failure
   */
  public ValidationException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }

  /**
   * Creates a 400 exception with a field-specific message.
   *
   * @param field  the field that failed validation
   * @param reason why the field is invalid
   */
  public ValidationException(String field, String reason) {
    super(
        String.format("Validation failed for field '%s': %s", field, reason),
        HttpStatus.BAD_REQUEST
    );
  }
}

package com.alertbridge.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base runtime exception for all AlertBridge-specific errors.
 * Carries an HTTP status so {@code GlobalExceptionHandler} can respond correctly.
 */
@Getter
public class AlertBridgeException extends RuntimeException {

  private final HttpStatus httpStatus;

  public AlertBridgeException(String message, HttpStatus httpStatus) {
    super(message);
    this.httpStatus = httpStatus;
  }

  public AlertBridgeException(String message, HttpStatus httpStatus, Throwable cause) {
    super(message, cause);
    this.httpStatus = httpStatus;
  }
}

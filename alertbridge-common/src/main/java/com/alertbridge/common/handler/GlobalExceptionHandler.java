package com.alertbridge.common.handler;

import com.alertbridge.common.dto.ErrorResponse;
import com.alertbridge.common.exception.AlertBridgeException;
import com.alertbridge.common.exception.ResourceNotFoundException;
import com.alertbridge.common.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Central exception handler shared by all AlertBridge REST services.
 *
 * <p>This handler is registered automatically via Spring Boot auto-configuration
 * ({@code AlertBridgeAutoConfiguration}) when {@code alertbridge-common} is on the
 * classpath of a servlet web application — no manual {@code @Import} or
 * {@code scanBasePackages} configuration is needed in the consuming service.
 *
 * <p>If a consuming service needs to customise exception handling it can declare its
 * own {@code GlobalExceptionHandler} bean; the {@code @ConditionalOnMissingBean}
 * guard in the auto-configuration will then skip this default registration.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  // ── AlertBridge domain exceptions ────────────────────────────────────────

  /** Handles {@link ResourceNotFoundException} → 404. */
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleResourceNotFound(
      ResourceNotFoundException ex, HttpServletRequest request) {
    log.warn("Resource not found: {}", ex.getMessage());
    return buildResponse(ex.getHttpStatus(), ex.getMessage(), null, request.getRequestURI());
  }

  /** Handles {@link ValidationException} → 400. */
  @ExceptionHandler(ValidationException.class)
  public ResponseEntity<ErrorResponse> handleValidation(
      ValidationException ex, HttpServletRequest request) {
    log.warn("Validation error: {}", ex.getMessage());
    return buildResponse(ex.getHttpStatus(), ex.getMessage(), null, request.getRequestURI());
  }

  /** Handles any other {@link AlertBridgeException}. */
  @ExceptionHandler(AlertBridgeException.class)
  public ResponseEntity<ErrorResponse> handleAlertBridge(
      AlertBridgeException ex, HttpServletRequest request) {
    log.error("AlertBridge error [{}]: {}", ex.getHttpStatus(), ex.getMessage());
    return buildResponse(ex.getHttpStatus(), ex.getMessage(), null, request.getRequestURI());
  }

  // ── Spring / Bean-Validation exceptions ──────────────────────────────────

  /** Handles {@code @Valid} / {@code @Validated} constraint failures → 400. */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex, HttpServletRequest request) {
    List<String> details = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(FieldError::getDefaultMessage)
        .collect(Collectors.toList());
    log.warn("Request validation failed: {}", details);
    return buildResponse(
        HttpStatus.BAD_REQUEST, "Request validation failed", details, request.getRequestURI());
  }

  /** Handles malformed / missing request body → 400. */
  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleNotReadable(
      HttpMessageNotReadableException ex, HttpServletRequest request) {
    log.warn("Malformed request body: {}", ex.getMessage());
    return buildResponse(HttpStatus.BAD_REQUEST,
        "Malformed or missing request body", null, request.getRequestURI());
  }

  // ── Catch-all ─────────────────────────────────────────────────────────────

  /** Handles any unhandled exception → 500. */
  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(
      Exception ex, HttpServletRequest request) {
    log.error("Unexpected error at [{}]: {}", request.getRequestURI(), ex.getMessage(), ex);
    return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
        "An unexpected error occurred", null, request.getRequestURI());
  }

  // ── Helper ────────────────────────────────────────────────────────────────

  private ResponseEntity<ErrorResponse> buildResponse(
      HttpStatus status, String message, List<String> details, String path) {
    ErrorResponse body = ErrorResponse.builder()
        .status(status.value())
        .error(status.getReasonPhrase())
        .message(message)
        .path(path)
        .details(details)
        .build();
    return ResponseEntity.status(status).body(body);
  }
}

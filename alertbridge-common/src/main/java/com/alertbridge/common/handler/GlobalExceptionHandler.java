package com.alertbridge.common.handler;

import com.alertbridge.common.dto.ErrorResponse;
import com.alertbridge.common.exception.AlertBridgeException;
import com.alertbridge.common.exception.ResourceNotFoundException;
import com.alertbridge.common.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Central exception handler shared by all AlertBridge REST services.
 * <p>
 * Include this module as a dependency and Spring Boot auto-component-scan will
 * pick up this advice automatically via {@code @RestControllerAdvice}.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ── AlertBridge domain exceptions ────────────────────────────────────────

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex,
                                                                HttpServletRequest request) {
        log.warn("Resource not found: {}", ex.getMessage());
        return buildResponse(ex.getHttpStatus(), ex.getMessage(), null, request.getRequestURI());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex,
                                                          HttpServletRequest request) {
        log.warn("Validation error: {}", ex.getMessage());
        return buildResponse(ex.getHttpStatus(), ex.getMessage(), null, request.getRequestURI());
    }

    @ExceptionHandler(AlertBridgeException.class)
    public ResponseEntity<ErrorResponse> handleAlertBridge(AlertBridgeException ex,
                                                           HttpServletRequest request) {
        log.error("AlertBridge error [{}]: {}", ex.getHttpStatus(), ex.getMessage());
        return buildResponse(ex.getHttpStatus(), ex.getMessage(), null, request.getRequestURI());
    }

    // ── Spring / Bean-Validation exceptions ──────────────────────────────────

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        List<String> details = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());

        log.warn("Request validation failed: {}", details);
        return buildResponse(HttpStatus.BAD_REQUEST,
                             "Request validation failed",
                             details,
                             request.getRequestURI());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleNotReadable(HttpMessageNotReadableException ex,
                                                           HttpServletRequest request) {
        log.warn("Malformed request body: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST,
                             "Malformed or missing request body",
                             null,
                             request.getRequestURI());
    }

    // ── Catch-all ─────────────────────────────────────────────────────────────

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex, HttpServletRequest request) {
        log.error("Unexpected error at [{}]: {}", request.getRequestURI(), ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR,
                             "An unexpected error occurred",
                             null,
                             request.getRequestURI());
    }

    // ── Helper ────────────────────────────────────────────────────────────────

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status,
                                                        String message,
                                                        List<String> details,
                                                        String path) {
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


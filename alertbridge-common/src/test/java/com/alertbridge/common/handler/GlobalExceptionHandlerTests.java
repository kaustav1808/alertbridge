package com.alertbridge.common.handler;

import com.alertbridge.common.dto.ErrorResponse;
import com.alertbridge.common.dto.NotificationRequest;
import com.alertbridge.common.exception.AlertBridgeException;
import com.alertbridge.common.exception.ResourceNotFoundException;
import com.alertbridge.common.exception.ValidationException;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTests {

  private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

  @Test
  @DisplayName("handleResourceNotFound should return 404")
  void handleResourceNotFoundReturnsNotFound() {
    ResourceNotFoundException ex = new ResourceNotFoundException("Notification", "id", "42");

    ResponseEntity<ErrorResponse> response =
        handler.handleResourceNotFound(ex, request("/api/v1/notifications/42"));

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    assertEquals("Notification not found with id: '42'", response.getBody().getMessage());
    assertEquals("/api/v1/notifications/42", response.getBody().getPath());
  }

  @Test
  @DisplayName("handleValidation should return 400")
  void handleValidationReturnsBadRequest() {
    ValidationException ex = new ValidationException("recipient", "must not be blank");

    ResponseEntity<ErrorResponse> response =
        handler.handleValidation(ex, request("/api/v1/notifications"));

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals(
        "Validation failed for field 'recipient': must not be blank",
        response.getBody().getMessage());
    assertEquals("/api/v1/notifications", response.getBody().getPath());
  }

  @Test
  @DisplayName("handleAlertBridge should preserve status and message")
  void handleAlertBridgePreservesStatusAndMessage() {
    AlertBridgeException ex =
        new AlertBridgeException("Service unavailable", HttpStatus.SERVICE_UNAVAILABLE);

    ResponseEntity<ErrorResponse> response =
        handler.handleAlertBridge(ex, request("/api/v1/notifications"));

    assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
    assertEquals("Service unavailable", response.getBody().getMessage());
  }

  @Test
  @DisplayName("handleMethodArgumentNotValid should return 400 with validation details")
  void handleMethodArgumentNotValidReturnsBadRequest() throws Exception {
    BeanPropertyBindingResult bindingResult =
        new BeanPropertyBindingResult(new NotificationRequest(), "notificationRequest");
    bindingResult.addError(
        new FieldError("notificationRequest", "recipient", "Recipient must not be blank"));
    bindingResult.addError(
        new FieldError("notificationRequest", "subject", "Subject is required"));
    Method method = TestController.class.getDeclaredMethod("send", NotificationRequest.class);
    MethodParameter methodParameter = new MethodParameter(method, 0);
    MethodArgumentNotValidException ex =
        new MethodArgumentNotValidException(methodParameter, bindingResult);

    ResponseEntity<ErrorResponse> response =
        handler.handleMethodArgumentNotValid(ex, request("/api/v1/notifications"));

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Request validation failed", response.getBody().getMessage());
    assertEquals("/api/v1/notifications", response.getBody().getPath());
    assertIterableEquals(
        List.of("Recipient must not be blank", "Subject is required"),
        response.getBody().getDetails());
  }

  @Test
  @DisplayName("handleNotReadable should return 400")
  void handleNotReadableReturnsBadRequest() {
    HttpMessageNotReadableException ex =
        new HttpMessageNotReadableException("JSON parse error");

    ResponseEntity<ErrorResponse> response =
        handler.handleNotReadable(ex, request("/api/v1/notifications"));

    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    assertEquals("Malformed or missing request body", response.getBody().getMessage());
    assertEquals("/api/v1/notifications", response.getBody().getPath());
  }

  @Test
  @DisplayName("handleGeneric should return 500 and safe message")
  void handleGenericReturnsInternalServerError() {
    RuntimeException ex = new RuntimeException("Boom");

    ResponseEntity<ErrorResponse> response =
        handler.handleGeneric(ex, request("/api/v1/notifications"));

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    assertEquals("An unexpected error occurred", response.getBody().getMessage());
    assertEquals("/api/v1/notifications", response.getBody().getPath());
  }

  private MockHttpServletRequest request(String path) {
    MockHttpServletRequest request = new MockHttpServletRequest();
    request.setRequestURI(path);
    return request;
  }

  private static class TestController {

    void send(NotificationRequest request) {
    }
  }
}

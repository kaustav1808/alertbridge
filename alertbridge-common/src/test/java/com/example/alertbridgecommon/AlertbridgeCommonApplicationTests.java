package com.alertbridge.common;

import com.alertbridge.common.constants.AlertBridgeConstants;
import com.alertbridge.common.dto.ApiResponse;
import com.alertbridge.common.dto.NotificationRequest;
import com.alertbridge.common.dto.NotificationResult;
import com.alertbridge.common.enums.NotificationChannel;
import com.alertbridge.common.enums.NotificationStatus;
import com.alertbridge.common.exception.AlertBridgeException;
import com.alertbridge.common.exception.ResourceNotFoundException;
import com.alertbridge.common.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("AlertBridge Common Library Tests")
class AlertbridgeCommonTests {

    // ── ApiResponse ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("ApiResponse.success() should return success status with data")
    void apiResponse_successWithData() {
        ApiResponse<String> response = ApiResponse.success("hello");
        assertEquals(AlertBridgeConstants.RESPONSE_SUCCESS, response.getStatus());
        assertEquals("hello", response.getData());
        assertNull(response.getMessage());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("ApiResponse.success() should accept message and data")
    void apiResponse_successWithMessageAndData() {
        ApiResponse<Integer> response = ApiResponse.success("Created", 42);
        assertEquals(AlertBridgeConstants.RESPONSE_SUCCESS, response.getStatus());
        assertEquals("Created", response.getMessage());
        assertEquals(42, response.getData());
    }

    @Test
    @DisplayName("ApiResponse.error() should return error status with message")
    void apiResponse_error() {
        ApiResponse<Void> response = ApiResponse.error("Something went wrong");
        assertEquals(AlertBridgeConstants.RESPONSE_ERROR, response.getStatus());
        assertEquals("Something went wrong", response.getMessage());
        assertNull(response.getData());
    }

    // ── Exceptions ───────────────────────────────────────────────────────────

    @Test
    @DisplayName("ResourceNotFoundException should produce 404 status")
    void resourceNotFoundException_404() {
        var ex = new ResourceNotFoundException("User", "id", 99L);
        assertEquals(HttpStatus.NOT_FOUND, ex.getHttpStatus());
        assertTrue(ex.getMessage().contains("User"));
        assertTrue(ex.getMessage().contains("99"));
    }

    @Test
    @DisplayName("ValidationException should produce 400 status")
    void validationException_400() {
        var ex = new ValidationException("email", "must be a valid email address");
        assertEquals(HttpStatus.BAD_REQUEST, ex.getHttpStatus());
        assertTrue(ex.getMessage().contains("email"));
    }

    @Test
    @DisplayName("AlertBridgeException should carry custom HTTP status")
    void alertBridgeException_customStatus() {
        var ex = new AlertBridgeException("Service unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, ex.getHttpStatus());
        assertEquals("Service unavailable", ex.getMessage());
    }

    // ── NotificationRequest ──────────────────────────────────────────────────

    @Test
    @DisplayName("NotificationRequest constructor should set all fields")
    void notificationRequest_constructor() {
        var req = new NotificationRequest(
                NotificationChannel.EMAIL,
                "user@example.com",
                "Welcome",
                "Hello, welcome to AlertBridge!"
        );
        assertEquals(NotificationChannel.EMAIL, req.getChannel());
        assertEquals("user@example.com", req.getRecipient());
        assertEquals("Welcome", req.getSubject());
        assertEquals("Hello, welcome to AlertBridge!", req.getBody());
    }

    // ── NotificationResult ───────────────────────────────────────────────────

    @Test
    @DisplayName("NotificationResult.sent() should produce SENT status")
    void notificationResult_sent() {
        var result = NotificationResult.sent("notif-001", NotificationChannel.SMS);
        assertEquals("notif-001", result.getNotificationId());
        assertEquals(NotificationChannel.SMS, result.getChannel());
        assertEquals(NotificationStatus.SENT, result.getStatus());
        assertNotNull(result.getProcessedAt());
    }

    @Test
    @DisplayName("NotificationResult.failed() should produce FAILED status with message")
    void notificationResult_failed() {
        var result = NotificationResult.failed("notif-002", NotificationChannel.PUSH, "FCM error");
        assertEquals(NotificationStatus.FAILED, result.getStatus());
        assertEquals("FCM error", result.getMessage());
    }

    // ── Constants ────────────────────────────────────────────────────────────

    @Test
    @DisplayName("AlertBridgeConstants should expose expected constant values")
    void constants_values() {
        assertEquals("X-Correlation-ID", AlertBridgeConstants.HEADER_CORRELATION_ID);
        assertEquals("Bearer ",          AlertBridgeConstants.BEARER_PREFIX);
        assertEquals(20,                 AlertBridgeConstants.DEFAULT_PAGE_SIZE);
        assertEquals(3,                  AlertBridgeConstants.MAX_RETRY_ATTEMPTS);
    }
}

package com.alertbridge.common.dto;

import com.alertbridge.common.constants.AlertBridgeConstants;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import lombok.Getter;

/**
 * Generic API response envelope used by all AlertBridge REST endpoints.
 *
 * @param <T> the type of the payload data
 */
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

  private final String status;
  private final String message;
  private final T data;
  private final Instant timestamp;

  private ApiResponse(String status, String message, T data) {
    this.status = status;
    this.message = message;
    this.data = data;
    this.timestamp = Instant.now();
  }

  // ── Factory methods ──────────────────────────────────────────────────────

  /** Creates a successful response with payload data. */
  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(AlertBridgeConstants.RESPONSE_SUCCESS, null, data);
  }

  /** Creates a successful response with a message and payload data. */
  public static <T> ApiResponse<T> success(String message, T data) {
    return new ApiResponse<>(AlertBridgeConstants.RESPONSE_SUCCESS, message, data);
  }


  /** Creates an error response with a message. */
  public static <T> ApiResponse<T> error(String message) {
    return new ApiResponse<>(AlertBridgeConstants.RESPONSE_ERROR, message, null);
  }

  /** Creates an error response with a message and additional error detail as data. */
  public static <T> ApiResponse<T> error(String message, T data) {
    return new ApiResponse<>(AlertBridgeConstants.RESPONSE_ERROR, message, data);
  }
}

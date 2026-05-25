package com.alertbridge.common.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

/**
 * Structured error response body returned by {@code GlobalExceptionHandler}.
 */
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

  private final int status;
  private final String error;
  private final String message;
  private final String path;
  @Builder.Default
  private final Instant timestamp = Instant.now();
  private final List<String> details;

}

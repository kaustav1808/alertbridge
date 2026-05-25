package com.alertbridge.common.dto;

import com.alertbridge.common.enums.NotificationChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request payload for dispatching a notification through any AlertBridge channel.
 * Used by the notification-service, scheduler-service, and analytics-service.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {

  @NotNull(message = "Notification channel must not be null")
  private NotificationChannel channel;

  @NotBlank(message = "Recipient must not be blank")
  private String recipient;

  @NotBlank(message = "Subject must not be blank")
  @Size(max = 255, message = "Subject must not exceed 255 characters")
  private String subject;

  @NotBlank(message = "Body must not be blank")
  private String body;

  /** Optional key/value metadata (e.g. template variables, device tokens). */
  private Map<String, String> metadata;

  /** Optional idempotency key supplied by the caller. */
  private String correlationId;
}

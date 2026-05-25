package com.alertbridge.common.dto;

import com.alertbridge.common.enums.NotificationChannel;
import com.alertbridge.common.enums.NotificationStatus;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

/**
 * Result returned after a notification dispatch attempt.
 */
@Getter
@Builder
public class NotificationResult {

  private final String notificationId;
  private final NotificationChannel channel;
  private final NotificationStatus status;
  private final String message;
  @Builder.Default
  private final Instant processedAt = Instant.now();

  // ── Convenience factory methods ──────────────────────────────────────────

  /** Creates a SENT result. */
  public static NotificationResult sent(String notificationId, NotificationChannel channel) {
    return NotificationResult.builder()
        .notificationId(notificationId)
        .channel(channel)
        .status(NotificationStatus.SENT)
        .build();
  }

  /** Creates a FAILED result with an error message. */
  public static NotificationResult failed(String notificationId,
      NotificationChannel channel, String errorMessage) {
    return NotificationResult.builder()
        .notificationId(notificationId)
        .channel(channel)
        .status(NotificationStatus.FAILED)
        .message(errorMessage)
        .build();
  }
}

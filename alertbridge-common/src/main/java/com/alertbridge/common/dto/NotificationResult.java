package com.alertbridge.common.dto;

import com.alertbridge.common.enums.NotificationChannel;
import com.alertbridge.common.enums.NotificationStatus;

import java.time.Instant;

/**
 * Result returned after a notification dispatch attempt.
 */
public class NotificationResult {

    private final String             notificationId;
    private final NotificationChannel channel;
    private final NotificationStatus status;
    private final String             message;
    private final Instant            processedAt;

    private NotificationResult(Builder builder) {
        this.notificationId = builder.notificationId;
        this.channel        = builder.channel;
        this.status         = builder.status;
        this.message        = builder.message;
        this.processedAt    = Instant.now();
    }

    // ── Convenience factory methods ──────────────────────────────────────────

    public static NotificationResult sent(String notificationId, NotificationChannel channel) {
        return new Builder()
                .notificationId(notificationId)
                .channel(channel)
                .status(NotificationStatus.SENT)
                .build();
    }

    public static NotificationResult failed(String notificationId,
                                            NotificationChannel channel,
                                            String errorMessage) {
        return new Builder()
                .notificationId(notificationId)
                .channel(channel)
                .status(NotificationStatus.FAILED)
                .message(errorMessage)
                .build();
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public String getNotificationId() {
        return notificationId;
    }

    public NotificationChannel getChannel() {
        return channel;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String              notificationId;
        private NotificationChannel channel;
        private NotificationStatus  status;
        private String              message;

        private Builder() {}

        public Builder notificationId(String notificationId) { this.notificationId = notificationId; return this; }
        public Builder channel(NotificationChannel channel)  { this.channel = channel;               return this; }
        public Builder status(NotificationStatus status)     { this.status = status;                 return this; }
        public Builder message(String message)               { this.message = message;               return this; }

        public NotificationResult build() {
            return new NotificationResult(this);
        }
    }
}


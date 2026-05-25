package com.alertbridge.common.dto;

import com.alertbridge.common.enums.NotificationChannel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Map;

/**
 * Request payload for dispatching a notification through any AlertBridge channel.
 * Used by the notification-service, scheduler-service, and analytics-service.
 */
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

    // ── Constructors ─────────────────────────────────────────────────────────

    public NotificationRequest() {}

    public NotificationRequest(NotificationChannel channel,
                                String recipient,
                                String subject,
                                String body) {
        this.channel   = channel;
        this.recipient = recipient;
        this.subject   = subject;
        this.body      = body;
    }

    // ── Getters / Setters ────────────────────────────────────────────────────

    public NotificationChannel getChannel() {
        return channel;
    }

    public void setChannel(NotificationChannel channel) {
        this.channel = channel;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }
}


package com.alertbridge.common.enums;

/**
 * Lifecycle states of a notification within AlertBridge.
 */
public enum NotificationStatus {

    /** Notification has been received and queued for delivery. */
    PENDING,

    /** Notification is currently being processed. */
    PROCESSING,

    /** Notification was delivered successfully. */
    SENT,

    /** Delivery failed but will be retried. */
    RETRYING,

    /** Delivery failed permanently after exhausting all retry attempts. */
    FAILED,

    /** Notification was cancelled before delivery. */
    CANCELLED
}


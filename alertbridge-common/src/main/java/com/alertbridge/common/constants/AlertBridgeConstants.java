package com.alertbridge.common.constants;

/**
 * Shared constants used across all AlertBridge microservices.
 */
public final class AlertBridgeConstants {

    private AlertBridgeConstants() {
        // utility class – no instantiation
    }

    // ── HTTP header names ────────────────────────────────────────────────────
    public static final String HEADER_CORRELATION_ID  = "X-Correlation-ID";
    public static final String HEADER_REQUEST_ID      = "X-Request-ID";
    public static final String HEADER_AUTHORIZATION   = "Authorization";
    public static final String BEARER_PREFIX          = "Bearer ";

    // ── API response fields ──────────────────────────────────────────────────
    public static final String RESPONSE_SUCCESS       = "success";
    public static final String RESPONSE_ERROR         = "error";

    // ── Notification channels ────────────────────────────────────────────────
    public static final String CHANNEL_EMAIL          = "EMAIL";
    public static final String CHANNEL_SMS            = "SMS";
    public static final String CHANNEL_PUSH           = "PUSH";

    // ── Default values ───────────────────────────────────────────────────────
    public static final int    DEFAULT_PAGE_SIZE      = 20;
    public static final int    DEFAULT_PAGE_NUMBER    = 0;
    public static final int    MAX_RETRY_ATTEMPTS     = 3;
}


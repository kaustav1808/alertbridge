package com.alertbridge.common.enums;

/**
 * Notification delivery channels supported by AlertBridge.
 */
public enum NotificationChannel {

  /** Email delivery via SMTP / alertbridge-email-service. */
  EMAIL,

  /** SMS delivery via Twilio / alertbridge-sms-service. */
  SMS,

  /** Mobile push notification via FCM / alertbridge-push-service. */
  PUSH
}

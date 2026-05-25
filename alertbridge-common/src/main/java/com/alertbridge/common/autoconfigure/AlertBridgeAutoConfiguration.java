package com.alertbridge.common.autoconfigure;

import com.alertbridge.common.handler.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type;
import org.springframework.context.annotation.Bean;

/**
 * Spring Boot auto-configuration for the AlertBridge Common Library.
 *
 * <p>When {@code alertbridge-common} is on the classpath of a Spring Boot servlet
 * web application, this configuration automatically registers
 * {@link GlobalExceptionHandler} as a bean — no manual component-scan or
 * {@code @Import} is required in the consuming service.
 *
 * <p>The handler is only registered if no other {@code GlobalExceptionHandler}
 * bean is already present (see {@code @ConditionalOnMissingBean}), so services
 * can still provide their own override.
 */
@AutoConfiguration
@ConditionalOnWebApplication(type = Type.SERVLET)
public class AlertBridgeAutoConfiguration {

  /**
   * Registers the shared {@link GlobalExceptionHandler} unless the consuming
   * application declares its own bean of the same type.
   *
   * @return the shared exception handler
   */
  @Bean
  @ConditionalOnMissingBean
  public GlobalExceptionHandler globalExceptionHandler() {
    return new GlobalExceptionHandler();
  }
}


package com.alertbridge.common.autoconfigure;

import com.alertbridge.common.handler.GlobalExceptionHandler;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AlertBridgeAutoConfiguration Tests")
class AlertBridgeAutoConfigurationTests {

  private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
      .withConfiguration(AutoConfigurations.of(AlertBridgeAutoConfiguration.class));

  @Test
  @DisplayName("auto-configuration should register GlobalExceptionHandler by default")
  void autoConfigurationRegistersGlobalExceptionHandler() {
    contextRunner.run(context -> assertThat(context).hasSingleBean(GlobalExceptionHandler.class));
  }

  @Test
  @DisplayName("auto-configuration should back off when user defines GlobalExceptionHandler")
  void autoConfigurationBacksOffWhenUserProvidesHandler() {
    contextRunner
        .withUserConfiguration(UserDefinedGlobalExceptionHandlerConfig.class)
        .run(context -> {
          assertThat(context).hasSingleBean(GlobalExceptionHandler.class);
          assertThat(context).getBean(GlobalExceptionHandler.class).isSameAs(
              UserDefinedGlobalExceptionHandlerConfig.USER_DEFINED_HANDLER);
        });
  }

  @Configuration(proxyBeanMethods = false)
  static class UserDefinedGlobalExceptionHandlerConfig {

    static final GlobalExceptionHandler USER_DEFINED_HANDLER = new GlobalExceptionHandler();

    @Bean
    GlobalExceptionHandler customGlobalExceptionHandler() {
      return USER_DEFINED_HANDLER;
    }
  }
}

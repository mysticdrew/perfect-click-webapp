package com.webapp.config;

public record AppConfig(int port, String environment) {
  private static final int DEFAULT_PORT = 7000;
  private static final String DEFAULT_ENVIRONMENT = "dev";

  public static AppConfig fromEnvironment() {
    String environment = valueOrDefault(System.getenv("APP_ENV"), DEFAULT_ENVIRONMENT);
    int port = parsePort(System.getenv("PORT"));
    return new AppConfig(port, environment);
  }

  private static String valueOrDefault(String value, String fallback) {
    if (value == null || value.isBlank()) {
      return fallback;
    }
    return value;
  }

  private static int parsePort(String rawPort) {
    if (rawPort == null || rawPort.isBlank()) {
      return DEFAULT_PORT;
    }

    try {
      int parsed = Integer.parseInt(rawPort);
      if (parsed < 1 || parsed > 65535) {
        throw new IllegalArgumentException("PORT must be between 1 and 65535.");
      }
      return parsed;
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("PORT must be a valid integer.", ex);
    }
  }
}

package com.webapp.config;

import java.nio.file.Path;
import java.util.Map;

public record AppConfig(
    int port,
    String environment,
    Path configDir,
    ConfigStoreType configStoreType,
    String sqlServerJdbcUrl,
    String sqlServerUsername,
    String sqlServerPassword) {
  private static final int DEFAULT_PORT = 7000;
  private static final String DEFAULT_ENVIRONMENT = "dev";
  private static final String DEFAULT_CONFIG_DIR = "data/configs";
  private static final String DEFAULT_CONFIG_STORE = "file";

  public AppConfig(int port, String environment, Path configDir) {
    this(port, environment, configDir, ConfigStoreType.FILE, "", "", "");
  }

  public static AppConfig fromEnvironment() {
    return fromValues(System.getenv());
  }

  static AppConfig fromValues(Map<String, String> values) {
    String environment = valueOrDefault(values.get("APP_ENV"), DEFAULT_ENVIRONMENT);
    int port = parsePort(values.get("PORT"));
    String rawConfigDir = valueOrDefault(values.get("APP_CONFIG_DIR"), DEFAULT_CONFIG_DIR);
    ConfigStoreType configStoreType =
        ConfigStoreType.fromRaw(
            valueOrDefault(values.get("APP_CONFIG_STORE"), DEFAULT_CONFIG_STORE));
    String sqlServerJdbcUrl = valueOrDefault(values.get("APP_SQLSERVER_JDBC_URL"), "");
    String sqlServerUsername = valueOrDefault(values.get("APP_SQLSERVER_USERNAME"), "");
    String sqlServerPassword = valueOrDefault(values.get("APP_SQLSERVER_PASSWORD"), "");
    if (configStoreType == ConfigStoreType.SQLSERVER && sqlServerJdbcUrl.isBlank()) {
      throw new IllegalArgumentException(
          "APP_SQLSERVER_JDBC_URL must be set when APP_CONFIG_STORE=sqlserver.");
    }
    return new AppConfig(
        port,
        environment,
        Path.of(rawConfigDir),
        configStoreType,
        sqlServerJdbcUrl,
        sqlServerUsername,
        sqlServerPassword);
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

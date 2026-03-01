package com.webapp.api;

import com.webapp.api.model.PingResponse;
import java.time.Clock;

public class PingService {
  private static final int MAX_MESSAGE_LENGTH = 120;
  private final Clock clock;

  public PingService(Clock clock) {
    this.clock = clock;
  }

  public PingResponse ping(String message, String environment) {
    String validatedMessage = validateMessage(message);
    return new PingResponse(validatedMessage, environment, clock.instant().toString());
  }

  static String validateMessage(String message) {
    if (message == null || message.isBlank()) {
      throw new IllegalArgumentException("message must not be blank");
    }
    if (message.length() > MAX_MESSAGE_LENGTH) {
      throw new IllegalArgumentException("message must be 120 characters or fewer");
    }
    return message;
  }
}

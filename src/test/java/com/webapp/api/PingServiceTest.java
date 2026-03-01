package com.webapp.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.webapp.api.model.PingResponse;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

class PingServiceTest {
  @Test
  void pingReturnsExpectedResponse() {
    Clock fixedClock = Clock.fixed(Instant.parse("2026-03-01T12:00:00Z"), ZoneOffset.UTC);
    PingService service = new PingService(fixedClock);

    PingResponse response = service.ping("hello", "test");

    assertEquals("hello", response.message());
    assertEquals("test", response.environment());
    assertEquals("2026-03-01T12:00:00Z", response.timestamp());
  }

  @Test
  void pingRejectsBlankMessage() {
    PingService service = new PingService(Clock.systemUTC());
    assertThrows(IllegalArgumentException.class, () -> service.ping("   ", "test"));
  }

  @Test
  void pingRejectsOverlongMessage() {
    PingService service = new PingService(Clock.systemUTC());
    String longMessage = "x".repeat(121);
    assertThrows(IllegalArgumentException.class, () -> service.ping(longMessage, "test"));
  }
}

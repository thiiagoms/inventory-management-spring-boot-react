package io.thiiagoms.ims.shared.infrastructure.time;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Test;

public class SystemClockTest {

  @Test
  void itReturnsTheCurrentUtcTimestamp() {
    var instant = Instant.parse("2026-08-21T12:00:00Z");
    var clock = new SystemClock(Clock.fixed(instant, ZoneOffset.UTC));

    var timestamp = clock.now();

    assertEquals(instant.toString(), timestamp.value());
  }
}

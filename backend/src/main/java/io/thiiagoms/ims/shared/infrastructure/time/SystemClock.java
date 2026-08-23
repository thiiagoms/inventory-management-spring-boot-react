package io.thiiagoms.ims.shared.infrastructure.time;

import io.thiiagoms.ims.shared.domain.time.Clock;
import io.thiiagoms.ims.shared.domain.valueobject.Timestamp;
import org.springframework.stereotype.Component;

@Component
public class SystemClock implements Clock {

  private final java.time.Clock clock;

  public SystemClock() {
    this(java.time.Clock.systemUTC());
  }

  SystemClock(java.time.Clock clock) {
    this.clock = clock;
  }

  @Override
  public Timestamp now() {
    return new Timestamp(clock.instant().toString());
  }
}

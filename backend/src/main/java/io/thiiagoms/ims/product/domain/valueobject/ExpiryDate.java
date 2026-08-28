package io.thiiagoms.ims.product.domain.valueobject;

import io.thiiagoms.ims.shared.domain.exception.InvalidDomainArgumentException;
import io.thiiagoms.ims.shared.domain.support.Guard;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

public final class ExpiryDate {

  public static final String FIELD = "expiryDate";

  private final LocalDateTime value;

  public ExpiryDate(LocalDateTime value) {
    this(value, Clock.systemDefaultZone(), true);
  }

  private ExpiryDate(LocalDateTime value, Clock clock, boolean validateFuture) {
    Guard.againstNull(FIELD, value);
    Guard.againstNull("clock", clock);

    if (validateFuture && !value.isAfter(LocalDateTime.now(clock))) {
      throw InvalidDomainArgumentException.with("Expiry date must be in the future.", FIELD);
    }

    this.value = value;
  }

  public static ExpiryDate future(LocalDateTime value, Clock clock) {
    return new ExpiryDate(value, clock, true);
  }

  public static ExpiryDate rehydrate(LocalDateTime value) {
    return new ExpiryDate(value, Clock.systemDefaultZone(), false);
  }

  public LocalDateTime value() {
    return value;
  }

  @Override
  public boolean equals(Object other) {
    return this == other || other instanceof ExpiryDate that && value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value);
  }

  @Override
  public String toString() {
    return value.toString();
  }
}
